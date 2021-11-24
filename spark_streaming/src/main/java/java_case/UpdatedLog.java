package java_case;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Tian Qi Qing
 * @version 1.0
 * @date 2021/07/13/16:50
 **/
@Data
@Accessors(chain = true)
public class UpdatedLog {
    Boolean succeed;

    String uuid;

    Integer logType;  // 0 for busin , 1 for per

    String transactionType;

    String province;

    String sex;

    String currency;

    String purpose;

    Integer amount;

    Integer age;

    String timestamp;

    String entryNode;

    String serverNode;

    String coreEntryNode;

    String coreNode;

    Integer entryDuration ;

    Integer serverDuration ;

    Integer coreDuration ;

    Integer coreEntryDuration ;
}
