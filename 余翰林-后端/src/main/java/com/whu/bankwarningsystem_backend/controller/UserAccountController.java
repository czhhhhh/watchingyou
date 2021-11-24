package com.whu.bankwarningsystem_backend.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.whu.bankwarningsystem_backend.userAccount.entity.UserAccount;
import com.whu.bankwarningsystem_backend.userAccount.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *@author 陈小萌
 * @date 2021/07/13
 * @upload 2021/7/10上传第一版
 * @update 更改了delete方法，还未上传到git
 */
//@CrossOrigin(allowCredentials = "true",origins ="http://10.128.134.168:8080")
@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserAccountController {

    @Autowired
    UserAccountService userAccountService;

    public static final String CHINA_REGEX_EXP="^((13[0-9])|(14[5,7,9])|(15[0-3,5-9])|(166)|(17[0-9])|(18[0-9])|(19[1,8,9]))\\d{8}$";
    public static final int PAGE_SIZE=5;

    @RequestMapping(value="/login",method = RequestMethod.POST)
    public String userLogin(String phone,String password){
        System.out.println(phone+"   "+password);
        UserAccount user;
        Map<String,Object> result=new HashMap<String,Object>();
        try{
            user=userAccountService.getUserByAccount(phone);
            if(user!=null&&user.getPassword().equals(password)){
                result.put("data",user);
                result.put("status",200);
            }else if(user!=null&&!user.getPassword().equals(password)){
                result.put("status",201);
                result.put("msg","密码错误");
            }else{
                result.put("status",201);
                result.put("msg","账号不存在");
            }

        }catch (Exception e){
            result.put("status",-1);
            result.put("msg",e.getMessage());
        }
        return JSON.toJSONString(result);
    }

    @RequestMapping(value="/createAccount",method = RequestMethod.POST)
    public String createAccount(String username,String phone){
        Map<String,Object> result=new HashMap<String,Object>();
        Pattern p=Pattern.compile(CHINA_REGEX_EXP);
        Matcher m=p.matcher(phone);
        if(m.matches()){
            try {
                if (userAccountService.getUserByAccount(phone)!=null){
                    result.put("status",201);
                    result.put("msg","创建失败，该账号已存在");
                }else{
                    int re=userAccountService.insertUser(username,phone);
                    if(re==1){
                        result.put("status",200);
                        result.put("msg","创建成功！账号为"+phone+",密码为123456");
                    }else {
                        result.put("status",201);
                        result.put("msg","创建失败");
                    }
                }
            }catch (Exception e){
                result.put("status",-1);
                result.put("msg",e.getMessage());
            }

        }else{
            result.put("status",201);
            result.put("msg","创建失败,电话号不符合规范");
        }
        return JSON.toJSONString(result);
    }


    @RequestMapping(value="/deleteAccount",method = RequestMethod.POST)
    public String deleteAccount(String phone){
        Map<String,Object> result=new HashMap<String,Object>();
        try {
            userAccountService.deleteUser(phone);
        } catch (Exception e) {
            result.put("status",-1);
            result.put("msg", e.getMessage());
        }

        return JSON.toJSONString(result);
    }

    @RequestMapping(value="/getList",method = RequestMethod.POST)
    public String getUserList(int p){
        IPage<UserAccount> page=null;
        Map<String,Object> result=new HashMap<String,Object>();
        try{
            page=userAccountService.getUserAtPage(p,PAGE_SIZE);
            //List<UserAccount> users=userAccountService.getAll();
            result.put("status",200);
            result.put("data",page);
        }catch (Exception e){
            result.put("status", -1);
            result.put("msg", e.getMessage());
        }
        return JSON.toJSONString(result);
    }
}
