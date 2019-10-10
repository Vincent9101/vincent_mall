package com.vincent.mall.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.vincent.mall.common.ServerResponse;
import com.vincent.mall.constants.AppConstants;
import com.vincent.mall.dao.CartMapper;
import com.vincent.mall.dao.ProductMapper;
import com.vincent.mall.pojo.Cart;
import com.vincent.mall.pojo.Product;
import com.vincent.mall.service.ICartService;
import com.vincent.mall.util.BigDecimalUtil;
import com.vincent.mall.util.PropertiesUtil;
import com.vincent.mall.vo.CartProductVO;
import com.vincent.mall.vo.CartVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: Vincent
 * @created: 2019/10/9  22:55
 * @description:购物车模块
 */
@Service
@Slf4j
public class CartServiceImpl implements ICartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public ServerResponse<CartVO> addProductToCart(Integer userId, Integer productId, Integer count) {
        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        if (cart == null) {
            //产品不存在 ，增加产品记录
            Cart cartItem = new Cart();
            cartItem.setQuantity(count);
            cartItem.setChecked(AppConstants.CartStatus.CHECKED);
            cartItem.setProductId(productId);
            cartItem.setUserId(userId);
            log.info("产品不存在购物车中，进行插入。");
            int rows = cartMapper.insert(cartItem);
            if (rows > 0) {
                log.info("产品不存在购物车中，插入成功。");
            } else {
                log.info("产品不存在购物车中，插入失败。");
            }
        } else {
            //产品已存在
            Cart updateCart = new Cart();
            updateCart.setId(cart.getId());
            updateCart.setQuantity(cart.getQuantity() + count);
            int rows = cartMapper.updateByPrimaryKeySelective(updateCart);
            if (rows > 0) {
                log.info("产品存在购物车中，更新成功。");
            } else {
                log.info("产品存在购物车中，更新失败。");
            }
        }
        CartVO cartVO = getCartVoLimit(userId);
        return ServerResponse.buildSuccessfulDataResponse(cartVO);
    }

    /**
     * 根据用户ID获取其购物车的产品状态
     *
     * @param userId
     * @return
     */
    private CartVO getCartVoLimit(Integer userId) {
        CartVO cartVO = new CartVO();
        List<Cart> cartList = cartMapper.selectCartByUserId(userId);
        List<CartProductVO> cartProductVOList = Lists.newArrayList();

        BigDecimal cartTotalPrice = new BigDecimal("0");

        for (Cart cartItem : cartList) {
            CartProductVO cartProductVo = new CartProductVO();
            cartProductVo.setId(cartItem.getId());
            cartProductVo.setUserId(userId);
            cartProductVo.setProductId(cartItem.getProductId());

            Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
            if (product != null) {
                cartProductVo.setProductMainImage(product.getMainImage());
                cartProductVo.setProductName(product.getName());
                cartProductVo.setProductSubtitle(product.getSubtitle());
                cartProductVo.setProductStatus(product.getStatus());
                cartProductVo.setProductPrice(product.getPrice());
                cartProductVo.setProductStock(product.getStock());
                //判断库存
                int buyLimitCount = 0;
                if (product.getStock() >= cartItem.getQuantity()) {
                    //库存充足的时候
                    buyLimitCount = cartItem.getQuantity();
                    cartProductVo.setLimitQuantity(AppConstants.CartStatus.LIMIT_NUM_SUCCESS);
                } else {
                    buyLimitCount = product.getStock();
                    cartProductVo.setLimitQuantity(AppConstants.CartStatus.LIMIT_NUM_FAIL);
                    //购物车中更新有效库存
                    Cart cartForQuantity = new Cart();
                    cartForQuantity.setId(cartItem.getId());
                    cartForQuantity.setQuantity(buyLimitCount);
                    cartMapper.updateByPrimaryKeySelective(cartForQuantity);
                }
                cartProductVo.setQuantity(buyLimitCount);
                //计算总价
                cartProductVo.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(), cartProductVo.getQuantity()));
                cartProductVo.setProductChecked(cartItem.getChecked());
            }

            if (cartItem.getChecked() == AppConstants.CartStatus.CHECKED) {
                //如果已经勾选,增加到整个的购物车总价中
                cartTotalPrice = (BigDecimalUtil.add(cartTotalPrice.doubleValue(), cartProductVo.getProductTotalPrice().doubleValue()));
            }
            cartProductVOList.add(cartProductVo);
        }
        cartVO.setCartTotalPrice(cartTotalPrice);
        cartVO.setCartProductVOList(cartProductVOList);
        cartVO.setAllChecked(this.getAllCheckedStatus(userId));
        cartVO.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        return cartVO;
    }

    private boolean getAllCheckedStatus(Integer userId) {
        if (userId == null) {
            return false;
        }
        return cartMapper.selectCartProductCheckedStatusByUserId(userId) == 0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ServerResponse<CartVO> updateProductInCart(Integer userId, Integer productId, Integer count) {

        if (productId == null && count == null) {
            return ServerResponse.buildUnSuccessfulMsgResponse("参数有误！");
        }
        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        if (cart != null) {
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKeySelective(cart);
        }
        return getProductFromCart(userId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ServerResponse<CartVO> deleteProductFromCart(String productIdlist, Integer userId) {
        List<String> productIds = Splitter.on(",").splitToList(productIdlist);
        if (CollectionUtils.isEmpty(productIds)) {
            return ServerResponse.buildUnSuccessfulMsgResponse("产品id为空，参数有误!");
        }
        cartMapper.deleteCartProductByUserIdAndProductIds(userId, productIds);
        return getProductFromCart(userId);
    }

    @Override
    public ServerResponse<CartVO> getProductFromCart(Integer userId) {
        return ServerResponse.buildSuccessfulDataResponse(getCartVoLimit(userId));
    }

    @Override
    public ServerResponse<CartVO> selectOrUnSelect(Integer userId, Integer checked, Integer productId) {
        cartMapper.checkedOrUnCheckedProduct(userId, checked, productId);
        return getProductFromCart(userId);
    }

    @Override
    public ServerResponse<Integer> getCartCount(Integer userId) {
        return ServerResponse.buildSuccessfulDataResponse(cartMapper.selectCountFromCart(userId));
    }
}
