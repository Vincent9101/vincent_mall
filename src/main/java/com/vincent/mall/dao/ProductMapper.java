package com.vincent.mall.dao;

import com.vincent.mall.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> selectProductList();

    List<Product> selectProductListByNameAndId(@Param("product_id") Integer productId,
                                               @Param("product_name") String productName);

    List<Product> selectProductListByNameAndCategoryIds(@Param("category_ids") List<Integer> categoryIds,
                                                        @Param("product_name") String productName);

}
