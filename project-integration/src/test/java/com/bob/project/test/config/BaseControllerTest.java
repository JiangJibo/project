package com.bob.project.test.config;

import com.bob.project.config.RootContextConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author JiangJibo
 * @version $Id$
 * @since 2016年12月8日 下午4:45:26
 */
@Rollback
@Transactional
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
