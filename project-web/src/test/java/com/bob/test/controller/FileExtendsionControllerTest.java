/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package com.bob.test.controller;

import com.bob.test.config.BaseControllerTest;
import org.junit.Test;


/**
 * @since 2017年3月6日 下午4:55:06
 * @version $Id$
 * @author JiangJibo
 *
 */
public class FileExtendsionControllerTest extends BaseControllerTest {

	/* (non-Javadoc)
	 * @see com.bob.test.config.BaseControllerTest#init()
	 */
	@Override
	protected void init() {

	}

	@Test
	public void testUploadFile() {
		System.out.println(this.fileUpload("image", "C:/Users/dell-7359/Desktop/log4j.properties", "/files/uploading", false, ""));
	}

}
