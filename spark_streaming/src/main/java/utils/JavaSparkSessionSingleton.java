package utils;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;

/**
 * @author Tian Qi Qing
 * @version 1.0
 * @date 2021/07/31/17:05
 **/
public class JavaSparkSessionSingleton {

    /**
     * 懒汉式单例
     */
    private static transient SparkSession instance = null;

    public static SparkSession getInstance(SparkConf sparkConf) {
        if (instance == null) {
            instance = SparkSession
                    .builder()
                    .config(sparkConf)
                    .getOrCreate();
        }
        return instance;
    }


}
