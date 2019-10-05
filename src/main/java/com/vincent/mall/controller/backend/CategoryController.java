package com.vincent.mall.controller.backend;

import com.vincent.mall.common.ServerResponse;
import com.vincent.mall.constants.AppConstants;
import com.vincent.mall.constants.enumeration.EnumResponseCode;
import com.vincent.mall.pojo.User;
import com.vincent.mall.service.ICategoryService;
import com.vincent.mall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author: Vincent
 * @created: 2019/10/4  23:07
 * @description:分类管理Controller
 */
@Controller
@RequestMapping("/manage/category/")
public class CategoryController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private ICategoryService categoryService;

    @ResponseBody
    @RequestMapping(value = "add_category.do", method = RequestMethod.POST)
    public ServerResponse addCategory(HttpSession session,
                                      @RequestParam("category_name") String categoryName,
                                      @RequestParam(value = "parent_id", defaultValue = "0") int parentId) {
        User currentUser = (User) session.getAttribute(AppConstants.CURRENT_USER);

        //校验登陆
        if (currentUser == null) {
            return ServerResponse.buildUnSuccessfulCodeAndMsgResponse(EnumResponseCode.NEED_LOGIN.getCode(),
                    "用户未登录！！！");
        }
        //校验是否是管理员
        ServerResponse response = iUserService.checkAdmin(currentUser);
        if (response.isSuccessful()) {
            //添加分类
            return categoryService.addCategory(categoryName, parentId);
        } else {
            return ServerResponse.buildUnSuccessfulMsgResponse("当前用户不是管理员！");
        }
    }

    @ResponseBody
    @RequestMapping(value = "update_category_name.do", method = RequestMethod.POST)
    public ServerResponse setCategoryName(HttpSession session,
                                          @RequestParam("category_name") String categoryName,
                                          @RequestParam(value = "category_id", defaultValue = "0") Integer categoryId) {
        User currentUser = (User) session.getAttribute(AppConstants.CURRENT_USER);
        //校验登陆
        if (currentUser == null) {
            return ServerResponse.buildUnSuccessfulCodeAndMsgResponse(EnumResponseCode.NEED_LOGIN.getCode(),
                    "用户未登录！！！");
        }
        //校验是否是管理员
        ServerResponse response = iUserService.checkAdmin(currentUser);
        if (response.isSuccessful()) {
            //更新分类
            return categoryService.updateCategory(categoryName, categoryId);
        } else {
            return ServerResponse.buildUnSuccessfulMsgResponse("当前用户不是管理员！");
        }
    }

    @ResponseBody
    @RequestMapping(value = "get_parallel_children_category.do", method = RequestMethod.POST)
    public ServerResponse getChildrenParallelCategory(HttpSession session,
                                                      @RequestParam(value = "category_id", defaultValue = "0") Integer categoryId) {


        User currentUser = (User) session.getAttribute(AppConstants.CURRENT_USER);
        //校验登陆
        if (currentUser == null) {
            return ServerResponse.buildUnSuccessfulCodeAndMsgResponse(EnumResponseCode.NEED_LOGIN.getCode(),
                    "用户未登录！！！");
        }
        //校验是否是管理员
        ServerResponse response = iUserService.checkAdmin(currentUser);
        if (response.isSuccessful()) {
            //查询平级子节点
            return categoryService.getChildrenParallelCategory(categoryId);
        } else {
            return ServerResponse.buildUnSuccessfulMsgResponse("当前用户不是管理员！");
        }
    }


    @ResponseBody
    @RequestMapping(value = "get_deep_children_category.do", method = RequestMethod.POST)
    public ServerResponse getDeepChildrenCategory(HttpSession session,
                                                  @RequestParam(value = "category_id", defaultValue = "0") Integer categoryId) {

        User currentUser = (User) session.getAttribute(AppConstants.CURRENT_USER);
        //校验登陆
        if (currentUser == null) {
            return ServerResponse.buildUnSuccessfulCodeAndMsgResponse(EnumResponseCode.NEED_LOGIN.getCode(),
                    "用户未登录！！！");
        }
        //校验是否是管理员
        ServerResponse response = iUserService.checkAdmin(currentUser);
        if (response.isSuccessful()) {
            //递归查询子节点品类
            //eg:0->100->2000
            return categoryService.selectCategoryAndChildrenById(categoryId);
        } else {
            return ServerResponse.buildUnSuccessfulMsgResponse("当前用户不是管理员！");
        }
    }
}
