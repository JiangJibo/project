/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.root.config.timer.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 自定义一个定时器,定时器的间隔可以自由配置，看@Schedule内的属性
 * 
 * @since 2017年4月3日 下午8:29:15
 * @version $Id$
 * @author JiangJibo
 *
 */
public class ScheduleTask {

	final static Logger LOGGER = LoggerFactory.getLogger(ScheduleTask.class);

	//'cron', 'fixedDelay(String)', or 'fixedRate(String)' 这几个属性只能有一个
	@Scheduled(initialDelay = 300000, fixedDelay = 300000) // 在容器初始化后5分钟执行第一次,之后每5分钟执行一次
	public void invokeEvery5Min() {
		LOGGER.debug("每5分钟尝试执行一次");
	}

}
