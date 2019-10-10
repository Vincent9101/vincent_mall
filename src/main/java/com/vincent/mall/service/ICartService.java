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

    /**
     * 向购物车更新产品
     *
     * @param userId
     * @param productId
     * @param count
     * @return
     */
    public ServerResponse<CartVO> updateProductInCart(Integer userId,
                                                      Integer productId,
                                                      Integer count);

    /**
     * 删除购物车产品
     */
    ServerResponse<CartVO> deleteProductFromCart(String productIdlist, Integer userId);

    /**
     * 获取购物车产品状态
     *
     * @param userId
     * @return
     */
    public ServerResponse<CartVO> getProductFromCart(Integer userId);

    /**
     * <P>选择或者取消
     * 如果productId为空则使购物车中所有产品根据checked的值 全选或者不选，
     * 否则只选中或者取消该productId的值
     * </P>
     *
     * @param userId
     * @param productId
     * @return
     */
    ServerResponse<CartVO> selectOrUnSelect(Integer userId, Integer checked, Integer productId);

    /**
     * 获取购物车中产品总数量
     *
     * @param userId
     * @return
     */
    ServerResponse<Integer> getCartCount(Integer userId);
}
