package com.jt.service;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.jt.mapper.ItemDescMapper;
import com.jt.pojo.ItemDesc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jt.mapper.ItemMapper;
import com.jt.pojo.Item;
import com.jt.vo.EasyUITable;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired // spring容器创建代理对象
	private ItemMapper itemMapper;

	@Autowired
	private ItemDescMapper itemDescMapper;

	/**
	 * 第一页: 每页20条 select * from tb_item limit 起始位置:每页条数 select * from tb_item limit
	 * 0,20;
	 * 
	 * 第二页: select * from tb_item limit 20,20;
	 * 
	 * 第三页: select * from tb_item limit 40,20;
	 * 
	 * 第N页: select * from tb_item limit (page-1)rows,rows;
	 */
	@Override
	public EasyUITable findItemByPage(int page, int rows) {

//		int total = itemMapper.selectCount(null); 
//		int start = (page-1) * rows;
//		List<Item> itemList = itemMapper.findItemByPage(start,rows);

		
		IPage<Item> iPage = new Page<>(page, rows); 
		QueryWrapper<Item> queryWrapper = new QueryWrapper<>();
		queryWrapper.orderByDesc("updated");
		IPage<Item> itemPage = itemMapper.selectPage(iPage, queryWrapper);
		int total = (int) itemPage.getTotal();
		List<Item> itemList = itemPage.getRecords();
		
		return new EasyUITable(total, itemList);
	}



	// 是否添加事务

	/**
	 * Spring中事务回滚策略
	 * 默认策略:
	 *  如果程序执行,遇到运行时异常,则事务自动回滚
	 *  如果程序执行,遇到检查异常/编译异常，Spring不负责回滚
	 * @param item
	 */
	@Transactional
	@Override
	public void saveItem(Item item, ItemDesc itemDesc) {
		item.setStatus(1).setCreated(new Date()).setUpdated(item.getCreated());
		// mp: 数据入库之后，会自动回显
		itemMapper.insert(item);

		// 补齐数据
		// Item是主键自增，只有入库之后才有主键，但是对象中没有主键信息
		itemDesc.setItemId(item.getId()).setCreated(item.getCreated()).setUpdated(item.getUpdated());
		itemDescMapper.insert(itemDesc);
	}

	@Override
	public ItemDesc findItemDescById(Long itemId) {
		return itemDescMapper.selectById(itemId);
	}

	@Override
	@Transactional
	public void updateItem(Item item, ItemDesc itemDesc) {

		item.setUpdated(new Date());
		itemMapper.updateById(item);

		itemDesc.setItemId(item.getId()).setUpdated(item.getUpdated());
		itemDescMapper.updateById(itemDesc);
	}

	@Override
	@Transactional
	public void deleteItems(Long[] ids) {
		List<Long> idList = Arrays.asList(ids);
		itemMapper.deleteBatchIds(idList);
		itemDescMapper.deleteBatchIds(idList);
	}

	/**
	 * 修改内容为status/updated，条件id
	 * @param ids
	 * @param status
	 */
	@Override
	public void updateStatus(Long[] ids, int status) {
		Item item = new Item();
		item.setStatus(status).setUpdated(new Date());
		QueryWrapper queryWrapper = new QueryWrapper();
		List <Long> idList = Arrays.asList(ids);
		queryWrapper.in("id",idList);
		itemMapper.update(item,queryWrapper);
	}

	@Override
	public Item findItemById(Long itemId) {
		return itemMapper.selectById(itemId);
	}

}
