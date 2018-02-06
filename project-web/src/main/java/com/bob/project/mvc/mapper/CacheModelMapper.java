/**
 * Copyright(C) 2016 Fugle Technology Co. Ltd. All rights reserved.
 */
package com.bob.project.mvc.mapper;

import java.util.List;

import com.bob.project.utils.BaseMapper;
import com.bob.project.web.config.model.CacheModel;

/**
 * @author JiangJibo
 * @version $Id$
 * @since 2016年12月6日 上午10:46:58
 */
public interface CacheModelMapper extends BaseMapper<Integer, CacheModel> {

    public List<CacheModel> selectAll();

    public List<CacheModel> selectByAge(Integer age);

    public CacheModel selectById(Integer id);

    public Integer updateById(CacheModel student);
}
