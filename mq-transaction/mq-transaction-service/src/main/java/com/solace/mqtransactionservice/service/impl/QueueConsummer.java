package com.solace.mqtransactionservice.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.solace.transactioncommon.entity.Money;
import com.solace.transactioncommon.entity.TransactionMessage;
import com.solace.transactioncommon.entity.User;
import com.solace.transactioncommon.service.MoneyService;
import com.solace.transactioncommon.service.MqMsgHandlerService;
import com.solace.transactioncommon.service.TransactionMessageService;
import com.solace.transactioncommon.service.UserService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.TextMessage;
import java.util.UUID;

/**
   * 作者 CG
   * 时间 2019/8/23 15:56
   * 注释 MQ监听器
   */
@Component
public class QueueConsummer {
    @Autowired
    private MqMsgHandlerService mqMsgHandlerService;
    @JmsListener(destination = "${myqueue}")     // 注解监听
    public void receive(TextMessage textMessage) throws  Exception{
        if (null==textMessage){
            System.out.println("接收消息为NULL");
        }
        TransactionMessage message = JSONObject.parseObject(textMessage.getText(), TransactionMessage.class);
        String type = message.getMessageDataType();
        if (type.equals("saveUser")){
            mqMsgHandlerService.saveUserMoney(message);
        }

    }

}
