package com.vincent.mall.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: Vincent
 * @created: 2019/10/9  23:41
 * @description:购物车产品对象
 */
@Data
public class CartProductVO {
    private Integer id;
    private Integer userId;
    private Integer productId;
    private Integer quantity;//购物车中此商品的数量
    private String productName;
    private String productSubtitle;
    private String productMainImage;
    private BigDecimal productPrice;
    private Integer productStatus;
    private BigDecimal productTotalPrice;
    private Integer productStock;
    private Integer productChecked;//此商品是否勾选

    private String limitQuantity;//限制数量的一个返回结果

}
