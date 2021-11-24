package com.whu.bankwarningsystem_backend.nodeDurationMonitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whu.bankwarningsystem_backend.nodeDurationMonitor.entity.NodeDurationMonitor;
import com.whu.bankwarningsystem_backend.nodeDurationMonitor.mapper.NodeDurationMonitorMapper;
import com.whu.bankwarningsystem_backend.nodeDurationMonitor.service.NodeDurationMonitorService;
import com.whu.bankwarningsystem_backend.util.NodeDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.soap.Node;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;


/**
 *@author 陈小萌
 * @date 2021/07/015
 * @uploda 2021/7/17
 * @update 增加修改新数据的status的功能
 */
@Service
public class NodeDurationMonitorServiceImpl
        extends ServiceImpl<NodeDurationMonitorMapper, NodeDurationMonitor>
        implements NodeDurationMonitorService {

    @Autowired
    NodeDurationMonitorMapper nodeDurationMonitorMapper;
    


    @Override
    public NodeDurationMonitor getDuration(String aggtime,String node) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("agg_time",aggtime);
        wrapper.eq("node_name",node);
       // wrapper.eq("transaction_type",trantype);
        wrapper.select("node_duration","status","id");
        NodeDurationMonitor result=nodeDurationMonitorMapper.selectOne(wrapper);
        return result;
    }

    @Override
    public int getNodeWaring(String startTime, String endTime, String node) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("node_name",node);
        wrapper.eq("status","warning");
        wrapper.between("agg_time",startTime,endTime);
        int result=nodeDurationMonitorMapper.selectCount(wrapper);
        return result;
    }

    @Override

    public int getNodeCount(String startTime, String endTime,  String node) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("node_name",node);
        wrapper.between("agg_time",startTime,endTime);
        int result=nodeDurationMonitorMapper.selectCount(wrapper);
        return result;
    }

    @Override
    public List<NodeDetail> getNodeList(String startTime, String endTime) throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        CountDownLatch latch = new CountDownLatch(6);
        //List<Future<NodeDetail>> futures=new ArrayList<>();
        List<NodeDetail> nodes=new ArrayList<>();

        ExecutorService exec = Executors.newFixedThreadPool(6);
        List<Callable<Integer>> tasks = new ArrayList<>();
        tasks.add(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                int warning=getNodeWaring(startTime,endTime,"ATMEntry");
                int normal=getNodeCount(startTime,endTime,"ATMEntry")-warning;
                latch.countDown();
                nodes.add( new NodeDetail("ATMEntry",warning,normal));
                return 1;
            }
        });
        tasks.add(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                int warning=getNodeWaring(startTime,endTime,"ATMServer");
                int normal=getNodeCount(startTime,endTime,"ATMServer")-warning;
                latch.countDown();
                nodes.add(new NodeDetail("ATMServer",warning,normal));
                return 1;
            }
        });
        tasks.add(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                int warning=getNodeWaring(startTime,endTime,"CoreService");
                int normal=getNodeCount(startTime,endTime,"CoreService")-warning;
                latch.countDown();
                nodes.add(new NodeDetail("CoreService",warning,normal));
                return 1;
            }
        });
        tasks.add(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                int warning=getNodeWaring(startTime,endTime,"FXEntry");
                int normal=getNodeCount(startTime,endTime,"FXEntry")-warning;
                latch.countDown();
                nodes.add(new NodeDetail("FXEntry",warning,normal));
                return 1;
            }
        });
        tasks.add(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                int warning=getNodeWaring(startTime,endTime,"FXServer");
                int normal=getNodeCount(startTime,endTime,"FXServer")-warning;
                latch.countDown();
                nodes.add(new NodeDetail("FXServer",warning,normal));
                return 1;
            }
        });
        tasks.add(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                int warning=getNodeWaring(startTime,endTime,"FXCoreEntry");
                int normal=getNodeCount(startTime,endTime,"FXCoreEntry")-warning;
                latch.countDown();
                nodes.add( new NodeDetail("FXCoreEntry",warning,normal));
                return 1;
            }
        });
        exec.invokeAll(tasks);
        exec.shutdown();
        System.out.println("在service层："+nodes);
        System.out.println("任务耗时："+(System.currentTimeMillis() - start)+"毫秒");
        return nodes;
    }

    @Override
    public int updateStatus(int id) {
        UpdateWrapper updateWrapper=new UpdateWrapper();
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("id",id);
        NodeDurationMonitor result=nodeDurationMonitorMapper.selectOne(wrapper);
        updateWrapper.eq("id",id);
        updateWrapper.eq("checked",0);
        updateWrapper.set("status","warning");
        updateWrapper.set("checked",1);
        return nodeDurationMonitorMapper.update(result,updateWrapper);
    }

    @Override
    public NodeDurationMonitor getDurationFirst(String aggtime, String node) {
        List<Integer>idList=new ArrayList<>();
        int duration=0;
        int count=0;
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("agg_time",aggtime);
        wrapper.eq("node_name",node);
        // wrapper.eq("transaction_type",trantype);
        wrapper.select("node_duration","status","id","count");
        List<NodeDurationMonitor> res=nodeDurationMonitorMapper.selectList(wrapper);
        if(res!=null&&res.size()!=0){
            if(res.size()==1){
                NodeDurationMonitor result=res.get(0);
                return result;
            }else{
                for(NodeDurationMonitor n:res){
                    idList.add(n.getId());
                    count+=n.getCount();
                    duration+=n.getCount()*n.getNode_duration();
                    //System.out.println(duration);
                }
                System.out.println("现在的count是："+count);
                if(count!=0){
                    duration=duration/count;
                }
                nodeDurationMonitorMapper.deleteBatchIds(idList);
            }
        }
        NodeDurationMonitor result=new NodeDurationMonitor();
        result.setNode_duration(duration);result.setCount(count);
        result.setStatus("normal");result.setAgg_time(aggtime);result.setNode_name(node);result.setChecked(1);
        nodeDurationMonitorMapper.insert(result);
        //result=nodeDurationMonitorMapper.selectOne(wrapper);
        return result;
    }


    @Override
    public IPage<NodeDurationMonitor> getWarning(int p, int size) {
        Page<NodeDurationMonitor> page=new Page(p,size);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("status","warning");
        wrapper.orderByDesc("agg_time");
        wrapper.select("id","node_duration","agg_time","node_name","transaction_type");
        IPage<NodeDurationMonitor> iPage=nodeDurationMonitorMapper.selectPage(page,wrapper);
        //List<NodeDurationMonitor> result=nodeDurationMonitorMapper.selectList(wrapper);
        return iPage;
    }

    @Override
    public int updateWarning(int id,String status) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("id",id);
        NodeDurationMonitor result=nodeDurationMonitorMapper.selectOne(wrapper);
        UpdateWrapper updateWrapper=new UpdateWrapper();
        updateWrapper.set("status",status);
        updateWrapper.eq("id",id);
        return nodeDurationMonitorMapper.update(result,updateWrapper);
    }


}
