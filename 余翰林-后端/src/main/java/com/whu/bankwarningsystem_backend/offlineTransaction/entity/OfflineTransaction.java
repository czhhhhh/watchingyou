package com.whu.bankwarningsystem_backend.offlineTransaction.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 *@author 陈小萌
 * @date 2021/07/09
 * @update 删了id字段，未上传git
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("offline_transaction")
public class OfflineTransaction {

    private String transaction_type;
    private int volume;
    private int count;
    private String agg_name;
    private String agg_date;
    private String agg_attr;
}
