package com.solace.transactioncommon.service;

import com.solace.transactioncommon.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.concurrent.ExecutionException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author CG
 * @since 2019-08-23
 */
public interface UserService extends IService<User> {
    void saveUser(User user);
}
