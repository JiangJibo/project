package com.bob.project.test.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;

import com.bob.project.config.RootContextConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author JiangJibo
 * @version $Id$
 * @since 2016年12月8日 下午4:45:26
 */
//@Rollback()
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RootContextConfig.class})
public abstract class BaseControllerTest {

    protected Gson gson;

    @Autowired
    protected WebApplicationContext webApplicationContext;

    @Before()
    public void setup() {
        //使用GsonBuilder针对日期类型指定解析后的格式,当Date只有年月日时,会使用当前时间来凑够解析长度
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    }

}
