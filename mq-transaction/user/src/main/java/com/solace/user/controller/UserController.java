package com.solace.user.controller;


import com.solace.transactioncommon.entity.User;
import com.solace.transactioncommon.service.UserService;
import com.solace.transactioncommon.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.UUID;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author CG
 * @since 2019-08-23
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @RequestMapping("saveUser")
    public void saveUser(){
        User user = new User();
        user.setId(StringUtil.get32UUID());
        user.setAge(new Random().nextInt(100));
        user.setUserName(StringUtil.get32UUID().toString().substring(0,6));
        userService.saveUser(user);
    }
}
