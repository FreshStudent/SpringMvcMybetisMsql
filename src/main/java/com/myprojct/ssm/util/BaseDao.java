package com.myprojct.ssm.util;

import java.util.List;

public interface BaseDao<E> {
	public void create(E e);
	public void update(E e);
	public void delete(E e);
	public E get(long id);
	public long count(E e);
	/**
	 * 分页查询
	 * @param e
	 * @return
	 */
	public List<E> queryByPage(E e);
	/**
	 * 不分页查询
	 * @param e
	 * @return
	 */
	public List<E> query(E e);
	
	
}
