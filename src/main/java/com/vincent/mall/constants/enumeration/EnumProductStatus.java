package com.vincent.mall.constants.enumeration;

import lombok.Getter;

/**
 * @author: Vincent
 * @created: 2019/10/8  22:20
 * @description:产品上架状态
 */

public enum EnumProductStatus {
    ON_SALE("在售", 1);

    @Getter
    private String value;
    @Getter
    private int code;

    EnumProductStatus(String value, int code) {
        this.value = value;
        this.code = code;
    }
}
