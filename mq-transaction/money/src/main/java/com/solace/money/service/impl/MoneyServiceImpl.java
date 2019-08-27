package com.solace.money.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.solace.money.mapper.MoneyMapper;
import com.solace.transactioncommon.entity.Money;
import com.solace.transactioncommon.entity.TransactionMessage;
import com.solace.transactioncommon.entity.User;
import com.solace.transactioncommon.service.MoneyService;
import com.solace.transactioncommon.service.TransactionMessageService;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author CG
 * @since 2019-08-23
 */
@Component
@Service
public class MoneyServiceImpl extends ServiceImpl<MoneyMapper, Money> implements MoneyService {
    @Reference
    private TransactionMessageService transactionMessageService;
    @Override
    public boolean findUserMoney(String userId) {
        QueryWrapper<Money> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Money::getUserId, userId);
        Money one = getOne(queryWrapper);
        return null==one?false:true;
    }

    @Override
    @Transactional
    public void saveUserMoney(TransactionMessage msg) {
        String messageBody = msg.getMessageBody();
        User user = JSONObject.parseObject(messageBody, User.class);
        String messageId = msg.getMessageId();
        //查询是否添加过
        boolean userMoney = findUserMoney(user.getId());
        if (userMoney)
            return;
        //处理添加请求
        Money money = new Money();
        money.setId(UUID.randomUUID().toString());
        money.setMyMoney(0);
        money.setUserId(user.getId());
        saveOrUpdate(money);
        //删除分布式事务消息
        transactionMessageService.deleteMessageByMessageId(messageId);
    }
}
