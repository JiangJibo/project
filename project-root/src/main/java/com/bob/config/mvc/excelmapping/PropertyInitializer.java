package com.bob.config.mvc.excelmapping;

/**
 * @since 2016年5月25日 上午11:15:46
 * @author
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
