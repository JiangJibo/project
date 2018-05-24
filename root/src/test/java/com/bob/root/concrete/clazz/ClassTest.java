package com.bob.root.concrete.clazz;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

/**
 * @author JiangJibo
 * @version $Id$
 * @since 2017年7月26日 下午3:07:49
 */
public class ClassTest {

    public void voidMethod() {
        System.out.println("执行Void方法");
    }

    @Test
    public void testGetVoidMethodReturn() {
        Method method = ReflectionUtils.findMethod(ClassTest.class, "voidMethod");
        Object obj = ReflectionUtils.invokeMethod(method, new ClassTest());
        System.out.println(obj);
    }

    @Test
    public void testGetInnerClass() {
        Class<?>[] classes = TopLevelClass.class.getDeclaredClasses();
        for (Class<?> clazz : classes) {
            System.out.println(
                "Class.Name = " + clazz.getName() + ", Class.SimpleName = " + clazz.getSimpleName() + ", Class.Package = " + clazz.getPackage().getName());
        }
    }

    @Test
    public void getMostSpecificMethod() throws Exception {
        Method orignalMethod = Handler.class.getMethod("handler");
        Method targetMethod = ClassUtils.getMostSpecificMethod(orignalMethod, TopLevelClass.class);
        System.out.println("orignalMethod:" + orignalMethod + " , targetMethod:" + targetMethod);
        TopLevelClass entity = new TopLevelClass();
        orignalMethod.invoke(entity);
        targetMethod.invoke(entity);
    }

    @Test
    public void testGetInetAdress() throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getLocalHost();
        System.out.println(inetAddress.getCanonicalHostName());
    }

    @Test
    public void testArrayList() {
        List<String> list = new ArrayList<String>();
        list.add(null);
        list.add(null);
        System.out.println(list.size());
        System.out.println(list.toString());
    }

    /**
     * 测试多层三目运算符反悔空指针异常
     */
    @Test
    public void test3m() {
        String s1 = "bb";
        String s2 = "aa";
        Integer result = (s1 == null && s2 == null) ? 0 : s1 == null ? -1 : s2 == null ? 1 : null;
        System.out.println(result);
    }

    @Test
    public void testDefaultMethod() {
        checkMethodIsDefault(TopLevelClass.class, "toString", new Class[0]);
        checkMethodIsDefault(TopLevelClass.class, "getId", new Class[0]);
        checkMethodIsDefault(Handler.class, "handler", new Class[0]);
        checkMethodIsDefault(Handler.class, "defaultMethod", new Class[0]);
    }

    private void checkMethodIsDefault(Class<?> clazz, String methodName, Class... args) {
        Method method = ReflectionUtils.findMethod(clazz, methodName, args);
        System.out.println(method.isDefault());
    }

    @Test
    public void testInputStream() throws IOException {
        FileInputStream fis = new FileInputStream(new File("C:\\Users\\wb-jjb318191\\Desktop\\空间单元导入模板.xls"));
        byte[] bytes = new byte[fis.available()];
        fis.read(bytes, 0, fis.available());
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        System.out.println(byteArrayInputStream.available());
    }

    private Converter<String, Integer> converter = new Converter<String, Integer>() {
        @Override
        public Integer convert(String source) {
            return Integer.valueOf(source);
        }
    };

    @Test
    public void testGetReturnType() {
        Converter<String, Integer> converter = new Converter<String, Integer>() {
            @Override
            public Integer convert(String source) {
                return Integer.valueOf(source);
            }
        };
        Class<?> clazz = ResolvableType.forClass(Converter.class, converter.getClass()).resolveGeneric(1);
        clazz = GenericTypeResolver.resolveTypeArguments(converter.getClass(), Converter.class)[0];
    }

    @Test
    public void checkNumber() {
        String string = "111 ";
        Double dou = 0.0d;
        char[] chars = ((String)string).toCharArray();
        for (char c : chars) {
            if (!Character.isDigit(c)) {
                return;
            }
        }
        dou = Double.valueOf(string);
        System.out.println(dou);
    }

    @Test
    public void testStringSplit() {
        String str = "asdfaa,";
        String[] strs = str.split(",");
        System.out.println(strs);
    }

    @Test
    public void testNullInstanceOf() {
        System.out.println(null instanceof Date);
    }

}
