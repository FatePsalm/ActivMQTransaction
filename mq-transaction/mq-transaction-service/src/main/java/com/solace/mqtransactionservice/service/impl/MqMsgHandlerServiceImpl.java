package com.solace.mqtransactionservice.service.impl;

import com.solace.transactioncommon.entity.TransactionMessage;
import com.solace.transactioncommon.service.MoneyService;
import com.solace.transactioncommon.service.MqMsgHandlerService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Component;

@Component
public class MqMsgHandlerServiceImpl implements MqMsgHandlerService {
    @Reference
    private MoneyService moneyService;
    @Override
    public void saveUserMoney(TransactionMessage msg) {
        moneyService.saveUserMoney(msg);
    }
}
