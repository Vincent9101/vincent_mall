package com.vincent.mall.service;

import com.github.pagehelper.PageInfo;
import com.vincent.mall.common.ServerResponse;
import com.vincent.mall.pojo.Product;

/**
 * @author: Vincent
 * @created: 2019/10/5  14:54
 * @description:产品相关操作
 */
public interface IProductService {
    /**
     * 保存或者更新产品
     *
     * @param product
     * @return
     */
    ServerResponse saveOrUpdateProduct(Product product);


    /**
     * 更新产品上架状态
     *
     * @param productId
     * @param saleStatus
     * @return
     */
    ServerResponse updateSaleStatus(Integer productId, Integer saleStatus);

    /**
     * 管理员获取产品详情
     *
     * @param productId
     * @return
     */
    ServerResponse manageGetProductDetail(Integer productId);

    /**
     * 获取产品列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse getProductList(int pageNum, int pageSize);

    /**
     * @param pageNum
     * @param pageSize
     * @param productId
     * @param productName
     * @return
     * @description 搜索产品
     */
    ServerResponse searchProduct(int pageNum, int pageSize, Integer productId, String productName);


    /**
     * 前台获取产品详情
     *
     * @param productId
     * @return
     */
    ServerResponse portalProductDetail(Integer productId);

    /**
     * 根据关键字和分类
     *
     * @param keyword
     * @param categoryId
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse<PageInfo> getPortalProductByKeywordCategoryId(String keyword,
                                                                 Integer categoryId,
                                                                 String orderBy,
                                                                 int pageNum,
                                                                 int pageSize);

}
