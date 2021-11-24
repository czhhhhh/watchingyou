package com.whu.bankwarningsystem_backend.realtimeTransaction.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.whu.bankwarningsystem_backend.realtimeTransaction.entity.RealtimeTransaction;

import java.util.List;

public interface RtTranService extends IService<RealtimeTransaction> {
    List<RealtimeTransaction> getLocation(String aggtime, String trantype);
    List<RealtimeTransaction> getAgeRange(String aggtime);
    List<RealtimeTransaction> getPurpose(String aggtime);
    List<RealtimeTransaction> getCurrency(String aggtime);
}
