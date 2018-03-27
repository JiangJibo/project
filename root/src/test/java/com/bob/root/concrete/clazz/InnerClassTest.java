package com.bob.root.concrete.clazz;

import java.lang.reflect.Field;

import com.bob.root.concrete.clazz.TopLevelClass.InnerClass;
import com.bob.root.concrete.clazz.TopLevelClass.NestedClass;
import org.junit.Test;

/**
 * 内部类与嵌套类测试
 *
 * @author wb-jjb318191
 * @create 2017-11-02 9:36
 */
public class InnerClassTest {

    @Test
    public void getInnerClass(){
        //InnerClass is = new InnerClass(); 会报错
        InnerClass is = new TopLevelClass().getInnerClass();
    }

    @Test
    public void getNestedClass(){
        NestedClass ns = new NestedClass();
        ns = new TopLevelClass().getNestedClass();
    }

    @Test
    public void testInnerClassName() {
        //获取所有的内部类及嵌套类
        Class<?>[] classes = TopLevelClass.class.getDeclaredClasses();
        for (Class<?> clazz : classes) {
            //内部类和嵌套类都属于成员类
            System.out.println(clazz.isMemberClass());
            System.out.println(
                "Class.Name = " + clazz.getName() + ", Class.SimpleName = " + clazz.getSimpleName() + ", Class.Package = " + clazz.getPackage().getName());
        }
        System.out.println(TopLevelClass.class.isMemberClass());
    }

    @Test
    public void getOutterClassInstance() throws NoSuchFieldException, IllegalAccessException {
        TopLevelClass tps = new TopLevelClass();
        InnerClass is = tps.getInnerClass();
        Field filed = is.getClass().getDeclaredField("this$0");
        //获取内部类对象持有的外部类实例
        TopLevelClass innerTps = (TopLevelClass)filed.get(is);
        //返回true，他们是同一个对象
        System.out.println(tps == innerTps);
    }

}
