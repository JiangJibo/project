package com.bob.root.concrete.clazz;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;

import com.bob.root.config.entity.RootUser;
import lombok.SneakyThrows;
import org.junit.Test;
import sun.misc.Unsafe;

/**
 * @author wb-jjb318191
 * @create 2020-05-18 9:28
 */
public class UnsafeTest {

    private static Unsafe unsafe;

    private volatile int x;

    static {
        try {
            Field getUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            getUnsafe.setAccessible(true);
            unsafe = (Unsafe)getUnsafe.get(null);
        } catch (Exception ex) { throw new Error(ex); }
    }

    @Test
    public void i(){
        System.out.println(16);
    }

    @Test
    @SneakyThrows
    public void testFiledOffset() {
        long idOffset = unsafe.objectFieldOffset(RootUser.class.getDeclaredField("name"));
        RootUser user = (RootUser)unsafe.allocateInstance(RootUser.class);
        unsafe.putObject(user, idOffset, "new value");
        unsafe.putObject(user, unsafe.objectFieldOffset(RootUser.class.getDeclaredField("id")), new Integer(1));
        System.out.println(user.getId());
    }

    @Test
    @SneakyThrows
    public void testUnsafe1() {
        /**
         * 对象操作
         * 实例化Data
         *
         * 可以通过类的class对象创建类对象（allocateInstance），获取对象属性的偏移量（objectFieldOffset）
         * ，通过偏移量设置对象的值（putObject）
         *
         * 对象的反序列化
         * 当使用框架反序列化或者构建对象时，会假设从已存在的对象中重建，你期望使用反射来调用类的设置函数，
         * 或者更准确一点是能直接设置内部字段甚至是final字段的函数。问题是你想创建一个对象的实例，
         * 但你实际上又不需要构造函数，因为它可能会使问题更加困难而且会有副作用。
         *
         */
        //调用allocateInstance函数避免了在我们不需要构造函数的时候却调用它
        Data data = (Data)unsafe.allocateInstance(Data.class);
        data.setId(1L);
        data.setName("unsafe");
        System.out.println(data);

        //返回成员属性在内存中的地址相对于对象内存地址的偏移量
        Field nameField = Data.class.getDeclaredField("name");
        long fieldOffset = unsafe.objectFieldOffset(nameField);
        //putLong，putInt，putDouble，putChar，putObject等方法，直接修改内存数据（可以越过访问权限）
        unsafe.putObject(data, fieldOffset, "这是新的值");
        System.out.println(data.getName());

        /**
         * 我们可以在运行时创建一个类，比如从已编译的.class文件中。将类内容读取为字节数组，
         * 并正确地传递给defineClass方法；当你必须动态创建类，而现有代码中有一些代理， 这是很有用的
         */
        File file = new File(
            "C:\\workspace\\idea2\\disruptor\\target\\classes\\com\\onyx\\distruptor\\test\\Data.class");
        FileInputStream input = new FileInputStream(file);
        byte[] content = new byte[(int)file.length()];
        input.read(content);
        Class c = unsafe.defineClass(null, content, 0, content.length, null, null);
        c.getMethod("getId").invoke(c.newInstance(), null);

        /**
         * 内存操作
         * 可以在Java内存区域中分配内存（allocateMemory），设置内存（setMemory，用于初始化），
         * 在指定的内存位置中设置值（putInt\putBoolean\putDouble等基本类型）
         */
        //分配一个8byte的内存
        long address = unsafe.allocateMemory(8L);
        //初始化内存填充1
        unsafe.setMemory(address, 8L, (byte)1);
        //测试输出
        System.out.println("add byte to memory:" + unsafe.getInt(address));
        //设置0-3 4个byte为0x7fffffff
        unsafe.putInt(address, 0x7fffffff);
        //设置4-7 4个byte为0x80000000
        unsafe.putInt(address + 4, 0x80000000);
        //int占用4byte
        System.out.println("add byte to memory:" + unsafe.getInt(address));
        System.out.println("add byte to memory:" + unsafe.getInt(address + 4));
    }

    @Test
    public void test1() {
        int y = 2;
        x = 1228 * 2;
        int z = 4;
    }

    @Test
    public void test2() {
        x = 1228 << 1;
    }

    @lombok.Data
    public class Data {

        private long id;
        private String name;

    }

}
