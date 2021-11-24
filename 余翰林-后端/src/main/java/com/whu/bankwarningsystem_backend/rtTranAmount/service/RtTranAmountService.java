package com.whu.bankwarningsystem_backend.rtTranAmount.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.whu.bankwarningsystem_backend.rtTranAmount.entity.RtTranAmount;

public interface RtTranAmountService extends IService<RtTranAmount> {
    RtTranAmount getTotalCount(String aggtime, String trantype);
    RtTranAmount getTotalVolume(String aggtime, String trantype);
}

