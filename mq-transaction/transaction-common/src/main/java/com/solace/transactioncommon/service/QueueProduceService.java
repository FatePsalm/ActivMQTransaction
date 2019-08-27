package com.solace.transactioncommon.service;

import java.util.Collection;
import java.util.List;

public interface QueueProduceService {
    /**
     * 作者 CG
     * 时间 2019/8/27 10:43
     * 注释 发送批量信息
     */
    <T> void sendMessage(Collection<T> t);

    /**
     * 作者 CG
     * 时间 2019/8/27 10:41
     * 注释 发送MQ消息
     */
    <T> void sendMessage(T t);

    //生产并发送
    void produceMessage();

    /**
     * 作者 CG
     * 时间 2019/8/27 10:57
     * 注释 定时重投
     */
    void produceMessageScheduled();

    /**
     * 作者 CG
     * 时间 2019/8/27 10:57
     * 注释 定时设置死亡队列
     */
    void deadLetterMessageScheduled();
}
