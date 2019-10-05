package com.vincent.mall.dao;

import com.vincent.mall.pojo.Category;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);

    /**
     * 获取子品类
     * @param parentId
     * @return
     */
    List<Category> selectCategoryChildrenByParentId(@Param("parent_id") Integer parentId);
}
