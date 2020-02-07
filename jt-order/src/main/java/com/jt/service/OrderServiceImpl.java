package com.jt.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.pojo.Order;
import com.jt.pojo.OrderItem;
import com.jt.pojo.OrderShipping;
import org.springframework.beans.factory.annotation.Autowired;


import com.alibaba.dubbo.config.annotation.Service;


import com.jt.mapper.OrderItemMapper;
import com.jt.mapper.OrderMapper;
import com.jt.mapper.OrderShippingMapper;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderServiceImpl implements DubboOrderService {
	
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderShippingMapper orderShippingMapper;
	@Autowired
	private OrderItemMapper orderItemMapper;


	@Transactional
	@Override
	public String saveOrder(Order order) {
		String orderId = ""+order.getUserId()+System.currentTimeMillis();
		Date date = new Date();

		// 订单入库
		order.setOrderId(orderId)
				.setStatus(1)
				.setCreated(date)
				.setUpdated(date);
		orderMapper.insert(order);

		// 订单物流入库
		OrderShipping orderShipping = order.getOrderShipping();
		orderShipping.setOrderId(orderId)
				.setCreated(date)
				.setUpdated(date);
		orderShippingMapper.insert(orderShipping);

		// 订单商品入库

		List<OrderItem> orderItems = order.getOrderItems();
		for (OrderItem orderitem: orderItems) {
			orderitem.setOrderId(orderId)
					.setCreated(date)
					.setUpdated(date);
			orderItemMapper.insert(orderitem);
		}

		System.out.println("入库成功");
		return orderId;
	}

	@Override
	public Order findOrderById(String id) {
		Order order = orderMapper.selectById(id);
		OrderShipping orderShipping = orderShippingMapper.selectById(id);
		QueryWrapper queryWrapper = new QueryWrapper();
		queryWrapper.eq("order_id",id);
		List<OrderItem> items = orderItemMapper.selectList(queryWrapper);
		order.setOrderShipping(orderShipping).setOrderItems(items);
		return order;
	}
}
