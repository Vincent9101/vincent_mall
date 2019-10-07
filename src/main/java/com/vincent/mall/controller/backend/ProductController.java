package com.vincent.mall.controller.backend;

import com.google.common.collect.Maps;
import com.vincent.mall.common.ServerResponse;
import com.vincent.mall.constants.AppConstants;
import com.vincent.mall.constants.enumeration.EnumResponseCode;
import com.vincent.mall.pojo.Product;
import com.vincent.mall.pojo.User;
import com.vincent.mall.service.IFileService;
import com.vincent.mall.service.IProductService;
import com.vincent.mall.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author: Vincent
 * @created: 2019/10/5  14:49
 * @description:产品接口
 */
@Controller
@RequestMapping("/manage/product/")
public class ProductController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IProductService productService;
    @Autowired
    private IFileService fileService;

    @RequestMapping(value = "save_product.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ServerResponse productSave(HttpSession session, @RequestBody Product product) {
        User currentUser = (User) session.getAttribute(AppConstants.CURRENT_USER);
        //校验登陆
        if (currentUser == null) {
            return ServerResponse.buildUnSuccessfulCodeAndMsgResponse(EnumResponseCode.NEED_LOGIN.getCode(),
                    "用户未登录！！！");
        }
        //校验是否是管理员
        ServerResponse response = iUserService.checkAdmin(currentUser);
        if (response.isSuccessful()) {
            //添加产品
            return productService.saveOrUpdateProduct(product);
        } else {
            return ServerResponse.buildUnSuccessfulMsgResponse("当前用户不是管理员！");
        }
    }

    /**
     * 更新产品上下架
     *
     * @param session
     * @param productId
     * @param status
     * @return
     */
    @RequestMapping(value = "save_sale_status.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse updateProductSaleStatus(HttpSession session,
                                                  @RequestParam("product_id") Integer productId,
                                                  @RequestParam("status") Integer status) {
        User currentUser = (User) session.getAttribute(AppConstants.CURRENT_USER);
        //校验登陆
        if (currentUser == null) {
            return ServerResponse.buildUnSuccessfulCodeAndMsgResponse(EnumResponseCode.NEED_LOGIN.getCode(),
                    "用户未登录！！！");
        }
        //校验是否是管理员
        ServerResponse response = iUserService.checkAdmin(currentUser);
        if (response.isSuccessful()) {
            //更新上架状态
            return productService.updateSaleStatus(productId, status);
        } else {
            return ServerResponse.buildUnSuccessfulMsgResponse("当前用户不是管理员！");
        }
    }

    @RequestMapping(value = "detail.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getDetail(HttpSession session,
                                    @RequestParam("product_id") Integer productId) {
        User currentUser = (User) session.getAttribute(AppConstants.CURRENT_USER);
        //校验登陆
        if (currentUser == null) {
            return ServerResponse.buildUnSuccessfulCodeAndMsgResponse(EnumResponseCode.NEED_LOGIN.getCode(),
                    "用户未登录！！！");
        }
        //校验是否是管理员
        ServerResponse response = iUserService.checkAdmin(currentUser);
        if (response.isSuccessful()) {
            //获取detail
            return productService.manageGetProductDetail(productId);
        } else {
            return ServerResponse.buildUnSuccessfulMsgResponse("当前用户不是管理员！");
        }
    }


    @RequestMapping(value = "list.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getList(HttpSession session,
                                  @RequestParam(value = "page_num", defaultValue = "1") int pageNum,
                                  @RequestParam(value = "page_size", defaultValue = "10") int pageSize) {
        User currentUser = (User) session.getAttribute(AppConstants.CURRENT_USER);
        //校验登陆
        if (currentUser == null) {
            return ServerResponse.buildUnSuccessfulCodeAndMsgResponse(EnumResponseCode.NEED_LOGIN.getCode(),
                    "用户未登录！！！");
        }
        //校验是否是管理员
        ServerResponse response = iUserService.checkAdmin(currentUser);
        if (response.isSuccessful()) {
            //获取list
            return productService.getProductList(pageNum, pageSize);
        } else {
            return ServerResponse.buildUnSuccessfulMsgResponse("当前用户不是管理员！");
        }
    }

    @RequestMapping(value = "search.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse searchProduct(HttpSession session,
                                        @RequestParam(value = "product_name", required = false) String productName,
                                        @RequestParam(value = "product_id", required = false) Integer productId,
                                        @RequestParam(value = "page_num", defaultValue = "1") int pageNum,
                                        @RequestParam(value = "page_size", defaultValue = "10") int pageSize) {
        User currentUser = (User) session.getAttribute(AppConstants.CURRENT_USER);
        //校验登陆
        if (currentUser == null) {
            return ServerResponse.buildUnSuccessfulCodeAndMsgResponse(EnumResponseCode.NEED_LOGIN.getCode(),
                    "用户未登录！！！");
        }
        //校验是否是管理员
        ServerResponse response = iUserService.checkAdmin(currentUser);
        if (response.isSuccessful()) {
            //获取list
            return productService.searchProduct(pageNum, pageSize, productId, productName);
        } else {
            return ServerResponse.buildUnSuccessfulMsgResponse("当前用户不是管理员！");
        }
    }

    @RequestMapping(value = "upload.do")
    @ResponseBody
    public ServerResponse upload(HttpSession session,
                                 @RequestParam("upload_file") MultipartFile file,
                                 HttpServletRequest request) {

        User currentUser = (User) session.getAttribute(AppConstants.CURRENT_USER);
        //校验登陆
        if (currentUser == null) {
            return ServerResponse.buildUnSuccessfulCodeAndMsgResponse(EnumResponseCode.NEED_LOGIN.getCode(),
                    "用户未登录！！！");
        }
        //校验是否是管理员
        ServerResponse response = iUserService.checkAdmin(currentUser);
        if (!response.isSuccessful()) {
            return ServerResponse.buildUnSuccessfulMsgResponse("当前用户不是管理员！");
        }
        String path = request.getSession().getServletContext().getRealPath("upload");
        String tagetFileName = fileService.upload(file, path);
        Map<String, String> resMap = Maps.newHashMap();
        resMap.put("uri", tagetFileName);
        return ServerResponse.buildSuccessfulDataResponse(resMap);
    }

    @RequestMapping(value = "rich_text_upload.do")
    @ResponseBody
    public Map richTextUpload(
            HttpSession session,
            @RequestParam("upload_file") MultipartFile file,
            HttpServletRequest request) {
        Map resMap = Maps.newHashMap();
        User currentUser = (User) session.getAttribute(AppConstants.CURRENT_USER);
        //校验登陆
        if (currentUser == null) {
            resMap.put("success", false);
            resMap.put("msg", "用户未登录！！！");
            return resMap;
        }
        //校验是否是管理员
        ServerResponse response = iUserService.checkAdmin(currentUser);
        if (!response.isSuccessful()) {
            resMap.put("success", false);
            resMap.put("msg", "当前用户不是管理员！！！");
            return resMap;
        }

        String path = request.getSession().getServletContext().getRealPath("upload");
        //富文本返回值有要求
        String tagetFileName = fileService.upload(file, path);
        if (StringUtils.isBlank(tagetFileName)) {
            resMap.put("success", false);
            resMap.put("msg", "上传失败！！！");
        } else {
            //todo: 根据前台富文本插件设置reponse header
            resMap.put("file_path", tagetFileName);
            resMap.put("success", true);
            resMap.put("msg", "上传成功！！！");
        }
        return resMap;
    }

}
