package com.whu.bankwarningsystem_backend.util;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class NodeDetail {
    private String nodeName;
    private int warning;
    private int normal;

    public NodeDetail(String nodeName, int nodeWaring, int nodeNormal) {
        this.nodeName=nodeName;
        warning=nodeWaring;
        normal=nodeNormal;
    }
}
