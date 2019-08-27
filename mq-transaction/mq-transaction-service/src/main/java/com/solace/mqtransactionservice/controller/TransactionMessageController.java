package com.solace.mqtransactionservice.controller;


import com.solace.mqtransactionservice.service.impl.QueueProduceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author CG
 * @since 2019-08-23
 */
@RestController
@RequestMapping("/transactionMessage")
public class TransactionMessageController {
    @Autowired
    private QueueProduceServiceImpl queueProduceServiceImpl;
    @RequestMapping("senMsg")
    public void senMsg(){
        queueProduceServiceImpl.produceMessage();
    }

}
