package com.bob.web.config.stringvalueresolver;

import com.bob.web.mvc.mapper.BankUserMapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.util.StringValueResolver;

/**
 * 通过{@link org.springframework.beans.factory.annotation.Value}实现自定义属性注入
 * 属性值可以从环境变量，磁盘，内存,及网络等获取
 *
 * @author wb-jjb318191
 * @create 2018-02-27 11:32
 */
public class DefaultStringValueResolver implements StringValueResolver, BeanFactoryAware {

    @Autowired
    private BankUserMapper bankUserMapper;

    @Override
    public String resolveStringValue(String strVal) {
        String value = null;
        if (strVal.startsWith("#{") && strVal.endsWith("}")) {
            String key = strVal.substring(2, strVal.length() - 1);
            value = bankUserMapper.selectByPrimaryKey(Integer.valueOf(key)).getAge().toString();
        }
        return value == null ? strVal : value;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        ((DefaultListableBeanFactory)beanFactory).addEmbeddedValueResolver(this);
    }
}
