package com.vincent.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.vincent.mall.common.ServerResponse;
import com.vincent.mall.constants.AppConstants;
import com.vincent.mall.constants.enumeration.EnumProductStatus;
import com.vincent.mall.dao.CategoryMapper;
import com.vincent.mall.dao.ProductMapper;
import com.vincent.mall.pojo.Category;
import com.vincent.mall.pojo.Product;
import com.vincent.mall.service.ICategoryService;
import com.vincent.mall.service.IProductService;
import com.vincent.mall.util.DateTimeUtil;
import com.vincent.mall.util.PropertiesUtil;
import com.vincent.mall.vo.ProductDetailVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: Vincent
 * @created: 2019/10/5  14:54
 * @description:产品相关接口实现
 */
@Service
public class ProductServiceImpl implements IProductService {
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ICategoryService iCategoryService;


    @Override
    public ServerResponse saveOrUpdateProduct(Product product) {
        if (product == null) {
            return ServerResponse.buildUnSuccessfulMsgResponse("产品为空，参数不正确！！！");
        }

        if (StringUtils.isNotBlank(product.getSubImages())) {
            String[] subImgArr = product.getSubImages().split(",");
            if (subImgArr.length > 0) {
                product.setMainImage(subImgArr[0]);
            }
        }

        int rowCount = 0;
        if (product.getId() != null) {
            rowCount = productMapper.updateByPrimaryKey(product);
            return rowCount > 0 ? ServerResponse.buildSuccessfulMsgResponse("更新成功")
                    : ServerResponse.buildUnSuccessfulMsgResponse("更新失败");
        } else {
            rowCount = productMapper.insert(product);
            return rowCount > 0 ? ServerResponse.buildSuccessfulDataResponse("保存成功", product.getId())
                    : ServerResponse.buildUnSuccessfulMsgResponse("保存失败");
        }

    }

    @Override
    public ServerResponse updateSaleStatus(Integer productId, Integer saleStatus) {
        if (productId == null || saleStatus == null) {
            return ServerResponse.buildUnSuccessfulMsgResponse("产品ID或者售卖状态为空，参数不正确！！！");
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(saleStatus);
        int rowCount = productMapper.updateByPrimaryKeySelective(product);
        return rowCount > 0 ? ServerResponse.buildSuccessfulMsgResponse("更新状态成功") :
                ServerResponse.buildUnSuccessfulMsgResponse("更新状态失败");
    }

    @Override
    public ServerResponse<ProductDetailVO> manageGetProductDetail(Integer productId) {
        if (productId == null) {
            return ServerResponse.buildUnSuccessfulMsgResponse("产品ID为空，参数不正确！！！");
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.buildUnSuccessfulMsgResponse("产品不存在！！！");

        }
        return ServerResponse.buildSuccessfulDataResponse(assembleProductDetailVO(product));
    }

    /**
     * 转换为productDetailVO
     *
     * @param product
     * @return
     */
    private ProductDetailVO assembleProductDetailVO(Product product) {
        ProductDetailVO productDetailVO = new ProductDetailVO();
        productDetailVO.setId(product.getId());
        productDetailVO.setSubtitle(product.getSubtitle());
        productDetailVO.setPrice(product.getPrice());
        productDetailVO.setMainImg(product.getMainImage());
        productDetailVO.setSubImgs(product.getSubImages());
        productDetailVO.setCategoryId(product.getCategoryId());
        productDetailVO.setDetail(product.getDetail());
        productDetailVO.setName(product.getName());
        productDetailVO.setStatus(product.getStatus());
        productDetailVO.setStock(product.getStock());
        productDetailVO.setImgHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));

        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category == null) {
            productDetailVO.setParentCategoryId(0);//默认根节点
        } else {
            productDetailVO.setParentCategoryId(category.getParentId());
        }

        productDetailVO.setCreated(DateTimeUtil.dateToStr(product.getCreateTime()));
        productDetailVO.setUpdated(DateTimeUtil.dateToStr(product.getUpdateTime()));
        return productDetailVO;
    }

    @Override
    public ServerResponse getProductList(int pageNum, int pageSize) {
        //startpage --start
        //填充业务SQL
        //pageHelper收尾
        PageHelper.startPage(pageNum, pageSize);
        List<Product> products = productMapper.selectProductList();
        PageInfo pageInfo = new PageInfo(products);

        return ServerResponse.buildSuccessfulDataResponse(pageInfo);
    }

    @Override
    public ServerResponse<PageInfo> searchProduct(int pageNum, int pageSize, Integer productId, String productName) {

        PageHelper.startPage(pageNum, pageSize);
        productName = StringUtils.isNotBlank(productName) ? productName = "%" + productName + "%" : "";
        List<Product> productList = productMapper.selectProductListByNameAndId(productId, productName);
        PageInfo pageInfo = new PageInfo(productList);
        return ServerResponse.buildSuccessfulDataResponse(pageInfo);
    }

    @Override
    public ServerResponse portalProductDetail(Integer productId) {
        if (productId == null) {
            return ServerResponse.buildUnSuccessfulMsgResponse("产品ID为空，参数不正确！！！");
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.buildUnSuccessfulMsgResponse("产品不存在！！！");
        }
        if (product.getStatus() != EnumProductStatus.ON_SALE.getCode()) {
            return ServerResponse.buildUnSuccessfulMsgResponse("产品下架或者不存在！！！");
        }
        return ServerResponse.buildSuccessfulDataResponse(assembleProductDetailVO(product));
    }


    @Override
    public ServerResponse<PageInfo> getPortalProductByKeywordCategoryId(String keyword,
                                                              Integer categoryId,
                                                              String orderBy,
                                                              int pageNum,
                                                              int pageSize) {
        if (StringUtils.isBlank(keyword) && categoryId == null) {
            return ServerResponse.buildUnSuccessfulMsgResponse("产品ID为空，参数不正确！！！");
        }
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category == null && StringUtils.isBlank(keyword)) {
            //分类不存在 返回空结果集 但是也需要分页内容
            PageHelper.startPage(pageNum, pageSize);
            PageInfo pageInfo = new PageInfo(Lists.<ProductDetailVO>newArrayList());
            return ServerResponse.buildSuccessfulDataResponse(pageInfo);
        }
        List<Integer> categoryIdList = iCategoryService.selectCategoryAndChildrenById(category.getId()).getData();
        if (StringUtils.isNotBlank(keyword)) {
            keyword = new StringBuilder("%").append(keyword).append("%").toString();
        }
        PageHelper.startPage(pageNum, pageSize);
        //排序处理
        if (StringUtils.isBlank(orderBy) &&
                AppConstants.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)) {
            String[] orderByArr = orderBy.split(",");
            PageHelper.orderBy(orderByArr[0] + " " + orderByArr[1]);
        }
        List<Product> productList = productMapper.selectProductListByNameAndCategoryIds(categoryIdList.size() == 0 ? null : categoryIdList, keyword);
        PageInfo pageInfoProduct = new PageInfo(productList);
        return ServerResponse.buildSuccessfulDataResponse(pageInfoProduct);
    }
}
