package com.vincent.mall.dao;

import com.vincent.mall.pojo.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);


    Cart selectCartByUserIdAndProductId(@Param("user_id") Integer userId,
                                        @Param("product_id") Integer productId);

    List<Cart> selectCartByUserId(@Param("user_id") Integer userId);

    int selectCartProductCheckedStatusByUserId(@Param("user_id") Integer userId);

    int deleteCartProductByUserIdAndProductIds(@Param("user_id") Integer userId,
                                               @Param("product_ids") List<String> productIdList);

    int checkedOrUnCheckedProduct(@Param("user_id") Integer userId,
                                  @Param("checked") Integer checked,
                                  @Param("product_id") Integer productId);
    int selectCountFromCart(@Param("user_id") Integer userId);
}
