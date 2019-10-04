package com.vincent.mall.constants.enumeration;

/**
 * @author: Vincent
 * @created: 2019/10/4  11:46
 * @description:返回code枚举类
 */
public enum EnumResponseCode {
    SUCCESS(0, "SUCCESS"),
    ERROR(1, "ERROR"),
    ILLEGAL_ARGUMENT(2, "ILLEGAL_ARGUMENT"),
    NEED_LOGIN(10, "NEED_LOGIN");
    private final int code;
    private final String desc;

    EnumResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
