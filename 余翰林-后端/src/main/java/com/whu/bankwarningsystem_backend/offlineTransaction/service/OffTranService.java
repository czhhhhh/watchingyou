package com.whu.bankwarningsystem_backend.offlineTransaction.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.whu.bankwarningsystem_backend.offlineTransaction.entity.OfflineTransaction;

import java.util.List;

public interface OffTranService extends IService<OfflineTransaction> {
    List<OfflineTransaction> getAmountRange(String aggtime, String trantype);
    List<OfflineTransaction> getLocation(String aggtime, String trantype);
    List<OfflineTransaction> getAgeRange(String aggtime );
    List<OfflineTransaction> getPurpose(String aggtime );
    List<OfflineTransaction> getCurrency(String aggtime );

}
