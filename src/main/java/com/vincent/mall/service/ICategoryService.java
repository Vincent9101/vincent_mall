package com.vincent.mall.service;

import com.vincent.mall.common.ServerResponse;
import com.vincent.mall.pojo.Category;

import java.util.List;

/**
 * @author: Vincent
 * @created: 2019/10/4  23:19
 * @description:分类处理Service
 */
public interface ICategoryService {
    /**
     * 添加分类
     *
     * @param categoryName 类别名
     * @param parentId     父类ID
     * @return
     */
    ServerResponse addCategory(String categoryName, int parentId);

    /**
     * 更新分类
     *
     * @param categoryName
     * @param categoryId
     * @return
     */
    ServerResponse updateCategory(String categoryName, Integer categoryId);


    /**
     * 获取平级子节点类
     *
     * @param categoryId
     * @return
     */
    ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId);


    /**
     * 根据品类ID递归查询品类与所有子品类
     *
     * @param categoryId
     * @return
     */
    ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId);
}


