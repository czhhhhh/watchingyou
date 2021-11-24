package java_case;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author Tian Qi Qing
 * @version 1.0
 * @date 2021/07/21/16:39
 **/
@Data
@Accessors(chain = true)
public class Node  implements Serializable {

    String aggTime;

    String nodeName;

    String transactionType;

    Integer duration ;
}
