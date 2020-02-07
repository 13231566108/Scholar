package com.jt.service;

import com.jt.anno.CacheFind;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.util.HttpClientService;
import com.jt.util.ObjectMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private HttpClientService httpClientService;

	@Override
	@CacheFind
	public Item findItemById(Long itemId) {
		String url = "http://manage.jt.com/web/item/findItemById/"+itemId;
		String json = httpClientService.doGet(url);
		Item item = ObjectMapperUtil.toObj(json, Item.class);
		return item;
	}

	@Override
	@CacheFind
	public ItemDesc findItemDescById(Long itemId) {

			String url = "http://manage.jt.com/web/item/findItemDescById/"+itemId;
			String json = httpClientService.doGet(url);
			ItemDesc itemDesc = ObjectMapperUtil.toObj(json,ItemDesc.class);
			return itemDesc;

	}
}
