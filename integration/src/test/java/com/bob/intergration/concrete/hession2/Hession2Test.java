package com.bob.intergration.concrete.hession2;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import com.caucho.hessian.io.Hessian2Output;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;

/**
 * @author wb-jjb318191
 * @create 2018-07-26 9:20
 */
public class Hession2Test {

    private Hession2Entity entity;

    @Before
    public void init() {
        entity = new Hession2Entity();
        entity.setId(1L);
        entity.setName("lanboal");
        entity.setAge(29);
    }

    @Test
    public void testHession2Serialize() throws IOException {
        byte[] bytes = serialize(entity);
        System.out.println("通过Hession2序列化后的大小为:[" + bytes.length + "]字节");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            char c = (char)bytes[i];
            if (c > -1 && c < 128) {
                sb.append(c);
            }
        }
        System.out.println(sb.toString());
    }

    @Test
    public void testJdkSerialize() throws IOException {
        FileOutputStream fos = new FileOutputStream("C:\\Users\\wb-jjb318191\\Desktop\\jdk_serialize.txt");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(entity);
        fos.close();
    }

    @Test
    public void testJsonSerialize() {
        String json = new Gson().toJson(entity);
        System.out.println(String.format("通过Json序列化后的字节长度为[%s]", json.getBytes().length));
    }

    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Hessian2Output ho = new Hessian2Output(os);
        byte[] cc = null;
        try {
            if (obj == null) {
                throw new NullPointerException();
            }
            ho.writeObject(obj);
            ho.flushBuffer();
            cc = os.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ho.close();
        }
        return cc;

    }

}
