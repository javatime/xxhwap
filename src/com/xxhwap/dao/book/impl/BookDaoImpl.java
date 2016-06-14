package com.xxhwap.dao.book.impl;

import com.xxhwap.book.TudouBookInfo;
import com.xxhwap.dao.base.IbatisBaseDao;
import com.xxhwap.dao.book.IBookDao;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhengyunfei
 *
 */
@Component("bookDao")
public class BookDaoImpl extends IbatisBaseDao implements IBookDao{
	private static final String COMMON = "com.xxhwap.book.";
	private static final String ADD = COMMON+"add";
	private static final String SAVE = COMMON+"save";
	private static final String UPDATE = COMMON+"update";
	private static final String DELETE = COMMON+"delete";
	private static final String FINDALLLIST = COMMON+"findAllList";
	private static final String FINDBYID = COMMON+"findById";
	@Override
	public long sendBook(TudouBookInfo bookInfo) {
		long id=0;
		try{
			id=(Long)this.insert(ADD,bookInfo);
		}catch (Exception e){
			e.printStackTrace();
		}
		return id;
	}
	@Override
	public long saveBook(TudouBookInfo bookInfo) {
		long id=0;
		try{
			id=(Long)this.insert(SAVE,bookInfo);
		}catch (Exception e){
			e.printStackTrace();
		}
		return id;
	}
	@Override
	public List<TudouBookInfo> findSendBookList(Map<String, Object> queryMap) {
		List<TudouBookInfo> list=new ArrayList<TudouBookInfo>();
		try {
			list= (List<TudouBookInfo>) this.queryAll(FINDALLLIST,queryMap);
		}catch (Exception e){
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public TudouBookInfo findById(String id) {
		TudouBookInfo bookInfo=null;
		try {
			Map<String,Object> queryMap=new HashMap<String, Object>();
			queryMap.put("id",id);
			bookInfo= (TudouBookInfo) this.query(FINDBYID,queryMap);
		}catch (Exception e){
			e.printStackTrace();
		}
		return bookInfo;
	}

	@Override
	public boolean updateBook(TudouBookInfo bookInfo) {
		boolean flg=false;
		try{
			this.update(UPDATE,bookInfo);
			flg=true;
		}catch (Exception e){
			e.printStackTrace();
		}
		return flg;
	}

	public int deleteByMap(Map<String, Object> queryMap) {
		int count=0;
		try{
			count=this.update(DELETE,queryMap);
		}catch (Exception e){
			e.printStackTrace();
		}
		return count;
	}
}