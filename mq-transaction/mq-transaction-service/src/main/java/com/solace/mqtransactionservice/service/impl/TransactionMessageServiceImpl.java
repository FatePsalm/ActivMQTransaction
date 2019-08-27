package com.solace.mqtransactionservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.solace.mqtransactionservice.mapper.TransactionMessageMapper;
import com.solace.transactioncommon.entity.TransactionMessage;
import com.solace.transactioncommon.enums.MessageStatusEnum;
import com.solace.transactioncommon.enums.PublicEnum;
import com.solace.transactioncommon.enums.TStatusConfigEnum;
import com.solace.transactioncommon.exceptions.MessageBizException;
import com.solace.transactioncommon.service.QueueProduceService;
import com.solace.transactioncommon.service.TransactionMessageService;
import com.solace.transactioncommon.utils.PublicConfigUtil;
import com.solace.transactioncommon.utils.StringUtil;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Component
@Service
public class TransactionMessageServiceImpl extends ServiceImpl<TransactionMessageMapper, TransactionMessage>  implements TransactionMessageService {
    @Autowired
    private QueueProduceService queueProduceService;


    public int saveMessageWaitingConfirm(TransactionMessage message) {

        if (message == null) {
            throw new MessageBizException(MessageBizException.SAVA_MESSAGE_IS_NULL, "保存的消息为空");
        }

        if (StringUtil.isEmpty(message.getConsumerQueue())) {
            throw new MessageBizException(MessageBizException.MESSAGE_CONSUMER_QUEUE_IS_NULL, "消息的消费队列不能为空 ");
        }
        message.setEditTime(new Date());
        message.setStatus(MessageStatusEnum.WAITING_CONFIRM.name());
        message.setAreadlyDead(PublicEnum.NO.name());
        message.setMessageSendTimes(0);
        return this.baseMapper.insert(message);
    }


    public void confirmAndSendMessage(String messageId) {
        final TransactionMessage message = getMessageByMessageId(messageId);
        if (message == null) {
            throw new MessageBizException(MessageBizException.SAVA_MESSAGE_IS_NULL, "根据消息id查找的消息为空");
        }
        message.setStatus(MessageStatusEnum.SENDING.name());
        message.setEditTime(new Date());
        this.baseMapper.updateById(message);
        queueProduceService.sendMessage(message);
    }


    public int saveAndSendMessage(final TransactionMessage message) {

        if (message == null) {
            throw new MessageBizException(MessageBizException.SAVA_MESSAGE_IS_NULL, "保存的消息为空");
        }
        if (StringUtil.isEmpty(message.getConsumerQueue())) {
            throw new MessageBizException(MessageBizException.MESSAGE_CONSUMER_QUEUE_IS_NULL, "消息的消费队列不能为空 ");
        }
        message.setStatus(MessageStatusEnum.SENDING.name());
        message.setAreadlyDead(PublicEnum.NO.name());
        message.setMessageSendTimes(0);
        message.setEditTime(new Date());
        int result = this.baseMapper.insert(message);
        queueProduceService.sendMessage(message);
        return result;
    }

    @Override
    public void saveAndSendMessage(List<TransactionMessage> transactionMessages) throws MessageBizException {
        for (TransactionMessage message:transactionMessages
             ) {
            message.setStatus(MessageStatusEnum.SENDING.name());
            message.setAreadlyDead(PublicEnum.NO.name());
            message.setMessageSendTimes(message.getMessageSendTimes()+1);
            message.setEditTime(new Date());
        }
        saveOrUpdateBatch(transactionMessages);
        queueProduceService.sendMessage(transactionMessages);

    }

    public void directSendMessage(final TransactionMessage message) {

        if (message == null) {
            throw new MessageBizException(MessageBizException.SAVA_MESSAGE_IS_NULL, "保存的消息为空");
        }
        if (StringUtil.isEmpty(message.getConsumerQueue())) {
            throw new MessageBizException(MessageBizException.MESSAGE_CONSUMER_QUEUE_IS_NULL, "消息的消费队列不能为空 ");
        }
        queueProduceService.sendMessage(message);
    }
    public void reSendMessage(final TransactionMessage message) {

        if (message == null) {
            throw new MessageBizException(MessageBizException.SAVA_MESSAGE_IS_NULL, "保存的消息为空");
        }

        if (StringUtil.isEmpty(message.getConsumerQueue())) {
            throw new MessageBizException(MessageBizException.MESSAGE_CONSUMER_QUEUE_IS_NULL, "消息的消费队列不能为空 ");
        }
        message.addSendTimes();
        message.setEditTime(new Date());
        this.baseMapper.updateById(message);

        queueProduceService.sendMessage(message);
    }
    public void reSendMessageByMessageId(String messageId) {
        final TransactionMessage message = getMessageByMessageId(messageId);
        if (message == null) {
            throw new MessageBizException(MessageBizException.SAVA_MESSAGE_IS_NULL, "根据消息id查找的消息为空");
        }

        int maxTimes = Integer.valueOf(PublicConfigUtil.readConfig("message.max.send.times"));
        if (message.getMessageSendTimes() >= maxTimes) {
            message.setAreadlyDead(PublicEnum.YES.name());
        }

        message.setEditTime(new Date());
        message.setMessageSendTimes(message.getMessageSendTimes() + 1);
        this.baseMapper.updateById(message);

        queueProduceService.sendMessage(message);
    }

    public void setMessageToAreadlyDead(String messageId) {
        TransactionMessage message = getMessageByMessageId(messageId);
        if (message == null) {
            throw new MessageBizException(MessageBizException.SAVA_MESSAGE_IS_NULL, "根据消息id查找的消息为空");
        }
        message.setAreadlyDead(PublicEnum.YES.name());
        message.setEditTime(new Date());
        this.baseMapper.updateById(message);
    }

    public TransactionMessage getMessageByMessageId(String messageId) {
        QueryWrapper<TransactionMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(TransactionMessage::getMessageId,messageId);
        TransactionMessage one = getOne(queryWrapper);
        return one;
    }

    public void deleteMessageByMessageId(String messageId) {
        int i = 1 / 0;
        QueryWrapper<TransactionMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(TransactionMessage::getMessageId,messageId);
        remove(queryWrapper);
    }

    @Override
    @Transactional
    public void retry() throws MessageBizException {
        System.out.println("开始执行重试机制!");
        /**
         * 查询状态还在发送中的
         * 没有进入死信队列的
         * 重试次数在规定次数内的
         * 最后修改时间超过规定时间的
         */
        QueryWrapper<TransactionMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(TransactionMessage::getStatus, MessageStatusEnum.SENDING.name())
                .eq(TransactionMessage::getAreadlyDead, PublicEnum.NO.name())
                .lt(TransactionMessage::getMessageSendTimes, TStatusConfigEnum.Timer.RETRY.getValue())
        .le(TransactionMessage::getEditTime, LocalDateTime.now().minus(  TStatusConfigEnum.Timer.TIME.getValue(), ChronoUnit.MINUTES ));
        List<TransactionMessage> list = list(queryWrapper);
        if (list!=null&&list.size()!=0){
            //自加一次重试
            //修改最后时间
            //保存并发送
            saveAndSendMessage(list);
        }
    }

    @Override
    @Transactional
    public void deadLetter() throws MessageBizException {
        System.out.println("开始清理死亡消息");
        /**
         * 重试次数超过规定次数
         * 最后次时间超过规定时间的
         * 设置为死信队列
         */
        QueryWrapper<TransactionMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(TransactionMessage::getStatus, MessageStatusEnum.SENDING.name())
                .eq(TransactionMessage::getAreadlyDead, PublicEnum.NO.name())
                .ge(TransactionMessage::getMessageSendTimes, TStatusConfigEnum.Timer.RETRY.getValue())
                .le(TransactionMessage::getEditTime, LocalDateTime.now().minus(  TStatusConfigEnum.Timer.TIME.getValue(), ChronoUnit.MINUTES ));
        List<TransactionMessage> list = list(queryWrapper);
        if (list!=null&&list.size()!=0){
           //设置为死信队列
            list.forEach(x->setMessageToAreadlyDead(x.getMessageId()));
        }
    }
}
