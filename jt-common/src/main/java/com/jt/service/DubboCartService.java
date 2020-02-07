package com.jt.service;

import com.jt.pojo.Cart;

import java.util.List;

public interface DubboCartService {

	 void updateCartNum(Cart cart) ;


	List<Cart> findCartListByUserId(Long userId);

	void saveCart(Cart cart);

	void deleteCartById(Cart cart);
}
