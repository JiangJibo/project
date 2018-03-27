package com.bob.common.mybatis;

import java.time.LocalDate;

import com.bob.common.utils.mybatis.statement.MybatisEntity;
import com.bob.common.utils.mybatis.statement.SqlProvider;
import org.junit.Before;
import org.junit.Test;

/**
 * Mysql基于注解形式的sql语句生成测试
 *
 * @author wb-jjb318191
 * @create 2017-09-11 11:10
 */
public class SqlGenerateTest {

    private SqlProvider sqlProvider;
    private MybatisEntity mybatisEntity;

    @Before
    public void doBefore(){
        sqlProvider = new SqlProvider();
        mybatisEntity = new MybatisEntity();
        mybatisEntity.setId("0015415");
        mybatisEntity.setName("lanboal");
        mybatisEntity.setAge(28);
        mybatisEntity.setDate(LocalDate.now());
        mybatisEntity.setUserAdressNumber(24);
    }

    @Test
    public void testInsert(){
        String sql = sqlProvider.insert(mybatisEntity);
        System.out.println(sql);
    }

    @Test
    public void testUpdate(){
        String sql = sqlProvider.update(mybatisEntity);
        System.out.println(sql);
    }

    @Test
    public void testDelete(){
        String sql = sqlProvider.delete(mybatisEntity);
        System.out.println(sql);
    }

    @Test
    public void testSelect(){
        String sql = sqlProvider.select(mybatisEntity);
        System.out.println(sql);
    }

}
