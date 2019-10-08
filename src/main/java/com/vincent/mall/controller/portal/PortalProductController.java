package com.vincent.mall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.vincent.mall.common.ServerResponse;
import com.vincent.mall.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: Vincent
 * @created: 2019/10/8  22:11
 * @description:前台产品管理controller
 */
@RequestMapping(value = "/product/")
@Controller
public class PortalProductController {

    @Autowired
    private IProductService productService;


    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse portalProductDetail(@RequestParam("product_id") Integer productId) {
        return productService.portalProductDetail(productId);
    }
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<PageInfo> portalListProduct(@RequestParam(required = false) String keyword,
                                                      @RequestParam(value = "category_id") Integer categoryId,
                                                      @RequestParam(value = "page_num", defaultValue = "1") int pageNum,
                                                      @RequestParam(value = "page_size", defaultValue = "10") int pageSize,
                                                      @RequestParam(value = "order_by", defaultValue = "") String orderBy) {

        return productService.getPortalProductByKeywordCategoryId(keyword, categoryId, orderBy, pageNum, pageSize);
    }

}
