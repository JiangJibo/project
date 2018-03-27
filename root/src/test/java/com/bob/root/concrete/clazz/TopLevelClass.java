package com.bob.root.concrete.clazz;

/**
 * @since 2017年7月26日 下午3:07:58
 * @version $Id$
 * @author JiangJibo
 *
 */
public class TopLevelClass {

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public InnerClass getInnerClass(){
        return new InnerClass();
    }

    public NestedClass getNestedClass(){
        return new NestedClass();
    }

    public static void print(){
        System.out.println("调用静态方法");
    }

    /**
     * 内部类
     */
    public class InnerClass {

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String toString(){
            //内部类可以使用外部类的属性和方法
            return id + name;
        }
    }

    /**
     * 嵌套类,静态内部类
     */
    public static class NestedClass {

        private int age;

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public void print(){
            //嵌套类智能使用外部类的静态成员及方法
            TopLevelClass.print();
        }
    }

}
