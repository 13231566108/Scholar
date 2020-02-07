package com.jt.service;

import java.util.ArrayList;
import java.util.List;

import com.jt.anno.CacheFind;
import com.jt.util.ObjectMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.ItemCatMapper;
import com.jt.pojo.ItemCat;
import com.jt.vo.EasyUITree;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;

@Service
public class ItemCatServiceImpl implements ItemCatService {

	// required:必须注入的属性，如果注入失败，则容器启动失败
	@Autowired(required = false)
	private Jedis jedis;

	@Autowired 
	private ItemCatMapper ItemCatMapper;

	@Override
	public ItemCat findItemCatById(Long itemCatId) {
		return ItemCatMapper.selectById(itemCatId);
	}

	/**
	 * 步骤:
	 *  1 页面需要返回VO对象,数据库中查询的结果pojo对象
	 *  2 需要将pojo对象转化为VO对象
	 *  VO:   id text state(开/关)
	 *  pojo: id name isParent==true?closed:open
	 */
	@Override
	@CacheFind
	public List<EasyUITree> findItemCatByParentId(Long parentId) {
		List<EasyUITree> treeList = new ArrayList<>();
		List<ItemCat> itemCatList = findItemByParentId(parentId);
		for (ItemCat itemCat : itemCatList) {
			Long id = itemCat.getId();
			String text = itemCat.getName();
			String state = itemCat.getIsParent()?"closed" : "open";
			EasyUITree e = new EasyUITree(id,text,state);
			treeList.add(e);
		}
		return treeList;

	}





	private List<ItemCat> findItemByParentId(Long parentId) {
		QueryWrapper<ItemCat> queryWrapper = new QueryWrapper<ItemCat>();
		queryWrapper.eq("parent_id", parentId);
		return ItemCatMapper.selectList(queryWrapper);
		
	}

	/**
	 * 思路：
	 * 			1.先查询缓存  key: com.jt.service.ItemCatServiceImpl.findItemCatCache::parentId
	 * 	 		2.判断结果信息
	 *
	 */
	@Override
	public List <EasyUITree> findItemCatCache(Long parentId) {
		String key = " com.jt.service.ItemCatServiceImpl.findItemCatCache::"+parentId;
		String value = jedis.get(key);

		List<EasyUITree> treeList = new ArrayList <>();
		if(StringUtils.isEmpty(value)){
			treeList = findItemCatByParentId(parentId);
			String json = ObjectMapperUtil.toJson(treeList);
			jedis.set(key,json);
		}else {
			treeList = ObjectMapperUtil.toObj(value,treeList.getClass());
		}
		return treeList;
	}



}
