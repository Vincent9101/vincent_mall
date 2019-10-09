package com.vincent.mall.service;

import com.vincent.mall.common.ServerResponse;
import com.vincent.mall.vo.CartVO;

/**
 * @author: Vincent
 * @created: 2019/10/9  22:55
 * @description:购物车服务
 */
public interface ICartService {

    /**
     * 向购物添加产品
     *
     * @param userId
     * @param productId
     * @param count
     * @return
     */
    public ServerResponse<CartVO> addProductToCart(Integer userId,
                                                   Integer productId,
                                                   Integer count);
}
