package utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import java_case.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Iterator;

/**
 * @author Tian Qi Qing
 * @version 1.0
 * @date 2021/07/09/16:53
 **/
@Slf4j
public class SparkUtils {
    private static final String BusinessLog = "Business" ;

    private static final String ATMLog = "ATM001" ;


    private static boolean isBusinessLog(String s){
        return StrUtil.startWith(s,BusinessLog);
    }

    private static boolean isATMLog(String s){
        return StrUtil.equals(s,ATMLog);
    }

    private static String getUid(String s){
        return StringUtils.substringAfter(s,"_");
    }

    private static String getValueAfterEqu(String s){ return StringUtils.substringAfter(s,"="); }

    /**
     * 时间戳向下取5的倍数
     * @param s
     * @return
     */
    private static String getTimeStrFloor(String s){
        try {
            Long time = Long.parseLong(getTimeStr(s));
            return String.valueOf(time/5*5);
        }catch (NumberFormatException ex){
            log.error("时间戳包含非数字不能转换为整数类型！");
            return null;
        }
    }

    /**
     * 取年月日时分秒
     * @param s
     * @return
     */
    private static String getTimeStr(String s){
        //年4 + 月2 + 日2 +时2 + 分2 +秒2 = 14
        return s.substring(0,14);
    }


    /**
     * parse text to case_class Log
     * @param str
     * @return
     */
    public static Log parseLog(String str) {
        String[] attrs = str.split(",");
        if (SparkUtils.isBusinessLog(attrs[0])) {
            if (SparkUtils.isATMLog(attrs[2])) {
                //ATM业务日志
                return new Log().setUuid(SparkUtils.getUid(attrs[1]))
                        .setTransactionType(attrs[2])
                        .setProvince(SparkUtils.getValueAfterEqu(attrs[4]))
                        .setSex(SparkUtils.getValueAfterEqu(attrs[5]))
                        .setAge(Integer.parseInt(SparkUtils.getValueAfterEqu(attrs[6])))
                        .setAmount(Integer.parseInt(SparkUtils.getValueAfterEqu(attrs[7])))
                        .setLogType(0)
                        .setCount(1)
                        .setTimestamp(getTimeStrFloor(attrs[3]));
            } else {
                //FX业务日志
                return new Log().setUuid(SparkUtils.getUid(attrs[1]))
                        .setTransactionType(attrs[2])
                        .setProvince(SparkUtils.getValueAfterEqu(attrs[4]))
                        .setCurrency(SparkUtils.getValueAfterEqu(attrs[5]))
                        .setPurpose(SparkUtils.getValueAfterEqu(attrs[6]))
                        .setAmount(Integer.parseInt(SparkUtils.getValueAfterEqu(attrs[7])))
                        .setAge(Integer.parseInt(SparkUtils.getValueAfterEqu(attrs[8])))
                        .setLogType(0)
                        .setCount(1)
                        .setTimestamp(getTimeStrFloor(attrs[3]));
            }
        }
        //else is PerformanceLog
        Log log = new Log().setUuid(SparkUtils.getUid(attrs[1]))
                .setTransactionType(attrs[2])
                .setLogType(1)
                .setTimestamp(getTimeStrFloor(attrs[3]))
                .setCount(1);
        log.getNodeInfos().add(new NodeInfo().setNodeName(attrs[4])
                .setDirection(attrs[5])
                .setTimestamp(Long.parseLong(attrs[3])));
        return log;
    }

    /**
     * convert complex case_class to flat case_class
     * @param log
     * @return
     */
    public static UpdatedLog convert(Log log){
        UpdatedLog newLog = new UpdatedLog();
        BeanUtil.copyProperties(log,newLog);

        if(log.getTransactionType().startsWith("ATM")){
            NodeInfo node_re = log.getNodeInfos().stream().filter(x -> x.getNodeName().endsWith("Entry")&&x.getDirection().equals("reverse")).findFirst().get();
            NodeInfo node_nor =log.getNodeInfos().stream().filter(x -> x.getNodeName().endsWith("Entry")&&x.getDirection().equals("normal")).findFirst().get();
            newLog.setEntryDuration((int) (node_re.getTimestamp()-node_nor.getTimestamp()))
                    .setEntryNode(node_nor.getNodeName());
        }
        else{
            NodeInfo node_re = log.getNodeInfos().stream().filter(x -> x.getNodeName().equals("FXEntry")&&x.getDirection().equals("reverse")).findFirst().get();
            NodeInfo node_nor =log.getNodeInfos().stream().filter(x -> x.getNodeName().equals("FXEntry")&&x.getDirection().equals("normal")).findFirst().get();
            newLog.setEntryDuration((int) (node_re.getTimestamp()-node_nor.getTimestamp()))
                    .setEntryNode(node_nor.getNodeName());

            NodeInfo var1 = log.getNodeInfos().stream().filter(x -> x.getNodeName().endsWith("CoreEntry")&&x.getDirection().equals("reverse")).findFirst().get();
            NodeInfo var2 =log.getNodeInfos().stream().filter(x -> x.getNodeName().endsWith("CoreEntry")&&x.getDirection().equals("normal")).findFirst().get();
            newLog.setCoreEntryDuration((int) (var1.getTimestamp()-var2.getTimestamp()))
                    .setCoreEntryNode(var1.getNodeName());
        }

        NodeInfo node_re = log.getNodeInfos().stream().filter(x -> x.getNodeName().endsWith("Server")&&x.getDirection().equals("reverse")).findFirst().get();
        NodeInfo node_nor =log.getNodeInfos().stream().filter(x -> x.getNodeName().endsWith("Server")&&x.getDirection().equals("normal")).findFirst().get();
        newLog.setServerDuration((int) (node_re.getTimestamp()-node_nor.getTimestamp()))
                .setServerNode(node_nor.getNodeName());

        NodeInfo var1 = log.getNodeInfos().stream().filter(x -> x.getNodeName().equals("CoreService")&&x.getDirection().equals("reverse")).findFirst().get();
        NodeInfo var2 =log.getNodeInfos().stream().filter(x -> x.getNodeName().equals("CoreService")&&x.getDirection().equals("normal")).findFirst().get();
        newLog.setCoreDuration((int) (var1.getTimestamp()-var2.getTimestamp()))
                .setCoreNode(var1.getNodeName());
        return newLog;
    }

    public static Iterator<Node> convertUpdatedLog2Node(UpdatedLog log){
        String type = log.getTransactionType();
        String time = log.getTimestamp();
        Node var1 = new Node().setAggTime(time).setTransactionType(type).setNodeName(log.getEntryNode()).setDuration(log.getEntryDuration());
        Node var2 = new Node().setAggTime(time).setTransactionType(type).setNodeName(log.getServerNode()).setDuration(log.getServerDuration());
        Node var3 = new Node().setAggTime(time).setTransactionType(type).setNodeName(log.getCoreNode()).setDuration(log.getCoreDuration());
        Node var4 = new Node().setAggTime(time).setTransactionType(type).setNodeName(log.getCoreEntryNode()).setDuration(log.getCoreEntryDuration());
        return type.contains("ATM")? Arrays.asList(var1,var2,var3).iterator() : Arrays.asList(var1,var2,var3,var4).iterator();
    }
}
