package java_case;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author Tian Qi Qing
 * @version 1.0
 * @date 2021/07/09/23:58
 **/
@Data
@Accessors(chain = true)
public class NodeInfo implements Serializable {
    String nodeName;

    String direction;

    long timestamp;
}
