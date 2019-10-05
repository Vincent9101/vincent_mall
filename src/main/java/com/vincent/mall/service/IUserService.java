package com.vincent.mall.service;

import com.vincent.mall.common.ServerResponse;
import com.vincent.mall.pojo.User;

/**
 * @author: Vincent
 * @created: 2019/10/4  11:19
 * @description:IUserService for business-logic
 */
public interface IUserService {
    /**
     * login function
     *
     * @param username
     * @param password
     * @return
     */
    ServerResponse<User> login(String username, String password);


    /**
     * register function
     *
     * @param user
     * @return
     */
    ServerResponse<String> register(User user);

    /**
     * @param str  : email or username to valid
     * @param type
     * @return
     */
    ServerResponse<String> checkValid(String str, String type);

    /**
     * 获取安全问题
     *
     * @param username
     * @return
     */
    ServerResponse selectQuestions(String username);

    /**
     * 验证安全问题
     *
     * @param username
     * @param ques
     * @param ans
     * @return
     */
    ServerResponse<String> checkSafeAns(String username, String ques, String ans);

    /**
     * 未登录状态 重置密码
     *
     * @param username
     * @param newPwd
     * @param token
     * @return
     */
    ServerResponse<String> forgetResetPwd(String username, String newPwd, String token);


    /**
     * 登陆状态重置密码
     *
     * @param oldPwd
     * @param newPwd
     * @param user
     * @return
     */
    ServerResponse<String> resetPwd(String oldPwd, String newPwd, User user);


    /**
     * 更新用户信息
     *
     * @param user
     * @return
     */
    ServerResponse<User> updateInfo(User user);

    /**
     * 获取用户详细信息
     *
     * @param userId
     * @return
     */
    ServerResponse<User> getDetailInfo(Integer userId);

    /**
     * 校验是否是管理员
     *
     * @param user
     * @return
     */
    ServerResponse checkAdmin(User user);



}
