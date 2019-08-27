package com.solace.mqtransactionservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.solace.transactioncommon.service.QueueProduceService;
import com.solace.transactioncommon.service.TransactionMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonObjectSerializer;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.Queue;
import javax.print.attribute.standard.Destination;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Component
public class QueueProduceServiceImpl implements QueueProduceService {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private Queue queue;
    @Autowired
    private TransactionMessageService transactionMessageService;

    @Override
    public <T> void sendMessage(Collection<T> t) {
        t.forEach(x ->
                sendMessage(x)
        );
    }

    @Override
    public <T> void sendMessage(T t) {
        jmsTemplate.convertAndSend(queue, JSONObject.toJSONString(t));
    }

    // 调用一次一个信息发出
    @Override
    public void produceMessage() {
        jmsTemplate.convertAndSend(queue, "****" + UUID.randomUUID().toString().substring(0, 6));
    }

    // 带定时投递的业务方法
    @Override
    @Scheduled(fixedDelay = 60000)    // 每3秒自动调用
    public void produceMessageScheduled() {
        transactionMessageService.retry();
    }

    @Override
    @Scheduled(fixedDelay = 60000)    // 每3秒自动调用
    public void deadLetterMessageScheduled() {
        transactionMessageService.deadLetter();
    }

}
