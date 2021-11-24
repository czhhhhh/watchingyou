package com.whu.bankwarningsystem_backend.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.whu.bankwarningsystem_backend.util.NodeDetail;
import com.whu.bankwarningsystem_backend.nodeDurationMonitor.entity.NodeDurationMonitor;
import com.whu.bankwarningsystem_backend.nodeDurationMonitor.service.NodeDurationMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 *@author 陈小萌
 * @date 2021/07/14
 * @upload 2021/7/14上传第一版
 * @update 对多线程访问数据库获取数据进行了修改
 */
@RestController
@CrossOrigin
@RequestMapping("/monitor")
public class MonitorController {
    @Autowired
    NodeDurationMonitorService nodeDurationMonitorService;

//    @Autowired
//    @Resource(name = "taskExecutor")
//    ThreadPoolTaskExecutor taskExecutor;

    public static final int PAGE_SIZE=10;
    public static final int ATMENTRY_KPI=130;
    public static final int ATMSERVER_KPI=70;
    public static final int CORESERVICE_KPI=45;
    public static final int FXENTRY_KPI=200;
    public static final int FXSERVER_KPI=115;
    public static final int FXCOREENTRY_KPI=80;
    public volatile static int Version=0;
    public volatile static int Version_1=0;
    public volatile static int Version_2=0;
    public volatile static int Version_3=0;
    public volatile static int Version_4=0;
    public volatile static int Version_5=0;

    public Date getNearestTime(Date date, int period){
        Calendar calendar = Calendar.getInstance();//获取当前时间
        calendar.setTime(date);
        int second = calendar.get(Calendar.SECOND);// 得到分钟
        int count=second%5;
        calendar.add(Calendar.SECOND,-(count+period+60));
        return calendar.getTime();
    }

    public Date plusFiveSeconds(Date date){
        Calendar calendar = Calendar.getInstance();//获取当前时间
        calendar.setTime(date);
        calendar.add(Calendar.SECOND,+5);
        return calendar.getTime();
    }
    public NodeDurationMonitor checkNode(String time,String nodeName,int kpi){
        NodeDurationMonitor nd=nodeDurationMonitorService.getDurationFirst(time,nodeName);
        if(nd!=null){
            if(nd.getNode_duration()>kpi){
                nd.setStatus("warning");
                nodeDurationMonitorService.updateStatus(nd.getId());
            }
        }
        System.out.println("node的status："+nd.getStatus());
        return nd;
    }

    @RequestMapping(value = "/getATMEntryDuration",method = RequestMethod.POST)
    public String getATMEntryDuration(int num,String nodeName){
        Map<String,Object> result=new HashMap<>();
        List<NodeDurationMonitor>durations=new ArrayList<>();
        int period=num*5;  //要求的总时间段
        Date date=new Date();
        System.out.println(date);
        date=getNearestTime(date,period);  //获取要开始查找的第一个时间点
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
        System.out.println(date);
        int kpi=0;
        switch(nodeName){
            case "ATMEntry":
                kpi=ATMENTRY_KPI;
                break;
            case "ATMServer":
                kpi=ATMSERVER_KPI;
                break;
            case "FXCoreEntry":
                kpi=FXCOREENTRY_KPI;
                break;
            case "FXEntry":
                kpi=FXENTRY_KPI;
                break;
            case "FXServer":
                kpi=FXSERVER_KPI;
                break;
            default:
                kpi=CORESERVICE_KPI;
        }
        //int total=0;
        try{
            for(int i=0;i<num;i++){
                //System.out.println("外循环:"+i);
                //NodeDurationMonitor nd=nodeDurationMonitorService.getDuration(df.format(date),nodeName);
//                if(nd==null){
//                    nd=new NodeDurationMonitor();
//                    nd.setNode_duration(0);
//                    nd.setStatus("warning");
//                    //nd.setAgg_time(sdf.format(date));
//                   // Date temp=df.parse(nd.getAgg_time());
//                }else{
//                    if(nd.getNode_duration()>kpi&&nd.getChecked()!=1){
//                        nodeDurationMonitorService.updateStatus(nd.getId());
//                        nd.setStatus("warning");
//                    }
//                }
                NodeDurationMonitor nd=new NodeDurationMonitor();
                if(MonitorController.Version==0){
                    synchronized (MonitorController.class){
                        if(MonitorController.Version==0){
//                            nd=nodeDurationMonitorService.getDurationFirst(df.format(date),nodeName);
//                            if(nd!=null){
//                                if(nd.getNode_duration()>kpi){
//                                    nd.setStatus("warning");
//                                    nodeDurationMonitorService.updateStatus(nd.getId());
//                                }
//                            }
                            nd=checkNode(df.format(date),nodeName,kpi);
                            MonitorController.Version=1;
                        }else{
                            nd=checkNode(df.format(date),nodeName,kpi);
                            MonitorController.Version=0;
                        }
                    }
                }else{
                    nd=checkNode(df.format(date),nodeName,kpi);
                    MonitorController.Version=0;
                }
                nd.setAgg_time(sdf.format(date));
                durations.add(nd);
                date=plusFiveSeconds(date);
                //total=0;
            }
            //System.out.println(counts);
            if(durations.size()!=0){
                result.put("status",200);
                result.put("data",durations);
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
    @RequestMapping(value = "/getATMServerDuration",method = RequestMethod.POST)
    public String getATMServerDuration(int num,String nodeName){
        Map<String,Object> result=new HashMap<>();
        List<NodeDurationMonitor>durations=new ArrayList<>();
        int period=num*5;  //要求的总时间段
        Date date=new Date();
        System.out.println(date);
        date=getNearestTime(date,period);  //获取要开始查找的第一个时间点
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
        System.out.println(date);
        int kpi=0;
        switch(nodeName){
            case "ATMEntry":
                kpi=ATMENTRY_KPI;
                break;
            case "ATMServer":
                kpi=ATMSERVER_KPI;
                break;
            case "FXCoreEntry":
                kpi=FXCOREENTRY_KPI;
                break;
            case "FXEntry":
                kpi=FXENTRY_KPI;
                break;
            case "FXServer":
                kpi=FXSERVER_KPI;
                break;
            default:
                kpi=CORESERVICE_KPI;
        }
        //int total=0;
        try{
            for(int i=0;i<num;i++){

                NodeDurationMonitor nd=new NodeDurationMonitor();
                if(MonitorController.Version_1==0){
                    synchronized (MonitorController.class){
                        if(MonitorController.Version_1==0){
                            nd=checkNode(df.format(date),nodeName,kpi);
                            MonitorController.Version_1=1;
                        }else{
                            nd=checkNode(df.format(date),nodeName,kpi);
                            MonitorController.Version_1=0;
                        }
                    }
                }else{
                    nd=checkNode(df.format(date),nodeName,kpi);
                    MonitorController.Version_1=0;
                }
                nd.setAgg_time(sdf.format(date));
                durations.add(nd);
                date=plusFiveSeconds(date);
                //total=0;
            }
            //System.out.println(counts);
            if(durations.size()!=0){
                result.put("status",200);
                result.put("data",durations);
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
    @RequestMapping(value = "/getFXEntryDuration",method = RequestMethod.POST)
    public String getFXEntryDuration(int num,String nodeName){
        Map<String,Object> result=new HashMap<>();
        List<NodeDurationMonitor>durations=new ArrayList<>();
        int period=num*5;  //要求的总时间段
        Date date=new Date();
        System.out.println(date);
        date=getNearestTime(date,period);  //获取要开始查找的第一个时间点
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
        System.out.println(date);
        int kpi=0;
        switch(nodeName){
            case "ATMEntry":
                kpi=ATMENTRY_KPI;
                break;
            case "ATMServer":
                kpi=ATMSERVER_KPI;
                break;
            case "FXCoreEntry":
                kpi=FXCOREENTRY_KPI;
                break;
            case "FXEntry":
                kpi=FXENTRY_KPI;
                break;
            case "FXServer":
                kpi=FXSERVER_KPI;
                break;
            default:
                kpi=CORESERVICE_KPI;
        }
        //int total=0;
        try{
            for(int i=0;i<num;i++){

                NodeDurationMonitor nd=new NodeDurationMonitor();
                if(MonitorController.Version_2==0){
                    synchronized (MonitorController.class){
                        if(MonitorController.Version_2==0){
                            nd=checkNode(df.format(date),nodeName,kpi);
                            MonitorController.Version_2=1;
                        }else{
                            nd=checkNode(df.format(date),nodeName,kpi);
                            MonitorController.Version_2=0;
                        }
                    }
                }else{
                    nd=checkNode(df.format(date),nodeName,kpi);
                    MonitorController.Version_2=0;
                }
                nd.setAgg_time(sdf.format(date));
                durations.add(nd);
                date=plusFiveSeconds(date);
                //total=0;
            }
            //System.out.println(counts);
            if(durations.size()!=0){
                result.put("status",200);
                result.put("data",durations);
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
    @RequestMapping(value = "/getFXServerDuration",method = RequestMethod.POST)
    public String getFXServerDuration(int num,String nodeName){
        Map<String,Object> result=new HashMap<>();
        List<NodeDurationMonitor>durations=new ArrayList<>();
        int period=num*5;  //要求的总时间段
        Date date=new Date();
        System.out.println(date);
        date=getNearestTime(date,period);  //获取要开始查找的第一个时间点
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
        System.out.println(date);
        int kpi=0;
        switch(nodeName){
            case "ATMEntry":
                kpi=ATMENTRY_KPI;
                break;
            case "ATMServer":
                kpi=ATMSERVER_KPI;
                break;
            case "FXCoreEntry":
                kpi=FXCOREENTRY_KPI;
                break;
            case "FXEntry":
                kpi=FXENTRY_KPI;
                break;
            case "FXServer":
                kpi=FXSERVER_KPI;
                break;
            default:
                kpi=CORESERVICE_KPI;
        }
        //int total=0;
        try{
            for(int i=0;i<num;i++){
                NodeDurationMonitor nd=new NodeDurationMonitor();
                if(MonitorController.Version_3==0){
                    synchronized (MonitorController.class){
                        if(MonitorController.Version_3==0){
                            nd=checkNode(df.format(date),nodeName,kpi);
                            MonitorController.Version_3=1;
                        }else{
                            nd=checkNode(df.format(date),nodeName,kpi);
                            MonitorController.Version_3=0;
                        }
                    }
                }else{
                    nd=checkNode(df.format(date),nodeName,kpi);
                    MonitorController.Version_3=0;
                }
                nd.setAgg_time(sdf.format(date));
                durations.add(nd);
                date=plusFiveSeconds(date);
                //total=0;
            }
            //System.out.println(counts);
            if(durations.size()!=0){
                result.put("status",200);
                result.put("data",durations);
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
    @RequestMapping(value = "/getFXCoreEntryDuration",method = RequestMethod.POST)
    public String getFXCoreEntryDuration(int num,String nodeName){
        Map<String,Object> result=new HashMap<>();
        List<NodeDurationMonitor>durations=new ArrayList<>();
        int period=num*5;  //要求的总时间段
        Date date=new Date();
        System.out.println(date);
        date=getNearestTime(date,period);  //获取要开始查找的第一个时间点
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
        System.out.println(date);
        int kpi=0;
        switch(nodeName){
            case "ATMEntry":
                kpi=ATMENTRY_KPI;
                break;
            case "ATMServer":
                kpi=ATMSERVER_KPI;
                break;
            case "FXCoreEntry":
                kpi=FXCOREENTRY_KPI;
                break;
            case "FXEntry":
                kpi=FXENTRY_KPI;
                break;
            case "FXServer":
                kpi=FXSERVER_KPI;
                break;
            default:
                kpi=CORESERVICE_KPI;
        }
        //int total=0;
        try{
            for(int i=0;i<num;i++){
                NodeDurationMonitor nd=new NodeDurationMonitor();
                if(MonitorController.Version_4==0){
                    synchronized (MonitorController.class){
                        if(MonitorController.Version_4==0){
                            nd=checkNode(df.format(date),nodeName,kpi);
                            MonitorController.Version_4=1;
                        }else{
                            nd=checkNode(df.format(date),nodeName,kpi);
                            MonitorController.Version_4=0;
                        }
                    }
                }else{
                    nd=checkNode(df.format(date),nodeName,kpi);
                    MonitorController.Version_4=0;
                }
                nd.setAgg_time(sdf.format(date));
                durations.add(nd);
                date=plusFiveSeconds(date);
                //total=0;
            }
            //System.out.println(counts);
            if(durations.size()!=0){
                result.put("status",200);
                result.put("data",durations);
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
    @RequestMapping(value = "/getCoreServiceDuration",method = RequestMethod.POST)
    public String getCoreServiceDuration(int num,String nodeName){
        Map<String,Object> result=new HashMap<>();
        List<NodeDurationMonitor>durations=new ArrayList<>();
        int period=num*5;  //要求的总时间段
        Date date=new Date();
        System.out.println(date);
        date=getNearestTime(date,period);  //获取要开始查找的第一个时间点
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
        System.out.println(date);
        int kpi=0;
        switch(nodeName){
            case "ATMEntry":
                kpi=ATMENTRY_KPI;
                break;
            case "ATMServer":
                kpi=ATMSERVER_KPI;
                break;
            case "FXCoreEntry":
                kpi=FXCOREENTRY_KPI;
                break;
            case "FXEntry":
                kpi=FXENTRY_KPI;
                break;
            case "FXServer":
                kpi=FXSERVER_KPI;
                break;
            default:
                kpi=CORESERVICE_KPI;
        }
        //int total=0;
        try{
            for(int i=0;i<num;i++){
                NodeDurationMonitor nd=new NodeDurationMonitor();
                if(MonitorController.Version_5==0){
                    synchronized (MonitorController.class){
                        if(MonitorController.Version_5==0){
                            nd=checkNode(df.format(date),nodeName,kpi);
                            MonitorController.Version_5=1;
                        }else{
                            nd=checkNode(df.format(date),nodeName,kpi);
                            MonitorController.Version_5=0;
                        }
                    }
                }else{
                    nd=checkNode(df.format(date),nodeName,kpi);
                    MonitorController.Version_5=0;
                }
                nd.setAgg_time(sdf.format(date));
                durations.add(nd);
                date=plusFiveSeconds(date);
                //total=0;
            }
            //System.out.println(counts);
            if(durations.size()!=0){
                result.put("status",200);
                result.put("data",durations);
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

    @RequestMapping(value = "/getWarningList",method = RequestMethod.POST)
    public String getWarningList(int p){
        Map<String,Object> result=new HashMap<>();
        IPage<NodeDurationMonitor> page;
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try{
            page=nodeDurationMonitorService.getWarning(p,PAGE_SIZE);
            if(page!=null){
                List<NodeDurationMonitor> nds=page.getRecords();
                for (NodeDurationMonitor nd:nds){
                    String time=nd.getAgg_time();
                    Date date=df.parse(time);
                    nd.setAgg_time(sdf.format(date));
                }
                page.setRecords(nds);
                result.put("data",page);
                result.put("status",200);
            }else{
                result.put("status",201);
                result.put("msg","获取失败，找不到数据");
            }
        }catch (Exception e){
            result.put("status", -1);
            result.put("msg", e.getMessage());
        }
        return JSON.toJSONString(result);
    }

//    @Async("taskExecutor")
//    @RequestMapping(value = "/getNodePercent")
//    public String getNodePercent(){
//        long start = System.currentTimeMillis();
//        CountDownLatch latch = new CountDownLatch(6);
//        List<Future<NodeDetail>> futures=new ArrayList<>();
//
//        Map<String,Object> result=new HashMap<>();
//        List<NodeDetail> nodes=new ArrayList<>();
//
//        int period=12*5*60;  //要求的总时间段
//        Date endDate=new Date();
//        System.out.println(endDate);
//        endDate=getNearestTime(endDate,0);
//        Date startDate=getNearestTime(endDate,period);  //获取要开始查找的第一个时间点
//        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
//        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
//        String startTime=df.format(startDate);
//        String endTime=df.format(endDate);
//
//        futures.add(taskExecutor.submit(new Callable<NodeDetail>() {
//            @Override
//            public NodeDetail call() throws Exception {
//                int warning=nodeDurationMonitorService.getNodeWaring(startTime,endTime,"ATMEntry");
//                int normal=nodeDurationMonitorService.getNodeCount(startTime,endTime,"ATMEntry")-warning;
//                latch.countDown();
//                return new NodeDetail("ATMEntry",warning,normal);
//            }
//        }));
//        futures.add(taskExecutor.submit(new Callable<NodeDetail>() {
//            @Override
//            public NodeDetail call() throws Exception {
//                int warning=nodeDurationMonitorService.getNodeWaring(startTime,endTime,"ATMServer");
//                int normal=nodeDurationMonitorService.getNodeCount(startTime,endTime,"ATMServer")-warning;
//                latch.countDown();
//                return new NodeDetail("ATMServer",warning,normal);
//            }
//        }));
//        futures.add(taskExecutor.submit(new Callable<NodeDetail>() {
//            @Override
//            public NodeDetail call() throws Exception {
//                int warning=nodeDurationMonitorService.getNodeWaring(startTime,endTime,"CoreService");
//                int normal=nodeDurationMonitorService.getNodeCount(startTime,endTime,"CoreService")-warning;
//                latch.countDown();
//                return new NodeDetail("CoreService",warning,normal);
//            }
//        }));
//        futures.add(taskExecutor.submit(new Callable<NodeDetail>() {
//            @Override
//            public NodeDetail call() throws Exception {
//                int warning=nodeDurationMonitorService.getNodeWaring(startTime,endTime,"FXEntry");
//                int normal=nodeDurationMonitorService.getNodeCount(startTime,endTime,"FXEntry")-warning;
//                latch.countDown();
//                return new NodeDetail("FXEntry",warning,normal);
//            }
//        }));
//        futures.add(taskExecutor.submit(new Callable<NodeDetail>() {
//            @Override
//            public NodeDetail call() throws Exception {
//                int warning=nodeDurationMonitorService.getNodeWaring(startTime,endTime,"FXServer");
//                int normal=nodeDurationMonitorService.getNodeCount(startTime,endTime,"FXServer")-warning;
//                latch.countDown();
//                return new NodeDetail("FXServer",warning,normal);
//            }
//        }));
//        futures.add(taskExecutor.submit(new Callable<NodeDetail>() {
//            @Override
//            public NodeDetail call() throws Exception {
//                int warning=nodeDurationMonitorService.getNodeWaring(startTime,endTime,"CoreEntry");
//                int normal=nodeDurationMonitorService.getNodeCount(startTime,endTime,"CoreEntry")-warning;
//                latch.countDown();
//                return new NodeDetail("CoreEntry",warning,normal);
//            }
//        }));
//
//        try{
//            for(Future<NodeDetail> future:futures){
//                System.out.println(future.get());
//                nodes.add(future.get());
//            }
//          System.out.println("现在是返回的结果："+nodes);
//            System.out.println("任务耗时："+(System.currentTimeMillis() - start)+"毫秒");
//          if(nodes!=null){
//              result.put("status",200);
//              result.put("data",nodes);
//              result.put("startTime",sdf.format(startDate));
//              result.put("endTime",sdf.format(endDate));
//          }else{
//              result.put("status",201);
//              result.put("msg","获取失败，没有找到数据");
//          }
//        }catch (Exception e){
//            result.put("status",-1);
//            result.put("msg",e.getMessage());
//        }
//        return JSON.toJSONString(result);
//    }
    @RequestMapping(value = "/getNodePercent")
    public String getNodePercent(){
        Map<String,Object> result=new HashMap<>();
        List<NodeDetail> nodes=new ArrayList<>();

        int period=12*5*10;  //要求的总时间段
        Date endDate=new Date();
        System.out.println(endDate);
        endDate=getNearestTime(endDate,0);
        Date startDate=getNearestTime(endDate,period);  //获取要开始查找的第一个时间点
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
        String startTime=df.format(startDate);
        String endTime=df.format(endDate);

        try{
            nodes=nodeDurationMonitorService.getNodeList(startTime,endTime);
            System.out.println("现在是返回的结果："+nodes);
            if(nodes!=null){
                result.put("status",200);
                result.put("data",nodes);
                result.put("startTime",sdf.format(startDate));
                result.put("endTime",sdf.format(endDate));
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
    @RequestMapping(value = "/updateWarning",method = RequestMethod.POST)
    public String updateWarning(int id){
        Map<String,Object> result=new HashMap<>();
        try {
            int re=nodeDurationMonitorService.updateWarning(id,"normal");
            if (re==1){
                result.put("status",200);
                result.put("msg","处理成功");
            }else{
                result.put("status",201);
                result.put("msg","处理失败");
            }
        }catch (Exception e){
            result.put("status", -1);
            result.put("msg", e.getMessage());
        }
        return JSON.toJSONString(result);
    }

    @RequestMapping(value = "/getPartDuration",method = RequestMethod.POST)
    public String getPartDuration(String nodeName){
        Map<String,Object> result=new HashMap<>();
        int kpi=0;
        switch(nodeName){
            case "ATMEntry":
                kpi=ATMENTRY_KPI;
                break;
            case "ATMServer":
                kpi=ATMSERVER_KPI;
                break;
            case "FXCoreEntry":
                kpi=FXCOREENTRY_KPI;
                break;
            case "FXEntry":
                kpi=FXENTRY_KPI;
                break;
            case "FXServer":
                kpi=FXSERVER_KPI;
                break;
            default:
                kpi=CORESERVICE_KPI;
        }
        System.out.println(nodeName+"的kpi为"+kpi);
        Date date=new Date();
        date=getNearestTime(date,0);
        System.out.println(date);
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        try{
            NodeDurationMonitor ndm=nodeDurationMonitorService.getDurationFirst(df.format(date),nodeName);
            if(ndm!=null){
                if(ndm.getNode_duration()>kpi){
                    ndm.setStatus("warning");
                    nodeDurationMonitorService.updateStatus(ndm.getId());
                }
                result.put("data",ndm);
                result.put("status",200);
            }else{
                result.put("status",201);
                result.put("msg","获取失败，找不到数据");
            }
        }catch (Exception e){
            result.put("status", -1);
            result.put("msg", e.getMessage());
        }
        return JSON.toJSONString(result);
    }




}
