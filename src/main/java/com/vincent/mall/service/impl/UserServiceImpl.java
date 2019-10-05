package com.vincent.mall.service.impl;

import com.vincent.mall.common.ServerResponse;
import com.vincent.mall.common.TokenCache;
import com.vincent.mall.constants.AppConstants;
import com.vincent.mall.dao.UserMapper;
import com.vincent.mall.pojo.User;
import com.vincent.mall.service.IUserService;
import com.vincent.mall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author: Vincent
 * @created: 2019/10/4  11:20
 * @description:implement for IUserService
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
        int resCount = userMapper.checkUserName(username);
        if (resCount == 0) {
            return ServerResponse.buildUnSuccessfulMsgResponse("用户名不存在！！！");
        }
        String md5Pwd = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectForLogin(username, md5Pwd);
        if (user == null) {
            return ServerResponse.buildUnSuccessfulMsgResponse("密码错误！！！");
        }
        user.setPassword(StringUtils.EMPTY);

        return ServerResponse.buildSuccessfulDataResponse("登陆成功", user);
    }

    @Override
    public ServerResponse<String> register(User user) {
        ServerResponse res = this.checkValid(user.getUsername(), AppConstants.USER_NAME);
        if (!res.isSuccessful()) {
            return res;
        }
        res = this.checkValid(user.getEmail(), AppConstants.EMAIL);
        if (!res.isSuccessful()) {
            return res;
        }
//        user.setRole(AppConstants.Role.ROLE_NORMAL);
        //MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int resCount = userMapper.insert(user);
        if (resCount == 0) {
            return ServerResponse.buildUnSuccessfulMsgResponse("注册失败，内部错误！！！");
        }

        return ServerResponse.buildSuccessfulMsgResponse("注册成功！！！");
    }


    @Override
    public ServerResponse<String> checkValid(String str, String type) {
        int resCount;
        if (StringUtils.isNotBlank(type)) {
            if (AppConstants.USER_NAME.equals(type)) {
                resCount = userMapper.checkUserName(str);
                if (resCount > 0) {
                    return ServerResponse.buildUnSuccessfulMsgResponse("用户名已存在！！！");
                }
            } else if (AppConstants.EMAIL.equals(type)) {
                resCount = userMapper.checkEmail(str);
                if (resCount > 0) {
                    return ServerResponse.buildUnSuccessfulMsgResponse("邮箱已存在！！！");
                }
            }
        } else {
            return ServerResponse.buildUnSuccessfulMsgResponse(" 参数错误！！");
        }
        return ServerResponse.buildSuccessfulMsgResponse("校验成功！");
    }

    @Override
    public ServerResponse selectQuestions(String username) {
        ServerResponse response = this.checkValid(username, AppConstants.USER_NAME);
        if (response.isSuccessful()) {
            return ServerResponse.buildUnSuccessfulMsgResponse("用户不存在");
        }
        String questions = userMapper.selectQuestionsByUsername(username);
        if (StringUtils.isNotBlank(questions)) {
            return ServerResponse.buildSuccessfulDataResponse(questions);
        }
        return ServerResponse.buildUnSuccessfulMsgResponse("没有设置密码问题！！！");
    }

    @Override
    public ServerResponse<String> checkSafeAns(String username, String ques, String ans) {
        int resCount = userMapper.checkAns(username, ques, ans);
        if (resCount > 0) {
            String forgetToken = UUID.randomUUID().toString();
            //放置cache
            TokenCache.put(AppConstants.TOKEN_PREFIX + username, forgetToken);
            return ServerResponse.buildSuccessfulDataResponse(forgetToken);
        }
        return ServerResponse.buildUnSuccessfulMsgResponse("答案错误！！！");
    }

    @Override
    public ServerResponse<String> forgetResetPwd(String username, String newPwd, String token) {
        if (StringUtils.isBlank(token)) {
            return ServerResponse.buildUnSuccessfulMsgResponse("参数错误，token必须传递！！！");
        }
        ServerResponse response = this.checkValid(username, AppConstants.USER_NAME);
        if (response.isSuccessful()) {
            return ServerResponse.buildUnSuccessfulMsgResponse("用户不存在");
        }

        String tokenCache = TokenCache.get(AppConstants.TOKEN_PREFIX + username);
        if (StringUtils.isBlank(tokenCache)) {
            return ServerResponse.buildUnSuccessfulMsgResponse("token 无效或者过期。");
        }
        if (StringUtils.equals(token, tokenCache)) {
            String mdPwd = MD5Util.MD5EncodeUtf8(newPwd);
            int resCount = userMapper.updatePwdByUsername(username, mdPwd);
            if (resCount > 0) {
                return ServerResponse.buildSuccessfulMsgResponse("修改密码成功。");
            }
        } else {
            return ServerResponse.buildUnSuccessfulMsgResponse("token错误,请重新获取token。");
        }
        return ServerResponse.buildUnSuccessfulMsgResponse("修改密码失败。");
    }

    @Override
    public ServerResponse<String> resetPwd(String oldPwd, String newPwd, User user) {
        //防止横向越权，校验用户旧密码，确定是该用户
        int resCount = userMapper.checkPwd(MD5Util.MD5EncodeUtf8(oldPwd), user.getId());
        if (resCount == 0) {
            return ServerResponse.buildUnSuccessfulMsgResponse("旧密码错误！");
        }
        int updateCount = userMapper.updatePwdByUsername(user.getUsername(), MD5Util.MD5EncodeUtf8(newPwd));
        if (updateCount > 0) {
            return ServerResponse.buildSuccessfulMsgResponse("修改密码成功。");
        }
        return ServerResponse.buildUnSuccessfulMsgResponse("修改密码失败。");
    }

    @Override
    public ServerResponse<User> updateInfo(User user) {
        //username 为主键不可更新
        //email 校验与当前email是否已被使用
        int resCount = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
        if (resCount > 0) {
            return ServerResponse.buildUnSuccessfulMsgResponse("email已被使用！");
        }

        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());
        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if (updateCount > 0) {
            return ServerResponse.buildSuccessfulMsgResponse("更新个人信息成功。");
        }
        return ServerResponse.buildUnSuccessfulMsgResponse("更新个人信息失败。");
    }

    @Override
    public ServerResponse<User> getDetailInfo(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            return ServerResponse.buildUnSuccessfulMsgResponse("找不到当前用户。");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.buildSuccessfulDataResponse(user);
    }

    @Override
    public ServerResponse checkAdmin(User user) {
        if (user != null && user.getRole().intValue() == AppConstants.Role.ROLE_ADMIN) {
            return ServerResponse.buildSuccessfulResponse();
        }
        return ServerResponse.buildUnSuccessfulMsgResponse("该用户不是管理员！");
    }

}
