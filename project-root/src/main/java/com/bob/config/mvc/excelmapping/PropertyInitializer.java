/**
 * Copyright(C) 2016 Fugle Technology Co. Ltd. All rights reserved.
 *
 */
package com.bob.config.mvc.excelmapping;

/**
 * @since 2016年5月25日 上午11:15:46
 * @version $Id: PropertyInitializer.java 17256 2016-06-26 09:35:05Z WuJianqiang $
 * @author HuMing
 *
 */
public interface PropertyInitializer<T> {

	/**
	 * 初始化解析的Excel对象
	 * 
	 * @return
	 */
	T initProperties();
}
