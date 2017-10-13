/**
 * Copyright(C) 2017 Fugle Technology Co. Ltd. All rights reserved.
 *
 */
package com.bob.test.concrete.propertysource;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.ResourcePropertySource;

import com.bob.test.config.BaseControllerTest;

/**
 * @since 2017年1月12日 下午4:41:13
 * @version $Id$
 * @author JiangJibo
 *
 */
public class PropertySourceTest extends BaseControllerTest {

	private static final String filePath = "com/bob/config/log/log4j.properties";

	private static final String copyPath = "com/bob/test/concrete/propertysource/log4j_copy.properties";

	private Environment environment;

	@Before
	public void initSources() throws IOException {
		environment = super.webApplicationContext.getEnvironment();
		MutablePropertySources propertySources = ((ConfigurableEnvironment) this.environment).getPropertySources();
		propertySources.addFirst(new ResourcePropertySource(new ClassPathResource(filePath)));
		propertySources.addLast(new ResourcePropertySource(new ClassPathResource(copyPath)));
	}

	@Test
	public void testGetEnvVars() {
		String layout = environment.getProperty("log4j.appender.E.layout");
		System.out.println(layout);
	}

	/* (non-Javadoc)
	 * @see com.bob.test.config.BaseControllerTest#init()
	 */
	@Override
	protected void init() {
		// TODO Auto-generated method stub

	}

}
