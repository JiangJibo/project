/**
 * Copyright(C) 2017 Fugle Technology Co. Ltd. All rights reserved.
 *
 */
package com.bob.project.config.mvc.timer.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 定时器任务类
 * 
 * @since 2017年2月13日 下午5:02:14
 * @version $Id$
 * @author JiangJibo
 *
 */
@Component
public class QuartzTaskExample {

	final static Logger LOGGER = LoggerFactory.getLogger(QuartzTaskExample.class);

	/**
	 * 定时执行此方法
	 */
	public void execute() {
		LOGGER.info("{}:执行定时任务!", this.getClass().getSimpleName());
	}

}
