package com.vincent.mall.controller.portal;

import com.vincent.mall.common.ServerResponse;
import com.vincent.mall.constants.AppConstants;
import com.vincent.mall.pojo.Shipping;
import com.vincent.mall.pojo.User;
import com.vincent.mall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author: Vincent
 * @created: 2019/10/11  21:09
 * @description:收货地址模块
 */
@RequestMapping("/shipping/")
@Controller
public class PortalShippingController {

    @Autowired
    private IShippingService iShippingService;

    @RequestMapping(value = "add_shipping.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse add(HttpSession session, Shipping shipping) {
        User user = (User) session.getAttribute(AppConstants.CURRENT_USER);
        if (user == null) {
            return ServerResponse.buildUnSuccessfulMsgResponse("请登录！！！");
        }
        return iShippingService.add(user.getId(), shipping);
    }

    @RequestMapping(value = "delete_shipping.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse delete(HttpSession session, @RequestParam("shipping_id") Integer shippingId) {
        User user = (User) session.getAttribute(AppConstants.CURRENT_USER);
        if (user == null) {
            return ServerResponse.buildUnSuccessfulMsgResponse("请登录！！！");
        }
        return iShippingService.delete(user.getId(), shippingId);
    }

    @ResponseBody
        @RequestMapping(value = "update_shipping.do", method = RequestMethod.POST)
    public ServerResponse update(HttpSession session, @RequestParam("shipping_id") Integer shippingId) {
        User user = (User) session.getAttribute(AppConstants.CURRENT_USER);
        if (user == null) {
            return ServerResponse.buildUnSuccessfulMsgResponse("请登录！！！");
        }
        return iShippingService.delete(user.getId(), shippingId);
    }

    @RequestMapping(value = "select_shipping.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse select(HttpSession session, @RequestParam("shipping_id") Integer shippingId) {
        User user = (User) session.getAttribute(AppConstants.CURRENT_USER);
        if (user == null) {
            return ServerResponse.buildUnSuccessfulMsgResponse("请登录！！！");
        }
        return iShippingService.select(user.getId(), shippingId);
    }

    @RequestMapping(value = "list_shipping.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse list(HttpSession session,
                               @RequestParam(defaultValue = "1") int pageNum,
                               @RequestParam(defaultValue = "10") int pageSize) {
        User user = (User) session.getAttribute(AppConstants.CURRENT_USER);
        if (user == null) {
            return ServerResponse.buildUnSuccessfulMsgResponse("请登录！！！");
        }
        return iShippingService.list(user.getId(),pageSize,pageNum);
    }
    }
