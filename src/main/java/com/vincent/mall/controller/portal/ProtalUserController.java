package com.vincent.mall.controller.portal;

import com.vincent.mall.common.ServerResponse;
import com.vincent.mall.constants.AppConstants;
import com.vincent.mall.constants.enumeration.EnumResponseCode;
import com.vincent.mall.pojo.User;
import com.vincent.mall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author: Vincent
 * @created: 2019/10/4  11:10
 * @description:用户相关接口
 */
@Controller
@RequestMapping("/user/")
public class ProtalUserController {

    @Autowired
    private IUserService iUserService;

    /**
     * @param username
     * @param session
     * @return
     */
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        ServerResponse serverResponse = iUserService.login(username, password);
        if (serverResponse.isSuccessful()) {
            session.setAttribute(AppConstants.CURRENT_USER, serverResponse.getData());
        }
        return serverResponse;
    }

    @RequestMapping(value = "logout.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session) {

        session.removeAttribute(AppConstants.CURRENT_USER);

        return ServerResponse.buildSuccessfulResponse();
    }

    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user) {
        return iUserService.register(user);
    }

    /**
     * @param str:email or username
     * @param type
     * @return
     */
    @RequestMapping(value = "validate.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String str, String type) {
        return iUserService.checkValid(str, type);
    }

    @RequestMapping(value = "get_user_login_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserLoginInfo(HttpSession session) {
        User user = (User) session.getAttribute(AppConstants.CURRENT_USER);
        if (user == null) {
            return ServerResponse.buildUnSuccessfulMsgResponse("请登录！！！");
        }
        return ServerResponse.buildSuccessfulDataResponse(user);
    }


    @RequestMapping(value = "get_safe_question.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetQuestions(String username) {
        return iUserService.selectQuestions(username);
    }

    @RequestMapping(value = "check_safe_answer.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAns(String username, @RequestParam("question") String ques, @RequestParam("answer") String ans) {
        return iUserService.checkSafeAns(username, ques, ans);
    }


    @RequestMapping(value = "forget_reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetResetPwd(String username, @RequestParam("new_password") String newPwd, String token) {
        return iUserService.forgetResetPwd(username, newPwd, token);
    }

    @RequestMapping(value = "reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPwd(HttpSession session, @RequestParam("old_password") String oldPwd, @RequestParam("new_password") String newPwd) {
        User user = (User) session.getAttribute(AppConstants.CURRENT_USER);
        if (user == null) {
            return ServerResponse.buildUnSuccessfulMsgResponse("用户未登录！！！");
        }
        return iUserService.resetPwd(oldPwd, newPwd, user);
    }

    @RequestMapping(value = "update_user_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateUserInfo(HttpSession session, User newUser) {
        User currentUser = (User) session.getAttribute(AppConstants.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.buildUnSuccessfulMsgResponse("用户未登录！！！");
        }
        newUser.setId(currentUser.getId());
        newUser.setUsername(currentUser.getUsername());
        ServerResponse<User> response = iUserService.updateInfo(newUser);
        if (response.isSuccessful()) {
            session.setAttribute(AppConstants.CURRENT_USER, response.getData());
        }
        return response;
    }

    @RequestMapping(value = "get_user_detail_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserDetailInfo(HttpSession session) {
        User currentUser = (User) session.getAttribute(AppConstants.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.buildUnSuccessfulCodeAndMsgResponse(EnumResponseCode.NEED_LOGIN.getCode(), "需要登录，status=10");
        }
        return iUserService.getDetailInfo(currentUser.getId());
    }
}
