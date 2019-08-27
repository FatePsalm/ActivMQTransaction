package com.solace.transactioncommon.service;

import com.solace.transactioncommon.entity.Money;
import com.baomidou.mybatisplus.extension.service.IService;
import com.solace.transactioncommon.entity.TransactionMessage;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author CG
 * @since 2019-08-23
 */
public interface MoneyService extends IService<Money> {
    /**
     * 作者 CG
     * 时间 2019/8/27 9:28
     * 注释 查询用户是否添加过
     */
    boolean findUserMoney(String userId);

    /**
     * 作者 CG
     * 时间 2019/8/27 9:58
     * 注释 新增用户钱包信息
     */
    void saveUserMoney(TransactionMessage msg);
}
