package com.whu.bankwarningsystem_backend.realtimeTransaction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whu.bankwarningsystem_backend.realtimeTransaction.entity.RealtimeTransaction;
import com.whu.bankwarningsystem_backend.realtimeTransaction.mapper.RtTranMapper;
import com.whu.bankwarningsystem_backend.realtimeTransaction.service.RtTranService;
import com.whu.bankwarningsystem_backend.rtTranAmount.entity.RtTranAmount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *@author 陈小萌
 * @date 2021/07/09
 * @update 更改了wrapper条件，未上传git
 */
@Service
public class RtTranServiceImpl extends ServiceImpl<RtTranMapper, RealtimeTransaction>
        implements RtTranService {

    @Autowired
    RtTranMapper rtTranMapper;


    @Override
    public List<RealtimeTransaction> getLocation(String aggtime, String trantype) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.select("IFNULL(sum(volume),0) as totalVol","agg_attr");
        wrapper.eq("agg_time",aggtime);
        wrapper.eq("agg_name","location");
        wrapper.eq("transaction_type",trantype);
        wrapper.groupBy("agg_attr");
        wrapper.having("count(*)>0");

        List<RealtimeTransaction> result=rtTranMapper.selectList(wrapper);
        System.out.println(result);
        return result;
    }

    @Override
    public List<RealtimeTransaction> getAgeRange(String aggtime) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("agg_time",aggtime);
        wrapper.eq("agg_name","ageRange");
        wrapper.select("IFNULL(sum(volume),0) as totalVol","agg_attr");
        wrapper.groupBy("agg_attr");
        wrapper.having("count(*)>0");
        List<RealtimeTransaction> result=rtTranMapper.selectList(wrapper);
        System.out.println(result);
        return result;
    }

    @Override
    public List<RealtimeTransaction> getPurpose(String aggtime) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("agg_time",aggtime);
        wrapper.eq("agg_name","purpose");
        wrapper.select("IFNULL(sum(volume),0) as totalVol","agg_attr");
        wrapper.groupBy("agg_attr");
        wrapper.having("count(*)>0");
        List<RealtimeTransaction> result=rtTranMapper.selectList(wrapper);
        System.out.println(result);
        return result;
    }

    @Override
    public List<RealtimeTransaction> getCurrency(String aggtime) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("agg_time",aggtime);
        wrapper.eq("agg_name","currency");
        wrapper.select("IFNULL(sum(volume),0) as totalVol","agg_attr");
        wrapper.groupBy("agg_attr");
        wrapper.having("count(*)>0");
        List<RealtimeTransaction> result=rtTranMapper.selectList(wrapper);
        System.out.println(result);
        return result;
    }
}
