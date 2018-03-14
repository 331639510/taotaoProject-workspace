package com.taotao.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;

/**
 * 购物车管理Controller
 * <p>Title: CartController</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Controller
public class CartController {

	@Value("${CART_KEY}")
	private String CART_KEY;
	
	@Value("${CART_EXPIER}")
	private Integer CART_EXPIER;
	
	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/cart/add/{itemId}")
	public String addItemCart(@PathVariable Long itemId, @RequestParam(defaultValue="1") Integer num, HttpServletRequest request, HttpServletResponse response) {
//		取购物车商品列表
		List<TbItem> cartItemList = getCartItemList(request);
//		判断商品在购物车中是否存在
		boolean flag = false;
		for (TbItem tbItem : cartItemList) {
			if(tbItem.getId() == itemId.longValue()) {
//				如果存在数量相加
				tbItem.setNum(tbItem.getNum() +num);
				flag = true;
				break;
			}
		}
//		如果 不存在，添加一个新商品
		if(!flag) {
//			需要调用服务取商品信息
			TbItem tbItem = itemService.geTbItemById(itemId);
//			设置购买的商品数量
			tbItem.setNum(num);
//			取一张图片
			String image = tbItem.getImage();
			if(StringUtils.isNotBlank(image)) {
				String[] images = image.split(",");
				tbItem.setImage(images[0]);
			}
//			把商品添加购物车
			cartItemList.add(tbItem);
		}
		CookieUtils.setCookie(request, response, CART_KEY, JsonUtils.objectToJson(cartItemList), CART_EXPIER, true);
		return "cartSuccess";
	}
	
	private List<TbItem> getCartItemList(HttpServletRequest request){
//		从cookie中取购物车商品列表
		String json = CookieUtils.getCookieValue(request, CART_KEY, true);
		if(StringUtils.isBlank(json)) {
//			如果没有内容，返回一个空列表
			return new ArrayList<>();
		}
		List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
		return list;
	}
	
	@RequestMapping("/cart/cart")
	public String showCartList(HttpServletRequest request) {
//		从cookie取购物车列表
		List<TbItem> cartItemList = getCartItemList(request);
//		把购物车列表传递jsp
		request.setAttribute("cartList", cartItemList);
		return "cart";
	}
	
	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public TaotaoResult updateItemNum(@PathVariable Long itemId, @PathVariable Integer num, HttpServletRequest request, HttpServletResponse response) {
//		从cookie获取购物车列表
		List<TbItem> cartList = getCartItemList(request);
//		查询对应商品
		for (TbItem tbItem : cartList) {
			if(tbItem.getId() == itemId.longValue()) {
//				更新商品数量
				tbItem.setNum(num);
				break;
			}
		}
//		把购物车列表写入cookie
		CookieUtils.setCookie(request, response, CART_KEY, JsonUtils.objectToJson(cartList), CART_EXPIER, true);
		return TaotaoResult.ok();
	}
	
	@RequestMapping("/cart/delete/{itemId}")
	public String deleteCarItem(@PathVariable Long itemId, HttpServletRequest request, HttpServletResponse response) {
//		从cookie取购物车列表
		List<TbItem> cartItemList = getCartItemList(request);
//		找到对应商品
		for (TbItem tbItem : cartItemList) {
			if(tbItem.getId() == itemId.longValue()) {
//				删除商品
				cartItemList.remove(tbItem);
				break;
			}
		}
		CookieUtils.setCookie(request, response, CART_KEY, JsonUtils.objectToJson(cartItemList), CART_EXPIER, true);
//		重定向到购物车列表页面
		return "redirect:/cart/cart.html";
	}
}
