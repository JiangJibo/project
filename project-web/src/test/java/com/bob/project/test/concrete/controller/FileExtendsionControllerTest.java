/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 */
package com.bob.project.test.concrete.controller;

import com.bob.project.mvc.controller.FileExtensionController;
import com.bob.project.test.config.BaseControllerTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @since 2017年3月6日 下午4:55:06
 * @version $Id$
 * @author JiangJibo
 *
 */
public class FileExtendsionControllerTest extends BaseControllerTest {

    @Autowired
    private FileExtensionController fileUploadController;

    /* (non-Javadoc)
     * @see BaseControllerTest#init()
     */
    @Override
    protected void init() {
        super.loginBefore = false;
        super.mappedController = fileUploadController;
    }

    @Test
    public void testUploadFile() {
        System.out.println(this.fileUpload("image", "C:/Users/dell-7359/Desktop/log4j.properties", "/files/uploading", false, ""));
    }

}
