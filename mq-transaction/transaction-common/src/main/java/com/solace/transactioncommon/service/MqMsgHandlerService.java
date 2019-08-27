package com.solace.transactioncommon.service;

import com.solace.transactioncommon.entity.TransactionMessage;

/**
   * 作者 CG
   * 时间 2019/8/27 9:23
   * 注释 MQ消息处理器
   */
public interface MqMsgHandlerService {
     /**
      * 作者 CG
      * 时间 2019/8/27 9:25
      * 注释 创建用户初始化钱包表
      */
     void saveUserMoney(TransactionMessage msg);
}
