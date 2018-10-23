package com.bob.web.concrete.mapper;

import java.util.Arrays;
import java.util.Date;

import com.bob.web.config.BaseControllerTest;
import com.bob.web.mvc.entity.form.BankUserForm;
import com.bob.web.mvc.entity.vo.BankUserVO;
import com.bob.web.mvc.mapper.BankUserMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Administrator
 * @create 2018-04-01 20:07
 */
public class BankUserMapperTest extends BaseControllerTest {

    @Autowired
    private BankUserMapper bankUserMapper;

    /**
     * 测试插入情况下TypeHandler自动转换
     */
    @Test
    public void testListString2VarcharTypeHandler() {
        BankUserForm form = new BankUserForm();
        form.setAdresses(Arrays.asList("上海,杭州,北京"));
        form.setAge(30);
        form.setUsername("王小二");
        form.setBirthday(new Date());
        form.setIdcard("330881201804011254");
        form.setAdress("");
        form.setPhoneNumber("18758745852");
        form.setSex(1);
        bankUserMapper.insertWithAdresses(form);
        System.out.println(form.getUserId());
    }

    @Test
    public void testInsert() {
        BankUserForm form = new BankUserForm();
        form.setAge(30);
        form.setUsername("王小二");
        form.setBirthday(new Date());
        form.setIdcard("330881201804011254");
        form.setAdresses(Arrays.asList("上海,杭州,北京"));
        bankUserMapper.insertSelective(form);
    }

    /**
     * 测试在查询时TypeHandler是否自动转换
     */
    @Test
    public void testSelectWithTypeHandler() {
        BankUserVO bankUserVO = bankUserMapper.selectByUserId(36610);
        System.out.println(bankUserVO.getAddresses().toString());
    }

    @Test
    public void test$() {
        BankUserForm form = new BankUserForm();
        form.setUserId(152);
        form.setUsername("王小二");
        bankUserMapper.selectByForm(form);
    }

    @Test
    public void test$Value(){
        bankUserMapper.selectByName("lanboal");
    }

}
