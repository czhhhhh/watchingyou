package com.whu.bankwarningsystem_backend.controller;

import com.alibaba.fastjson.JSON;
import com.whu.bankwarningsystem_backend.offlineTransaction.entity.OfflineTransaction;
import com.whu.bankwarningsystem_backend.offlineTransaction.service.OffTranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 *@author 陈小萌
 * @date 2021/07/13
 * @upload 2021/7/17
 */
@RestController
@CrossOrigin
@RequestMapping("/offline")
public class OfflineController {
    @Autowired
    OffTranService offTranService;

    @RequestMapping(value = "/getAmountRangeVolume",method = RequestMethod.POST)
    public String getAmountRangeVolume(String trantype,int day){
        Map<String,Object> result= new HashMap<String,Object>();
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd");
        Date date = new Date();// 获取当前时间
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.add(Calendar.DAY_OF_MONTH, -day);
        try{
            List<OfflineTransaction> volumes=offTranService.getAmountRange(sdf.format(now.getTime()),trantype);
            if (volumes.size()!=0){
                result.put("data",volumes);
                result.put("status",200);
            }else{
                result.put("msg","获取失败，没有找到数据");
                result.put("status",201);
            }
        }catch (Exception e){
            result.put("msg",e.getMessage());
            result.put("status",-1);
        }

        return JSON.toJSONString(result);
    }

    @RequestMapping(value = "/getLocationVolume",method = RequestMethod.POST)
    public String getLocationVolume(String trantype,int day){
        Map<String,Object> result= new HashMap<String,Object>();
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd");
        Date date = new Date();// 获取当前时间
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.add(Calendar.DAY_OF_MONTH, -day);
        try{
            List<OfflineTransaction> volumes=offTranService.getLocation(sdf.format(now.getTime()),trantype);
            if (volumes.size()!=0){
                result.put("data",volumes);
                result.put("status",200);
            }else{
                result.put("msg","获取失败，没有找到数据");
                result.put("status",201);
            }
        }catch (Exception e){
            result.put("msg",e.getMessage());
            result.put("status",-1);
        }

        return JSON.toJSONString(result);
    }

    @RequestMapping(value = "/getAgeRangeVolume",method = RequestMethod.POST)
    public String getAgeRangeVolume(int day){
        Map<String,Object> result= new HashMap<String,Object>();
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd");
        Date date = new Date();// 获取当前时间
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        //now.add(Calendar.DAY_OF_MONTH, -Integer.parseInt(aggtime));
        now.add(Calendar.DAY_OF_MONTH, -day);
        try{
            List<OfflineTransaction> volumes=offTranService.getAgeRange(sdf.format(now.getTime()));
            if (volumes.size()!=0){
                result.put("data",volumes);
                result.put("status",200);
            }else{
                result.put("msg","获取失败，没有找到数据");
                result.put("status",201);
            }
        }catch (Exception e){
            result.put("msg",e.getMessage());
            result.put("status",-1);
        }

        return JSON.toJSONString(result);
    }

    @RequestMapping(value = "/getPurposeVolume",method = RequestMethod.POST)
    public String getPurposeVolume(int day){
        Map<String,Object> result= new HashMap<String,Object>();
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd");
        Date date = new Date();// 获取当前时间
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        //now.add(Calendar.DAY_OF_MONTH, -Integer.parseInt(aggtime));
        now.add(Calendar.DAY_OF_MONTH, -day);
        try{
            List<OfflineTransaction> volumes=offTranService.getPurpose(sdf.format(now.getTime()));
            if (volumes.size()!=0){
                result.put("data",volumes);
                result.put("status",200);
            }else{
                result.put("msg","获取失败，没有找到数据");
                result.put("status",201);
            }
        }catch (Exception e){
            result.put("msg",e.getMessage());
            result.put("status",-1);
        }

        return JSON.toJSONString(result);
    }

    @RequestMapping(value = "/getCurrencyVolume",method = RequestMethod.POST)
    public String getCurrencyVolume(int day){
        Map<String,Object> result= new HashMap<String,Object>();
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd");
        Date date = new Date();// 获取当前时间
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        //now.add(Calendar.DAY_OF_MONTH, -Integer.parseInt(aggtime));
        now.add(Calendar.DAY_OF_MONTH, -day);
        try{
            List<OfflineTransaction> volumes=offTranService.getCurrency(sdf.format(now.getTime()));
            if (volumes.size()!=0){
                result.put("data",volumes);
                result.put("status",200);
            }else{
                result.put("msg","获取失败，没有找到数据");
                result.put("status",201);
            }
        }catch (Exception e){
            result.put("msg",e.getMessage());
            result.put("status",-1);
        }

        return JSON.toJSONString(result);
    }
}
