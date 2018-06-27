package com.bob.root.concrete.javassist;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import org.junit.Test;
import org.springframework.beans.BeanUtils;

/**
 * 测试通过Javassist字节码框架动态生成Class
 *
 * @author wb-jjb318191
 * @create 2018-04-24 15:29
 */
public class CompilerByJavassist {

    private String className = "User";

    private String rootDir = "D:";

    @Test
    public void testInvokeJavassistMethod() throws Exception {
        CtClass ctClass = buildJavassistClass();
        Object javassistObject = BeanUtils.instantiate(ctClass.toClass());
        Method method = javassistObject.getClass().getDeclaredMethod("print", new Class[] {});
        method.invoke(javassistObject, null);
    }

    /**
     * @throws Exception
     */
    @Test
    public void persistenceJavassistClass() throws Exception {
        CtClass ctClass = buildJavassistClass();
        // 把生成的class写入到文件中
        byte[] byteArr = ctClass.toBytecode();
        File file = new File(classNameToPath(className));
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(byteArr);
        fos.close();
    }

    @Test
    public void loadJavassistClass() throws ClassNotFoundException {
        ClassLoader classLoader = new ClassLoader() {

            protected Class<?> findClass(String name) throws ClassNotFoundException {

                byte[] classData = getClassData(name);
                if (classData == null) {
                    throw new ClassNotFoundException();
                } else {
                    return defineClass(name, classData, 0, classData.length);
                }
            }
        };
        Class clazz = classLoader.loadClass("User");
        System.out.println(clazz.getName());
    }

    private byte[] getClassData(String className) {
        String path = classNameToPath(className);
        try {
            InputStream ins = new FileInputStream(path);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int bufferSize = 4096;
            byte[] buffer = new byte[bufferSize];
            int bytesNumRead = 0;
            while ((bytesNumRead = ins.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesNumRead);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String classNameToPath(String className) {
        return rootDir + File.separatorChar
            + className.replace('.', File.separatorChar) + ".class";
    }

    /**
     * 测试通过Javassist生成Java Class
     *
     * @throws Exception
     */
    public CtClass buildJavassistClass() throws Exception {
        // ClassPool: CtClass对象容器
        ClassPool pool = ClassPool.getDefault();

        // 通过ClassPool生成一个public的User类
        CtClass ctClass = pool.makeClass("User");

        // 添加属性
        // 1. 添加属性private int id;
        CtField idField = new CtField(pool.getCtClass("int"), "id", ctClass);
        idField.setModifiers(Modifier.PRIVATE);
        ctClass.addField(idField);

        // 2.添加属性private String username
        CtField nameField = new CtField(pool.get("java.lang.String"), "username", ctClass);
        nameField.setModifiers(Modifier.PRIVATE);
        ctClass.addField(nameField);

        // 添加setter/getter方法
        ctClass.addMethod(CtNewMethod.getter("getId", idField));
        ctClass.addMethod(CtNewMethod.setter("setId", idField));
        ctClass.addMethod(CtNewMethod.getter("getUsername", nameField));
        ctClass.addMethod(CtNewMethod.setter("setUsername", nameField));

        // 添加构造函数
        CtConstructor ctConstructor = new CtConstructor(new CtClass[] {}, ctClass);
        // 添加构造函数方法体
        StringBuffer sb = new StringBuffer();
        sb.append("{\n").append("this.id = 27;\n").append("this.username=\"carl\";\n}");
        ctConstructor.setBody(sb.toString());
        ctClass.addConstructor(ctConstructor);

        // 添加自定义方法
        CtMethod printMethod = new CtMethod(CtClass.voidType, "print", new CtClass[] {}, ctClass);
        printMethod.setModifiers(Modifier.PUBLIC);
        StringBuffer printSb = new StringBuffer();
        printSb.append("{\nSystem.out.println(\"begin!\");\n")
            .append("System.out.println(id);\n")
            .append("System.out.println(username);\n")
            .append("System.out.println(\"end!\");\n")
            .append("}");
        printMethod.setBody(printSb.toString());
        ctClass.addMethod(printMethod);
        return ctClass;

    }

}
