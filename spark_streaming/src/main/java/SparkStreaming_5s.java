import java_case.Log;
import java_case.Node;
import java_case.UpdatedLog;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.Optional;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function3;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.sql.*;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.State;
import org.apache.spark.streaming.StateSpec;
import org.apache.spark.streaming.api.java.*;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import scala.Tuple2;
import utils.JavaSparkSessionSingleton;
import utils.SparkUtils;

import java.util.*;

/**
 * @author Tian Qi Qing
 * @version 1.0
 * @date 2021/07/20/16:31
 **/
public class SparkStreaming_5s {
    public static String dbUrl = "jdbc:mysql://localhost:3306/bws?useSSL=false&serverTimezone=Asia/Shanghai&rewriteBatchedStatements=true";
    public static String dbUser = "root";
    public static String dbPswd = "root";

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("demo").setMaster("local[4]").set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
        conf.registerKryoClasses((Class<?>[]) Arrays.asList(ConsumerRecord.class).toArray());
        // spark = SparkSession.builder().config(conf).getOrCreate();
        //JavaStreamingContext streamingContext = new JavaStreamingContext(new JavaSparkContext(spark.sparkContext()), Durations.seconds(10));
        JavaStreamingContext streamingContext = new JavaStreamingContext(conf, Durations.seconds(10));
        streamingContext.checkpoint("/tmp/log-analyzer-streaming");


        Map<String, Object> kafkaParams = new HashMap<>();
        //Kafka??????????????????
        kafkaParams.put("bootstrap.servers", "http://192.168.43.203:9092");
        //??????kafka??????key????????????????????????????????????????????????????????????????????????uft-8???
        kafkaParams.put("key.deserializer", StringDeserializer.class);
        //??????kafka??????value????????????????????????????????????????????????????????????????????????uft-8???
        kafkaParams.put("value.deserializer", StringDeserializer.class);
        //?????????ID???????????????
        kafkaParams.put("group.id", "kfk");
        //?????????latest(??????,??????????????????largest????????????)??????smallest(??????)?????????????????????
        kafkaParams.put("auto.offset.reset", "latest");
        //??????true,consumer????????????zookeeper?????????????????????offset
        kafkaParams.put("enable.auto.commit", false);
        //????????????Topic???????????????????????????
        Collection<String> topics = Arrays.asList("bank");

        try{
            // ??????kafka?????????
            final JavaInputDStream<ConsumerRecord<String, String>> stream =
                    KafkaUtils.createDirectStream(
                            streamingContext,
                            LocationStrategies.PreferConsistent(),
                            ConsumerStrategies.Subscribe(topics, kafkaParams)
                    );
            stream.repartition(15);

            // mapWithState??????????????????
            Function3<String, Optional<Log>, State<Log>, Tuple2<String,Log>> mappingFunction =  (key, log, state)->
            {
                if(state.isTimingOut()){
                    return new Tuple2<>(key,state.get());
                }
                //???????????????,????????????log??????stateLog??????
                if(!state.exists()){
                    state.update(log.get());
                    return new Tuple2<>(key,log.get());
                }else{
                    Log stateLog = state.get();
                    Log comingLog = log==null? Log.defaultLog():log.get();
                    //??????????????????
                    if(stateLog.getLogType()==comingLog.getLogType()){
                        stateLog.getNodeInfos().addAll(comingLog.getNodeInfos());
                        stateLog.setCount(stateLog.getCount()+comingLog.getCount());
                        state.update(stateLog);
                        if((stateLog.getTransactionType().startsWith("ATM")&& stateLog.getCount()==7)||stateLog.getCount()==9){
                            return new Tuple2<>(key,stateLog);
                        }
                        else{
                            return new Tuple2<>(key,comingLog);
                        }
                    }
                    //???????????????????????????????????????
                    else if(stateLog.getLogType()==0){
                        stateLog.getNodeInfos().addAll(comingLog.getNodeInfos());
                        stateLog.setCount(stateLog.getCount()+ comingLog.getCount());
                        state.update(stateLog);
                        if((stateLog.getTransactionType().startsWith("ATM")&& stateLog.getCount()==7)||stateLog.getCount()==9){
                            return new Tuple2<>(key,stateLog);
                        }
                        else{
                            return new Tuple2<>(key,comingLog);
                        }
                    }
                    else {
                        comingLog.getNodeInfos().addAll(stateLog.getNodeInfos());
                        comingLog.setCount(comingLog.getCount()+ stateLog.getCount());
                        state.update(comingLog);
                        if((comingLog.getTransactionType().startsWith("ATM")&& comingLog.getCount()==7)||comingLog.getCount()==9){
                            return new Tuple2<>(key,comingLog);
                        }
                        else{
                            return new Tuple2<>(key,stateLog);
                        }
                    }
                }
            };

            //??????log,??????????????????
            //????????????+???????????????key
            JavaMapWithStateDStream<String,Log,Log,Tuple2<String,Log>> stateLogs = stream
                    .flatMap((FlatMapFunction<ConsumerRecord<String, String>, String>) s -> Arrays.asList(s.value().split(System.lineSeparator())).iterator())
                    .mapToPair((PairFunction<String, String, Log>) log -> new Tuple2<>(log.split(",")[1], SparkUtils.parseLog(log)))
                    .mapWithState(StateSpec.function(mappingFunction).timeout(Durations.milliseconds(600)));


            //??????????????????????????????FX????????????????????????ATM??????
            JavaDStream<UpdatedLog> targetLog  =  stateLogs
                    .filter((Function<Tuple2<String,Log>,Boolean>) kv->
                           ((kv._2.getCount()==7&&kv._2.getTransactionType()!=null&&kv._2.getTransactionType().startsWith("ATM"))
                                ||kv._2.getCount()==9))
                     .map((Function<Tuple2<String,Log> , UpdatedLog>) kv -> SparkUtils.convert(kv._2));
            JavaDStream<Node> targetLog_nodeInfo = targetLog.flatMap((FlatMapFunction<UpdatedLog, Node>) SparkUtils::convertUpdatedLog2Node);
            targetLog_nodeInfo.foreachRDD(rdd->{
                //SparkSession spark = SparkSession.builder().config(rdd.context().getConf()).getOrCreate();
                SparkSession spark = JavaSparkSessionSingleton.getInstance(rdd.context().getConf());
                spark.createDataFrame(rdd, Node.class).createOrReplaceTempView("moni2");
                String sql = "select transactionType AS transaction_type,aggTime  AS agg_time,nodeName AS node_name, sum(duration)/count(1) as node_duration, " +
                        " count(1) AS count, \"0\" AS deleted, \"0\" AS checked,  \"normal\" AS status from moni2 GROUP BY transaction_type,agg_time,node_name ";
                Dataset<Row> moniData = spark.sql(sql);
                moniData.write()
                        .format("jdbc")
                        .option("url",dbUrl)
                        .option("dbtable","node_duration_monitor")
                        .option("user",dbUser)
                        .option("password",dbPswd)
                        .option("driver", "com.mysql.cj.jdbc.Driver")
                        .mode(SaveMode.Append)
                        .save();
            });
           targetLog.foreachRDD(rdd-> {
               //SparkSession spark = SparkSession.builder().config(rdd.context().getConf()).getOrCreate();
               SparkSession spark = JavaSparkSessionSingleton.getInstance(rdd.context().getConf());
               spark.createDataFrame(rdd, UpdatedLog.class).createOrReplaceTempView("moni");
               String sql1 = "select transactionType AS transaction_type, timestamp AS agg_time,count(1) AS count,sum(amount) AS volume  " +
                       "from moni GROUP BY transaction_type,agg_time";
               Dataset<Row> performance = spark.sql(sql1);
               performance.write()
                       .format("jdbc")
                       .option("url",dbUrl)
                       .option("dbtable", "realtime_transaction_amount")
                       .option("user", dbUser)
                       .option("password", dbPswd)
                       .mode(SaveMode.Append)
                       .save();

               //????????????
               String sql2 = "select transactionType AS transaction_type , timestamp AS agg_time,sum(amount) AS volume, " +
                       " province AS agg_attr, \"location\" AS agg_name " + "from moni where province is not null  GROUP BY transactionType , agg_time,province";
               Dataset<Row> business_area = spark.sql(sql2);
               business_area.write()
                       .format("jdbc")
                       .option("url",dbUrl)
                       .option("dbtable", "realtime_transaction")
                       .option("user", dbUser)
                       .option("password", dbPswd)
                       .mode(SaveMode.Append)
                       .save();

               //????????????
               String sql3 = "select transactionType AS transaction_type , timestamp  AS agg_time,sum(amount) AS volume, " +
                       " purpose AS agg_attr, \"purpose\" AS agg_name " + "from moni where purpose is not null  GROUP BY transactionType , agg_time,purpose";
               Dataset<Row> business_purpose = spark.sql(sql3);
               business_purpose.write()
                       .format("jdbc")
                       .option("url",dbUrl)
                       .option("dbtable", "realtime_transaction")
                       .option("user", dbUser)
                       .option("password", dbPswd)
                       .mode(SaveMode.Append)
                       .save();

               //????????????
               String sql4 = "select transactionType AS transaction_type , timestamp AS agg_time,sum(amount) AS volume, " +
                       " currency AS agg_attr, \"currency\" AS agg_name " + "from moni where currency is not null  GROUP BY transactionType , agg_time,currency";
               Dataset<Row> business_currency = spark.sql(sql4);
               business_currency.write()
                       .format("jdbc")
                       .option("url",dbUrl)
                       .option("dbtable", "realtime_transaction")
                       .option("user", dbUser)
                       .option("password", dbPswd)
                       .mode(SaveMode.Append)
                       .save();

               //??????????????????
               String sql5 = "select transactionType AS transaction_type , timestamp AS agg_time,sum(amount) AS volume, " +
                       "case when age<18 then \"??????18\" " +
                       "when age<25 and age>=18 then \"18-25\" " +
                       "when age<35 and age>=25 then \"26-35\" " +
                       "when age<45 and age>=35 then \"36-45\" " +
                       "when age<60 and age>=45 then \"46-60\" " +
                       "when age<80 and age>=65 then \"61-80\" " +
                       "else \"81\" END AS agg_attr, " +
                       " \"ageRange\" AS agg_name " + "from moni  GROUP BY transactionType , agg_time,agg_attr";
               Dataset<Row> business_range = spark.sql(sql5);
               business_range.write()
                       .format("jdbc")
                       .option("url",dbUrl)
                       .option("dbtable", "realtime_transaction")
                       .option("user", dbUser)
                       .option("password", dbPswd)
                       .mode(SaveMode.Append)
                       .save();
           });
            streamingContext.start();              // Start the computation
            streamingContext.awaitTermination();   // Wait for the computation to terminate
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
