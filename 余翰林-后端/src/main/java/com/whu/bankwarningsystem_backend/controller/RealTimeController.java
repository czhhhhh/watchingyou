package com.whu.bankwarningsystem_backend.controller;

import com.alibaba.fastjson.JSON;
import com.whu.bankwarningsystem_backend.realtimeTransaction.entity.RealtimeTransaction;
import com.whu.bankwarningsystem_backend.realtimeTransaction.service.RtTranService;
import com.whu.bankwarningsystem_backend.rtTranAmount.entity.RtTranAmount;
import com.whu.bankwarningsystem_backend.rtTranAmount.service.RtTranAmountService;
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
 * @upload 2021/7/13上传第一版
 * @update 2021/7/14修改了getVolume和getCount,7/26更改获取volume和count
 */
@RestController
@CrossOrigin
@RequestMapping("/realtime")
public class RealTimeController {
    @Autowired
    RtTranAmountService rtTranAmountService;
    @Autowired
    RtTranService rtTranService;

    public Date getNearestTime(Date date,int period){
        Calendar calendar = Calendar.getInstance();//获取当前时间
        calendar.setTime(date);
        int second = calendar.get(Calendar.SECOND);// 得到分钟
        int count=second%5;
        calendar.add(Calendar.SECOND,-(count+period+120));
       return calendar.getTime();
    }

    public Date plusFiveSeconds(Date date){
        Calendar calendar = Calendar.getInstance();//获取当前时间
        calendar.setTime(date);
        calendar.add(Calendar.SECOND,+5);
        return calendar.getTime();
    }

    public int getpartVolume(String aggTime,String trantype){
        System.out.println("调用gettotalvolume："+aggTime);
        RtTranAmount rt=rtTranAmountService.getTotalVolume(aggTime,trantype);
        if(rt!=null){
            return rt.getVolume();
        }else{
            return 0;
        }
    }

    public int getpartCount(String aggTime,String trantype){
        System.out.println("调用gettotalCount："+aggTime);
        RtTranAmount rt=rtTranAmountService.getTotalCount(aggTime,trantype);
        if(rt!=null){
            return rt.getCount();
        }else{
            return 0;
        }
    }


    @RequestMapping(value = "getVolume",method = RequestMethod.POST)
    public String getVolume(int num, int interval,String trantype){
        Map<String,Object> result= new HashMap<String,Object>();
        //Map<String,Integer>volumes=new HashMap<>();
        List<RtTranAmount> volumes=new ArrayList<>();
        int period=num*interval;  //要求的总时间段
        Date date=new Date();
        System.out.println(date);
        date=getNearestTime(date,period);  //获取要开始查找的第一个时间点
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
        System.out.println(date);
//        int count=interval/5;
//        int total=0;
        try{
            for(int i=0;i<num;i++){
                System.out.println("外循环:"+i);
//                for (int j=0;j<count;j++){
//                    System.out.println("内循环:"+j);
//                    total+=getpartVolume(df.format(date),trantype);
//                    date=plusFiveSeconds(date);
//                }
                RtTranAmount rt=rtTranAmountService.getTotalVolume(df.format(date),trantype);
                if(rt==null){
                    rt=new RtTranAmount();
                }
                rt.setAgg_time(sdf.format(date));
                System.out.println(rt);
                volumes.add(rt);
                date=plusFiveSeconds(date);
            }
            System.out.println(volumes);
            if(volumes.size()!=0){
                result.put("status",200);
                result.put("data",volumes);
                result.put("trantype",trantype);
            }else{
                result.put("status",201);
                result.put("msg","获取失败，没有找到数据");
            }
        }catch (Exception e){
            result.put("status",-1);
            result.put("msg",e.getMessage());
        }


        return JSON.toJSONString(result);
    }

    @RequestMapping(value = "getCount",method = RequestMethod.POST)
    public String getCount(int num, int interval,String trantype){
        Map<String,Object> result= new HashMap<String,Object>();
        //Map<String,Integer>counts=new HashMap<>();
        List<RtTranAmount> counts=new ArrayList<>();
        int period=num*interval;  //要求的总时间段
        Date date=new Date();
        System.out.println(date);
        date=getNearestTime(date,period);  //获取要开始查找的第一个时间点
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
        System.out.println(date);
//        int count=interval/5;
//        int total=0;
        try{
            for(int i=0;i<num;i++){
                //System.out.println("外循环:"+i);
//                for (int j=0;j<count;j++){
//                    //System.out.println("内循环:"+j);
//                    total+=getpartCount(df.format(date),trantype);
//                    date=plusFiveSeconds(date);
//                }
//                counts.put(sdf.format(date),total);
//                total=0;
                RtTranAmount rt=rtTranAmountService.getTotalCount(df.format(date),trantype);
                if(rt==null){
                    rt=new RtTranAmount();
                }
                rt.setAgg_time(sdf.format(date));
                counts.add(rt);
                date=plusFiveSeconds(date);
            }
            //System.out.println(counts);
            if(counts.size()!=0){
                result.put("status",200);
                result.put("data",counts);
                result.put("trantype",trantype);
            }else{
                result.put("status",201);
                result.put("msg","获取失败，没有找到数据");
            }
        }catch (Exception e){
            result.put("status",-1);
            result.put("msg",e.getMessage());
        }
        return JSON.toJSONString(result);
    }

    @RequestMapping(value = "getLocationVolume",method = RequestMethod.POST)
    public String getLocationVolume(String trantype,int interval){
        Map<String,Object> result= new HashMap<String,Object>();
        Map<String,Integer>volumes=new HashMap<>();
        List<RealtimeTransaction> rt;
        Date date=new Date();
        System.out.println(date);
        date=getNearestTime(date,interval);  //获取要开始查找的第一个时间点
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        System.out.println(date);
        int count=interval/5;
        try{
            for(int i=0;i<count;i++){
                //System.out.println("外循环:"+i);
                rt=rtTranService.getLocation(df.format(date),trantype);
                if(rt!=null){
                    for(RealtimeTransaction r:rt){
                        String area=r.getAgg_attr();
                        if(volumes.containsKey(area)){
                            volumes.put(area,volumes.get(area)+r.getTotalVol());
                        }else {
                            volumes.put(area,r.getTotalVol());
                        }
                    }
                }
                date=plusFiveSeconds(date);
            }
            //System.out.println(counts);
            if(volumes.size()!=0){
                result.put("status",200);
                result.put("data",volumes);
                result.put("trantype",trantype);
            }else{
                result.put("status",201);
                result.put("msg","获取失败，没有找到数据");
            }
        }catch (Exception e){
            result.put("status",-1);
            result.put("msg",e.getMessage());
        }
        return JSON.toJSONString(result);
    }

    @RequestMapping(value = "getAgeRangeVolume",method = RequestMethod.POST)
    public String getAgeRangeVolume(int interval){
        Map<String,Object> result= new HashMap<String,Object>();
        Map<String,Integer>volumes=new HashMap<>();
        List<RealtimeTransaction> rt=new ArrayList<>();
        Date date=new Date();
        System.out.println(date);
        date=getNearestTime(date,interval);  //获取要开始查找的第一个时间点
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        System.out.println(date);
        int count=interval/5;
        try{
            for(int i=0;i<count;i++){
                //System.out.println("外循环:"+i);
                rt=rtTranService.getAgeRange(df.format(date));
                if(rt!=null){
                    for(RealtimeTransaction r:rt){
                        String area=r.getAgg_attr();
                        if(volumes.containsKey(area)){
                            volumes.put(area,volumes.get(area)+r.getTotalVol());
                        }else {
                            volumes.put(area,r.getTotalVol());
                        }
                    }
                }
                date=plusFiveSeconds(date);
            }
            //System.out.println(counts);
            if(volumes.size()!=0){
                result.put("status",200);
                result.put("data",volumes);
            }else{
                result.put("status",201);
                result.put("msg","获取失败，没有找到数据");
            }
        }catch (Exception e){
            result.put("status",-1);
            result.put("msg",e.getMessage());
        }
        return JSON.toJSONString(result);
    }

    @RequestMapping(value = "getPurposeVolume",method = RequestMethod.POST)
    public String getPurposeVolume(int interval){
        Map<String,Object> result= new HashMap<String,Object>();
        Map<String,Integer>volumes=new HashMap<>();
        List<RealtimeTransaction> rt=new ArrayList<>();
        Date date=new Date();
        System.out.println(date);
        date=getNearestTime(date,interval);  //获取要开始查找的第一个时间点
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        System.out.println(date);
        int count=interval/5;
        try{
            for(int i=0;i<count;i++){
                //System.out.println("外循环:"+i);
                rt=rtTranService.getPurpose(df.format(date));
                if(rt!=null){
                    for(RealtimeTransaction r:rt){
                        String area=r.getAgg_attr();
                        if(volumes.containsKey(area)){
                            volumes.put(area,volumes.get(area)+r.getTotalVol());
                        }else {
                            volumes.put(area,r.getTotalVol());
                        }
                    }
                }
                date=plusFiveSeconds(date);
            }

            //System.out.println(counts);
            if(volumes.size()!=0){
                result.put("status",200);
                result.put("data",volumes);
            }else{
                result.put("status",201);
                result.put("msg","获取失败，没有找到数据");
            }
        }catch (Exception e){
            result.put("status",-1);
            result.put("msg",e.getMessage());
        }
        return JSON.toJSONString(result);
    }

    @RequestMapping(value = "getCurrencyVolume",method = RequestMethod.POST)
    public String getCurrencyVolume(int interval){
        System.out.println("进入Currency————————————！——————");
        Map<String,Object> result= new HashMap<String,Object>();
        Map<String,Integer>volumes=new HashMap<>();
        List<RealtimeTransaction> rt=new ArrayList<>();
        Date date=new Date();
        System.out.println(date);
        date=getNearestTime(date,interval);  //获取要开始查找的第一个时间点
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        System.out.println(date);
        int count=interval/5;
        try{
            for(int i=0;i<count;i++){
                //System.out.println("外循环:"+i);
                rt=rtTranService.getCurrency(df.format(date));
                if(rt!=null){
                    for(RealtimeTransaction r:rt){
                        String area=r.getAgg_attr();
                        if(volumes.containsKey(area)){
                            volumes.put(area,volumes.get(area)+r.getTotalVol());
                        }else {
                            volumes.put(area,r.getTotalVol());
                        }
                    }
                }
                date=plusFiveSeconds(date);
            }
            System.out.println("Currency的！"+rt);
            //System.out.println(counts);
            if(volumes.size()!=0){
                result.put("status",200);
                result.put("data",volumes);
            }else{
                result.put("status",201);
                result.put("msg","获取失败，没有找到数据");
            }
        }catch (Exception e){
            result.put("status",-1);
            result.put("msg",e.getMessage());
        }
        return JSON.toJSONString(result);
    }
}
