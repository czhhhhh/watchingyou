package com.whu.bankwarningsystem_backend.userAccount.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 *@author 陈小萌
 * @date 2021/07/09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_account")
public class UserAccount implements Serializable {
    @TableId
    private int id;
    private String phone_num;
    private String user_name;
    private String password;
    private int role_type;
    @TableLogic
    private int deleted;

    public UserAccount(String phone_num, String user_name, String password) {
        this.phone_num = phone_num;
        this.user_name = user_name;
        this.password = password;
    }

}
