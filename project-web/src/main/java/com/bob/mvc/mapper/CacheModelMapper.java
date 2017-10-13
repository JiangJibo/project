/**
 * Copyright(C) 2016 Fugle Technology Co. Ltd. All rights reserved.
 *
 */
package com.bob.mvc.mapper;

import java.util.List;

import com.bob.config.mvc.model.CacheModel;

/**
 * @since 2016年12月6日 上午10:46:58
 * @version $Id$
 * @author JiangJibo
 *
 */
public interface CacheModelMapper {

	public List<CacheModel> selectAll();

	public void insert(CacheModel student);

	public List<CacheModel> selectByAge(Integer age);

	public CacheModel selectById(Integer id);

	public Integer updateById(CacheModel student);
}
