package java_case;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tian Qi Qing
 * @version 1.0
 * @date 2021/07/09/22:42
 **/
@Data
@Accessors(chain = true)
public class Log implements Serializable {

    String uuid;

    Integer logType;  // 0 for busin , 1 for per

    String transactionType;

    String province;

    String sex;

    String currency;

    String purpose;

    Integer amount;

    Integer age;

    Integer count ;

    String timestamp;

    List<NodeInfo> nodeInfos = new ArrayList<>();

    public static Log defaultLog(){
        return new Log().setCount(0).setLogType(0);
    }
}