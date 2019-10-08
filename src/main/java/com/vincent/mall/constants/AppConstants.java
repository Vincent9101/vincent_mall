package com.vincent.mall.constants;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * @author: Vincent
 * @created: 2019/10/4  12:52
 * @description:App常量
 */
public class AppConstants {
    /**
     * 当前用户
     */
    public static final String CURRENT_USER = "CURRENT_USER";

    public static final String EMAIL = "EMAIL";

    public static final String USER_NAME = "USER_NAME";

    public static final String TOKEN_PREFIX = "token_";

    public interface ProductListOrderBy {
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_asc", "price_desc");
    }

    public interface Role {
        /**
         * 普通用户
         */
        int ROLE_NORMAL = 0;
        /**
         * 管理员
         */
        int ROLE_ADMIN = 1;
    }


}
