package com.whu.bankwarningsystem_backend.nodeDurationMonitor.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.whu.bankwarningsystem_backend.nodeDurationMonitor.entity.NodeDurationMonitor;
import com.whu.bankwarningsystem_backend.util.NodeDetail;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public interface NodeDurationMonitorService extends IService<NodeDurationMonitor> {
    NodeDurationMonitor getDuration(String aggtime,String node );
    IPage<NodeDurationMonitor> getWarning(int p, int size);
    int updateWarning(int id,String status);
    int getNodeWaring(String startTime, String endTime,  String node);
    int  getNodeCount(String startTime, String endTime, String node);
    List<NodeDetail> getNodeList(String startTime, String endTime) throws ExecutionException, InterruptedException;
    int updateStatus(int id);
    NodeDurationMonitor getDurationFirst(String aggtime,String node );
}
