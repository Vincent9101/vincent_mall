package com.vincent.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.vincent.mall.common.ServerResponse;
import com.vincent.mall.dao.ShippingMapper;
import com.vincent.mall.pojo.Shipping;
import com.vincent.mall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author: Vincent
 * @created: 2019/10/11  21:12
 * @description:IShippingService实现
 */
@Service
public class ShippingService implements IShippingService {
    @Autowired
    private ShippingMapper shippingMapper;

    @Override
    public ServerResponse<Map> add(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int rows = shippingMapper.insert(shipping);
        if (rows > 0) {
            Map<String, Object> res = Maps.newHashMap();
            res.put("shpping_id", shipping.getId());
            return ServerResponse.buildSuccessfulDataResponse("新建地址成功", res);
        }
        return ServerResponse.buildUnSuccessfulMsgResponse("新建地址失败");
    }

    @Override
    public ServerResponse delete(Integer userId, Integer shippingId) {
        int rows = shippingMapper.deleteByShippingIdAndUserId(shippingId, userId);
        if (rows > 0) {
            return ServerResponse.buildSuccessfulDataResponse("删除地址成功");
        }
        return ServerResponse.buildUnSuccessfulMsgResponse("删除地址失败");
    }

    @Override
    public ServerResponse update(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int rows = shippingMapper.updateByShipping(shipping);
        if (rows > 0) {
            return ServerResponse.buildSuccessfulDataResponse("更新地址成功");
        }
        return ServerResponse.buildUnSuccessfulMsgResponse("更新地址失败");
    }

    @Override
    public ServerResponse select(Integer userId, Integer shippingId) {
        Shipping shipping = shippingMapper.selectByShippingIdAndUserId(shippingId, userId);
        if (shipping != null) {
            return ServerResponse.buildSuccessfulDataResponse("查询成功", shippingId);
        }
        return ServerResponse.buildUnSuccessfulMsgResponse("无法查询到该地址");
    }

    @Override
    public ServerResponse list(Integer userId, int pageSize, int pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        List<Shipping> shippings = shippingMapper.selectByUserId(userId);
        PageInfo pageInfo = new PageInfo(shippings);
        return ServerResponse.buildSuccessfulDataResponse(pageInfo);
    }
}
