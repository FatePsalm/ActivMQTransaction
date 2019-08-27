package com.solace.mqtransactionservice;

import com.solace.mqtransactionservice.service.impl.QueueProduceServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MqTransactionServiceApplicationTests {
    @Resource
    private QueueProduceServiceImpl queueProduceServiceImpl;

    @Test
    public void contextLoads() {
        queueProduceServiceImpl.produceMessage();
    }

}
