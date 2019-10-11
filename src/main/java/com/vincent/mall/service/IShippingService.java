package com.vincent.mall.service;

import com.vincent.mall.common.ServerResponse;
import com.vincent.mall.pojo.Shipping;

/**
 * @author: Vincent
 * @created: 2019/10/11  21:12
 * @description:收货地址模块
 */
public interface IShippingService {
    /**
     * 添加收货地址
     *
     * @param userId
     * @param shipping
     * @return
     */
    ServerResponse add(Integer userId, Shipping shipping);

    /**
     * 删除地址
     *
     * @param userId
     * @param shippingId
     * @return
     */
    ServerResponse delete(Integer userId, Integer shippingId);

    /**
     * 更新地址
     *
     * @param userId
     * @param shipping
     * @return
     */
    ServerResponse update(Integer userId, Shipping shipping);

    /**
     * 查询地址
     *
     * @param userId
     * @param shippingId
     * @return
     */
    public ServerResponse select(Integer userId, Integer shippingId);


    /**
     * 分页查询
     * @param userId
     * @param pageSize
     * @param pageNum
     * @return
     */
    ServerResponse list(Integer userId, int pageSize, int pageNum);
}
