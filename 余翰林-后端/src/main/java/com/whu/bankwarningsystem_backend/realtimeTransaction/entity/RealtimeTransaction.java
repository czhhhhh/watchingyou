package com.whu.bankwarningsystem_backend.realtimeTransaction.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 *@author 陈小萌
 * @date 2021/07/10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("realtime_transaction")
public class RealtimeTransaction {
    @TableId
    private int id;
    private String transaction_type;
    private String agg_time;
    private int volume;
    private String agg_name;
    private String agg_attr;
    @TableField(exist = false)
    private int totalVol;
}
