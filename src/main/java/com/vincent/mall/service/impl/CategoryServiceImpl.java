package com.vincent.mall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.vincent.mall.common.ServerResponse;
import com.vincent.mall.dao.CategoryMapper;
import com.vincent.mall.pojo.Category;
import com.vincent.mall.service.ICategoryService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

/**
 * @author: Vincent
 * @created: 2019/10/4  23:19
 * @description:分类处理serviceImpl
 */
@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Override
    public ServerResponse addCategory(String categoryName, int parentId) {
        if (StringUtils.isBlank(categoryName)) {
            return ServerResponse.buildUnSuccessfulMsgResponse("分类名不能为空，参数错误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setStatus(true);
        category.setParentId(parentId);
        int resCount = categoryMapper.insert(category);
        if (resCount > 0) {
            return ServerResponse.buildSuccessfulMsgResponse("添加品类成功.");
        }
        return ServerResponse.buildUnSuccessfulMsgResponse("添加品类失败.");
    }

    @Override
    public ServerResponse updateCategory(String categoryName, Integer categoryId) {

        if (StringUtils.isBlank(categoryName) || categoryId == null) {
            return ServerResponse.buildUnSuccessfulMsgResponse("品类名或者品类ID不能为空，参数错误！");
        }
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if (rowCount > 0) {
            return ServerResponse.buildSuccessfulMsgResponse("更新品类成功.");

        }
        return ServerResponse.buildUnSuccessfulMsgResponse("更新品类失败.");
    }

    @Override
    public ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId) {
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        if (CollectionUtils.isEmpty(categoryList)) {
            logger.info("当前id不存在子类");
        }
        return ServerResponse.buildSuccessfulDataResponse(categoryList);
    }


    @Override
    public ServerResponse selectCategoryAndChildrenById(Integer categoryId) {
        Set<Category> categorySet = Sets.newHashSet();
        findChildrenCategory(categorySet, categoryId);
        List<Integer> categoryIdList = Lists.newArrayList();
        categorySet.stream()
                .forEach(category -> categoryIdList.add(category.getId()));
        return ServerResponse.buildSuccessfulDataResponse(categoryIdList);
    }

    private void findChildrenCategory(Set<Category> categorySet, Integer categoryId) {

        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category != null) {
            categorySet.add(category);
        }

        //递归查询子节点 todo:优化 递归查询的效率不高
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        for (Category categoryTemp : categoryList) {
            findChildrenCategory(categorySet, categoryTemp.getId());
        }
    }
}
