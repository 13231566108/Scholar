package com.jt.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.pojo.User;
import com.jt.service.DubboCartService;

import com.jt.util.UserThreadLocal;
import com.jt.vo.SysResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

	@Reference
	private DubboCartService dubboCartService;

	/**
	 * 跳转购物车页面
	 * 页面的数据要求：items=“…￥{cartList}”
	 */
	@RequestMapping("/show")
	public String show(Model model){
		//User user = (User) model.getAttribute("JT_USER");

		Long userId = UserThreadLocal.get().getId();
		List<Cart> cartList = dubboCartService.findCartListByUserId(userId);
		model.addAttribute("cartList",cartList);
		return "cart";
	}

	@RequestMapping("/update/num/{itemId}/{num}")
	@ResponseBody
	public SysResult updateCartNum(Cart cart){
		Long userId = UserThreadLocal.get().getId();
		cart.setUserId(userId);
		dubboCartService.updateCartNum(cart);
		return  SysResult.success();
	}

	@RequestMapping("/add/{itemId}")
	public String saveCart(Cart cart){
		Long userId = UserThreadLocal.get().getId();
		cart.setUserId(userId);
		dubboCartService.saveCart(cart);
		return "redirect:/cart/show.html";
	}

	@RequestMapping("/delete/{itemId}")
	public String deleteCart(Cart cart){
		Long userId = UserThreadLocal.get().getId();
		cart.setUserId(userId);
		dubboCartService.deleteCartById(cart);
		return "redirect:/";
	}
}
