package com.whu.bankwarningsystem_backend.rtTranAmount.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 *@author 陈小萌
 * @date 2021/07/123
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("realtime_transaction_amount")
public class RtTranAmount {
    @TableId
    private int id;
    private String transaction_type;
    private int count;
    private int volume;
    private String agg_time;
}
