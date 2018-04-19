package com.bob.web.concrete.mapper;

import com.bob.web.config.BaseControllerTest;
import com.bob.web.mvc.mapper.UserMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wb-jjb318191
 * @create 2018-04-18 10:58
 */
public class UserMapperTest extends BaseControllerTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testWithDynamicSql(){
        userMapper.selectByName("lanboal");
    }

}
