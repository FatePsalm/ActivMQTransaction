package com.solace.user.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.solace.transactioncommon.entity.TransactionMessage;
import com.solace.transactioncommon.entity.User;
import com.solace.transactioncommon.service.TransactionMessageService;
import com.solace.transactioncommon.service.UserService;
import com.solace.transactioncommon.utils.StringUtil;
import com.solace.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author CG
 * @since 2019-08-23
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private TransactionMessageService transactionMessageService;
    @Override
    @Transactional
    public void saveUser(User user)  {
        //保存用户
        this.baseMapper.insert(user);
        //价钱 预先保存消息
        TransactionMessage transactionMessage = sealRpTransactionMessage(user);
        transactionMessageService.saveMessageWaitingConfirm(transactionMessage);
        //执行业务逻辑
        //1
        //提交
        transactionMessageService.confirmAndSendMessage(transactionMessage.getMessageId());
        System.out.println("方法执行结束");
    }
    private TransactionMessage sealRpTransactionMessage(User user){
       //封装User信息
        String messageId = StringUtil.get32UUID();
        String messageBody = JSONObject.toJSONString(user);
        TransactionMessage rpTransactionMessage = new TransactionMessage( messageId, messageBody,"sealRpTransactionMessage");
        rpTransactionMessage.setMessageDataType("saveUser");
        return rpTransactionMessage;
    }
}
