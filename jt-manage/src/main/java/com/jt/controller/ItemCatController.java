package com.jt.controller;

import java.util.List;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;
import com.jt.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jt.pojo.ItemCat;
import com.jt.service.ItemCatService;
import com.jt.vo.EasyUITree;

@RestController
@RequestMapping("/item/cat")
public class ItemCatController {

	@Autowired
	private ItemCatService itemCatService;

	@Autowired
	private ItemService itemService;

	@RequestMapping("/queryItemName")
	public String findItemCatNameById(Long itemCatId) {
		ItemCat itemCat = itemCatService.findItemCatById(itemCatId);
		return itemCat.getName();
	}

	/**
	 * url:/item/cat/list
	 * 参数: 参数忽略
	 * 返回值: list<EasyUITree>
	 * @RequestParam:
	 *  defaultValue: 默认值 如果不传数据则使用默认值
	 *  name/value:   表示接受的数据
	 *  required:	  是否为必传数据
	 */
	@RequestMapping("/list")
	public List<EasyUITree> findItemCatByParentId(@RequestParam(defaultValue = "0",name = "id",required = true) Long parentId){

		return itemCatService.findItemCatByParentId(parentId);

		// 使用redis实现数据查询  注意是临时方法
		//return itemCatService.findItemCatCache(parentId);
	}


}
