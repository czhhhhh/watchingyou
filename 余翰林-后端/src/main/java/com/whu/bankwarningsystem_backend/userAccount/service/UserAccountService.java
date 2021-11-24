package com.whu.bankwarningsystem_backend.userAccount.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.whu.bankwarningsystem_backend.userAccount.entity.UserAccount;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *@author 陈小萌
 * @date 2021/07/09
 */

public interface UserAccountService extends IService<UserAccount> {
     UserAccount getUserByAccount( String phoneNum);
     int insertUser(String username, String phone);
     int deleteUser(String phone);
     List<UserAccount> getAll();
     IPage<UserAccount> getUserAtPage(int p, int size);

}
