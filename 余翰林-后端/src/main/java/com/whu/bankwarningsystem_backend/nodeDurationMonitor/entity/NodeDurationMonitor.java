package com.whu.bankwarningsystem_backend.nodeDurationMonitor.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 *@author 陈小萌
 * @date 2021/07/14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("node_duration_monitor")
public class NodeDurationMonitor {
    @TableId
    private  int id;
    private int node_duration;
    private String agg_time;
    private String node_name;
    private String transaction_type;
    private String status;
    private int count;
    @TableLogic//(value="0",delval="1")
    private int deleted;
    private int checked;
}
