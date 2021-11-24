package com.whu.bankwarningsystem_backend.offlineTransaction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whu.bankwarningsystem_backend.offlineTransaction.entity.OfflineTransaction;
import com.whu.bankwarningsystem_backend.offlineTransaction.mapper.OffTranMapper;
import com.whu.bankwarningsystem_backend.offlineTransaction.service.OffTranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *@author 陈小萌
 * @date 2021/07/09
 * @update 更改了wrapper条件，未上传git
 */
@Service
public class OffTranServiceImpl extends ServiceImpl<OffTranMapper, OfflineTransaction>
        implements OffTranService {

    @Autowired
    OffTranMapper offTranMapper;


    @Override
    public List<OfflineTransaction> getAmountRange(String aggtime, String trantype) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.select("count","agg_attr");
        wrapper.eq("agg_date",aggtime);
        wrapper.eq("agg_name","amountRange");
        wrapper.eq("transaction_type",trantype);
        List<OfflineTransaction> result=offTranMapper.selectList(wrapper);
        return result;
    }

    @Override
    public List<OfflineTransaction> getLocation(String aggtime, String trantype) {

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.select("volume","agg_attr");
        wrapper.eq("agg_date",aggtime);
        wrapper.eq("agg_name","location");
        wrapper.eq("transaction_type",trantype);
        List<OfflineTransaction> result=offTranMapper.selectList(wrapper);
        return result;
    }

    @Override
    public List<OfflineTransaction> getAgeRange(String aggtime) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("transaction_type","FX001");
        wrapper.eq("agg_date",aggtime);
        wrapper.eq("agg_name","ageRange");
        wrapper.select("volume","agg_attr");
        List<OfflineTransaction> result=offTranMapper.selectList(wrapper);
        return result;
    }

    @Override
    public List<OfflineTransaction> getPurpose(String aggtime) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("transaction_type","FX001");
        wrapper.eq("agg_date",aggtime);
        wrapper.eq("agg_name","purpose");
        wrapper.select("volume","agg_attr");
        List<OfflineTransaction> result=offTranMapper.selectList(wrapper);
        return result;
    }

    @Override
    public List<OfflineTransaction> getCurrency(String aggtime) {

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("transaction_type","FX001");
        wrapper.eq("agg_date",aggtime);
        wrapper.eq("agg_name","currency");
        wrapper.select("volume","agg_attr");
        List<OfflineTransaction> result=offTranMapper.selectList(wrapper);
        return result;
    }
}
