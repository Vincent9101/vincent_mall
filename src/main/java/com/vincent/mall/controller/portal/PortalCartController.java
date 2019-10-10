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
        if (productId == null || count == null) {
            return ServerResponse.buildUnSuccessfulMsgResponse("参数有误!!!");

        }
        return cartService.addProductToCart(currentUser.getId(), productId, count);
    }

    @RequestMapping(value = "update.do")
    @ResponseBody
    public ServerResponse<CartVO> updateProductInCart(HttpSession session,
                                                      @RequestParam Integer count,
                                                      @RequestParam(value = "product_id") Integer productId) {
        User currentUser = (User) session.getAttribute(AppConstants.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.buildUnSuccessfulMsgResponse("请登录后操作!!!");
        }
        if (productId == null || count == null) {
            return ServerResponse.buildUnSuccessfulMsgResponse("参数有误!!!");

        }
        return cartService.updateProductInCart(currentUser.getId(), productId, count);
    }

    @RequestMapping(value = "delete.do")
    @ResponseBody
    public ServerResponse<CartVO> deleteProductFromCart(HttpSession session,
                                                        @RequestParam("product_id_list") String productIdList) {
        User currentUser = (User) session.getAttribute(AppConstants.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.buildUnSuccessfulMsgResponse("请登录后操作!!!");
        }
        return cartService.deleteProductFromCart(productIdList, currentUser.getId());
    }

    @RequestMapping(value = "list.do")
    @ResponseBody
    public ServerResponse<CartVO> listProductFromCart(HttpSession session) {
        User currentUser = (User) session.getAttribute(AppConstants.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.buildUnSuccessfulMsgResponse("请登录后操作!!!");
        }
        return cartService.getProductFromCart(currentUser.getId());
    }

    @RequestMapping(value = "select.do")
    @ResponseBody
    public ServerResponse<CartVO> selectProductFromCart(HttpSession session,
                                                        @RequestParam(value = "checked",
                                                                required = true,
                                                                defaultValue = "true") boolean checked,
                                                        @RequestParam(value = "product_id", required = false) Integer productId) {
        User currentUser = (User) session.getAttribute(AppConstants.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.buildUnSuccessfulMsgResponse("请登录后操作!!!");
        }
        return cartService.selectOrUnSelect(currentUser.getId(), checked ? AppConstants.CartStatus.CHECKED : AppConstants.CartStatus.UN_CHECKED, productId);
    }

    @RequestMapping(value = "get_cart_count.do")
    @ResponseBody
    public ServerResponse<Integer> getCartCount(HttpSession session) {
        User currentUser = (User) session.getAttribute(AppConstants.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.buildUnSuccessfulMsgResponse("请登录后操作!!!");
        }
        return cartService.getCartCount(currentUser.getId());
    }
}
