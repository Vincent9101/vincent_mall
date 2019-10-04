package com.vincent.mall.controller.backend;

import com.vincent.mall.common.ServerResponse;
import com.vincent.mall.constants.AppConstants;
import com.vincent.mall.pojo.User;
import com.vincent.mall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author: Vincent
 * @created: 2019/10/4  15:38
 * @description:后台用户管理接口
 */
@Controller
@RequestMapping("/manage/user/")
public class UserManageController {

    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        ServerResponse serverResponse = iUserService.login(username, password);
        if (serverResponse.isSuccessful()) {
            User user = (User) serverResponse.getData();
            if (user.getRole().intValue() == AppConstants.Role.ROLE_ADMIN) {
                session.setAttribute(AppConstants.CURRENT_USER, serverResponse.getData());
                return serverResponse;
            } else {
                return ServerResponse.buildUnSuccessfulMsgResponse("不是管理员,无法登陆");
            }
        }
        return serverResponse;
    }

}
