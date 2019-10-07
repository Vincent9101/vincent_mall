package com.vincent.mall.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: Vincent
 * @created: 2019/10/6  23:15
 * @description:产品 View Object OR Value Object
 */
@Data
public class ProductDetailVO {
    private Integer id;
    private Integer categoryId;
    private Integer parentCategoryId;
    private String name;
    private String subtitle;
    private String mainImg;
    private String subImgs;
    private String detail;
    private BigDecimal price;
    private Integer stock;
    private Integer status;
    private String created;
    private String updated;
    /**
     * 图片服务器前缀地址 todo:配置
     */
    private String imgHost;

}
