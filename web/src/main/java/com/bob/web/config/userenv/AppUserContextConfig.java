package com.bob.web.config.userenv;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 用户环境变量配置,可在任意初获取用户登录信息
 * 
 * @since 2017年7月7日 上午9:21:48
 * @version $Id$
 * @author JiangJibo
 *
 */
@Configuration
@ComponentScan() // 当此注解内不指定扫描包路径时,会使用此类的包名作为扫描的路径,即"com.bob.processor.mvc.userenv"
public class AppUserContextConfig {

}
