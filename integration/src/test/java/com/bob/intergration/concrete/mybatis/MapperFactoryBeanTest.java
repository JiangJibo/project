package com.bob.intergration.concrete.mybatis;

import java.beans.PropertyDescriptor;
import java.util.Set;

import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.ClassPathMapperScanner;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.GenericBeanDefinition;

/**
 * @author Administrator
 * @create 2018-03-03 11:13
 */
public class MapperFactoryBeanTest {

    /**
     * 此方法揭示了Spring在实例化Mapper对象时,如何将SqlSessionFactory与其关联起来
     *
     * @see ClassPathMapperScanner#processBeanDefinitions(Set),218行
     * 当未明确指定sqlSessionFactory及SqlSessionTemplate时,设置{@link AbstractBeanDefinition#autowireMode} ={@link AbstractBeanDefinition#AUTOWIRE_BY_TYPE}
     *
     * Spring在实例化Bean时，发现其{@link AbstractBeanDefinition#autowireMode} = {@link AbstractBeanDefinition#AUTOWIRE_BY_TYPE}时,
     * 调用{@link AbstractAutowireCapableBeanFactory#autowireByType(String, AbstractBeanDefinition, BeanWrapper, MutablePropertyValues)}
     *
     * 在{@link AbstractAutowireCapableBeanFactory#autowireByType
     * }方法时再调用{@link AbstractAutowireCapableBeanFactory#unsatisfiedNonSimpleProperties(AbstractBeanDefinition, BeanWrapper)}
     *
     * 在unsatisfiedNonSimpleProperties()方法内获取其PropertyDescriptors,提取出符合属性描述"sqlSessionFactory","sqlSessionTemplate"
     * 然后解析这两个属性描述对应的类型,可以是属性类型，或者从getter,setter方法上提取，之后实例化此类型的Bean,最后执行MapperFactoryBean相应属性的setter方法
     * @see MapperFactoryBean#setSqlSessionFactory(SqlSessionFactory)
     * @see MapperFactoryBean#setSqlSessionTemplate(SqlSessionTemplate)
     */
    @Test
    public void testGetMapperFactoryBeanProDesc() {
        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(new MapperFactoryBean());
        for (PropertyDescriptor pd : beanWrapper.getPropertyDescriptors()) {
            System.out.println(pd.getName());
        }
    }

    /**
     * {@link ClassPathMapperScanner#processBeanDefinitions(Set)#186行}在扫描解析Mapper接口时,将接口的类名设置为泛型参数GenericArgumentValue
     * 这样Spring在实例化这个MapperFactoryBean时,会去其BeanDefinition中查看是否有泛型参数,若有则会使用附带此类型参数的构造函数
     * {@link MapperFactoryBean#MapperFactoryBean(Class)}
     * 这样Mapper接口的实际类型在实例化MapperFactoryBean时就注入其中了,之后SqlSessionFactory就能通过此接口生成动态代理对象,
     * 注入Spring中
     */
    public void setGenericMapperType() {
        GenericBeanDefinition definition = new GenericBeanDefinition();
        definition.setBeanClass(MapperFactoryBean.class);

        // the mapper interface is the original class of the bean
        // but, the actual class of the bean is MapperFactoryBean
        definition.getConstructorArgumentValues().addGenericArgumentValue(definition.getBeanClassName()); // issue #59

    }

}
