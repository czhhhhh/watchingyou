package com.whu.bankwarningsystem_backend.userAccount.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whu.bankwarningsystem_backend.userAccount.entity.UserAccount;
import com.whu.bankwarningsystem_backend.userAccount.mapper.UserAccountMapper;
import com.whu.bankwarningsystem_backend.userAccount.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *@author 陈小萌
 * @date 2021/07/09
 */
@Service
public class UserAccountServiceImpl extends ServiceImpl<UserAccountMapper, UserAccount>
        implements UserAccountService {
    @Autowired
    private UserAccountMapper userAccountMapper;


    @Override
    public UserAccount getUserByAccount(String phoneNum) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("phone_num",phoneNum);
        UserAccount user=userAccountMapper.selectOne(wrapper);
        //System.out.println(user);
        return user;
    }

    @Override
    public int insertUser(String username, String phone) {
        UserAccount user=new UserAccount(phone,username,"123456");
        System.out.println(user);
        return userAccountMapper.insert(user);
    }

    @Override
    public int deleteUser(String phoneNum) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("phone_num",phoneNum);
        return userAccountMapper.delete(wrapper);
    }

    @Override
    public List<UserAccount> getAll() {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.select("phone_num","user_name");
        return userAccountMapper.selectList(wrapper);
    }

    @Override
    public IPage<UserAccount> getUserAtPage(int p, int size) {
        Page<UserAccount> page=new Page(p,size);
        QueryWrapper<UserAccount> wrapper=new QueryWrapper();
        wrapper.select().orderByAsc("id");
        IPage<UserAccount> iPage=userAccountMapper.selectPage(page,wrapper);

        return  iPage;
    }


}
