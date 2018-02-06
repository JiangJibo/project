/**
 * Copyright(C) 2016 Fugle Technology Co. Ltd. All rights reserved.
 *
 */
package com.bob.project.mvc.service;

import java.util.List;

import com.bob.project.web.config.model.CacheModel;

/**
 * @since 2016年12月6日 上午11:28:41
 * @version $Id$
 * @author JiangJibo
 *
 */
public interface CacheModelService {

	public List<CacheModel> listAll();

	public void create(CacheModel student);

	public List<CacheModel> listByAge(Integer age);

	public CacheModel getById(Integer id);

	public Integer updateById(CacheModel student);

}
