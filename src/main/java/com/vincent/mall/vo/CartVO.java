package com.vincent.mall.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: Vincent
 * @created: 2019/10/9  23:42
 * @description:购物车ValueObject
 */
@Data
public class CartVO {
    private List<CartProductVO> cartProductVOList;
    private BigDecimal cartTotalPrice;
    private Boolean allChecked;//是否已经都勾选
    private String imageHost;
}
