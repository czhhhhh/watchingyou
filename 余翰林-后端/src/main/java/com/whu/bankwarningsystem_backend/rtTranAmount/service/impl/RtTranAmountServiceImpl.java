package com.whu.bankwarningsystem_backend.rtTranAmount.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whu.bankwarningsystem_backend.rtTranAmount.entity.RtTranAmount;
import com.whu.bankwarningsystem_backend.rtTranAmount.mapper.RtTranAmountMapper;
import com.whu.bankwarningsystem_backend.rtTranAmount.service.RtTranAmountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *@author 陈小萌
 * @date 2021/07/12
 * @update 更改了wrapper条件，未上传git
 */
@Service
public class RtTranAmountServiceImpl extends ServiceImpl<RtTranAmountMapper, RtTranAmount>
        implements RtTranAmountService {

    @Autowired
    RtTranAmountMapper rtTranAmountMapper;

    @Override
    public RtTranAmount getTotalCount(String aggtime, String trantype) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("agg_time",aggtime);
        wrapper.eq("transaction_type",trantype);
        wrapper.select("count");
        List<RtTranAmount> res=rtTranAmountMapper.selectList(wrapper);
        int attr=0;
        for(RtTranAmount rt:res){
            attr+=rt.getCount();
        }
        RtTranAmount result=new RtTranAmount();
        result.setCount(attr);
        System.out.println(result);
        return result;
    }

    @Override
    public RtTranAmount getTotalVolume(String aggtime, String trantype) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("agg_time",aggtime);
        wrapper.eq("transaction_type",trantype);
        wrapper.select("volume");
        List<RtTranAmount> res=rtTranAmountMapper.selectList(wrapper);
        int attr=0;
        for(RtTranAmount rt:res){
            attr+=rt.getVolume();
        }
        RtTranAmount result=new RtTranAmount();
        result.setVolume(attr);
        System.out.println(result);
        return result;
    }

}
