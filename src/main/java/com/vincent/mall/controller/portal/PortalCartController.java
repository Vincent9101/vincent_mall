package com.vincent.mall.controller.portal;

import com.vincent.mall.common.ServerResponse;
import com.vincent.mall.constants.AppConstants;
import com.vincent.mall.pojo.User;
import com.vincent.mall.service.ICartService;
import com.vincent.mall.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author: Vincent
 * @created: 2019/10/9  22:42
 * @description:购物车模块
 */
@RequestMapping(value = "/cart/")
@Controller
public class PortalCartController {
    @Autowired
    private ICartService cartService;

    @RequestMapping(value = "add.do")
    @ResponseBody
    public ServerResponse<CartVO> addProductToCart(HttpSession session,
                                                   @RequestParam Integer count,
                                                   @RequestParam(value = "product_id") Integer productId) {
        User currentUser = (User) session.getAttribute(AppConstants.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.buildUnSuccessfulMsgResponse("请登录后操作!!!");
        }
        if (productId==null||count==null){
            return ServerResponse.buildUnSuccessfulMsgResponse("参数有误!!!");

        }
        return cartService.addProductToCart(currentUser.getId(), productId, count);
    }
}
