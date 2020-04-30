package com.bob.common.ip;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import com.bob.common.utils.ip.IpGeoMetaInfo;
import com.bob.common.utils.ip.v6.Ipv6SearchProcessor;
import com.github.maltalex.ineter.base.IPv6Address;
import com.google.common.io.Files;
import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;
import org.apache.commons.io.FileUtils;

/**
 * @author wb-jjb318191
 * @create 2020-04-14 10:16
 */
@SuppressWarnings("Duplicates")
public class Ipv6SearchProcessorTest {

    public static void main(String[] args) throws Exception {
        testInOneThread();
    }

    private static void testInOneThread() throws Exception {
        System.out.println(System.getProperty("java.library.path"));
        String dat = Ipv6IndexProcessorTest.DAT_FILE_NAME;

        Ipv6SearchProcessor finder = new Ipv6SearchProcessor();
        finder.load(new File(dat), new IpGeoMetaInfo());
        System.out.println(ObjectSizeCalculator.getObjectSize(finder) / 1000);

        byte[] test = new BigInteger("39623477191446").toByteArray();
        int l = test.length;
        if (l < 16) {
            byte[] bytes = new byte[16];
            System.arraycopy(test, 0, bytes, 16 - l, l);
            test = bytes;
        }

        String value = new BigInteger(IPv6Address.of("0:0:0:0:0:2001:250:312").toArray()).toString();

        String ipv6 = IPv6Address.of(test).toString();
        System.out.println(ipv6);
        System.out.println(finder.search(ipv6));
        //System.out.println(finder.search("0:0:0:0:0:2600:1:912c"));

        String ipFile = Ipv6IndexProcessorTest.IP_FILE_NAME;
        List<String> ips = FileUtils.readLines(new File(ipFile), "UTF-8");
       /* List<byte[]> ipv6s = ips.stream().map(s -> {
            BigInteger bigInteger = new BigInteger(s);
            byte[] ip = bigInteger.toByteArray();
            int length = ip.length;
            if (length < 16) {
                byte[] bytes = new byte[16];
                System.arraycopy(ip, 0, bytes, 16 - length, length);
                ip = bytes;
            }
            System.out.println(IPv6Address.of(ip).toString());
            return ip;
        }).collect(Collectors.toList());*/
        List<String> ipv6s = ips.stream().map(s -> {
            BigInteger bigInteger = new BigInteger(s);
            byte[] ip = bigInteger.toByteArray();
            int length = ip.length;
            if (length < 16) {
                byte[] bytes = new byte[16];
                System.arraycopy(ip, 0, bytes, 16 - length, length);
                ip = bytes;
            }
            return IPv6Address.of(ip).toString();
        }).collect(Collectors.toList());

        System.gc();
        long t1 = System.currentTimeMillis();
        int k = 0;
        for (int i = 0; i < 1000 * 1000 * 100; i++) {
            String ip = ipv6s.get(k);
            /*if (k == 483103) {
                System.out.println("---");
            }*/
            String s = finder.search(ip);
            if (s == null) {
                System.out.println("------");
            }
            k++;
            if (k == ips.size()) {
                k = 0;
            }
        }
        System.out.println((System.currentTimeMillis() - t1) + "ms");
    }

    public static void testThreads() throws Exception {
        Ipv6SearchProcessor finder = new Ipv6SearchProcessor();
        finder.load(new File(Ipv6IndexProcessorTest.DAT_FILE_NAME), new IpGeoMetaInfo());

        List<String> ips = FileUtils.readLines(new File(Ipv6IndexProcessorTest.IP_FILE_NAME), "UTF-8");
        List<String> ipv6s = ips.stream().map(s -> {
            BigInteger bigInteger = new BigInteger(s);
            byte[] ip = bigInteger.toByteArray();
            int length = ip.length;
            if (length < 16) {
                byte[] bytes = new byte[16];
                System.arraycopy(ip, 0, bytes, 16 - length, length);
                ip = bytes;
            }
            return IPv6Address.of(ip).toString();
        }).collect(Collectors.toList());

        long t1 = System.currentTimeMillis();

        CountDownLatch latch = new CountDownLatch(4);

        int size = ipv6s.size();

        Thread thread1 = new Thread(() -> {
            int k = 0;
            for (int i = 0; i < 1000 * 1000 * 100; i++) {
                finder.search(ipv6s.get(k++));
                if (k == size) {
                    k = 0;
                }
            }
            latch.countDown();
        });
        Thread thread2 = new Thread(() -> {
            int k = 0;
            for (int i = 0; i < 1000 * 1000 * 100; i++) {
                finder.search(ipv6s.get(k++));
                if (k == size) {
                    k = 0;
                }
            }
            latch.countDown();
        });
        Thread thread3 = new Thread(() -> {
            int k = 0;
            for (int i = 0; i < 1000 * 1000 * 100; i++) {
                finder.search(ipv6s.get(k++));
                if (k == size) {
                    k = 0;
                }
            }
            latch.countDown();
        });
        Thread thread4 = new Thread(() -> {
            int k = 0;
            for (int i = 0; i < 1000 * 1000 * 100; i++) {
                //String ip = ips.get(k++);
                finder.search(ipv6s.get(k++));
                if (k == size) {
                    k = 0;
                }
            }
            latch.countDown();
        });
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        latch.await();

        System.out.println(System.currentTimeMillis() - t1);
        //System.out.println(ip);
        //System.out.println(result);
        //System.gc();
    }

    public static void testToByteArray() {
        System.out.println(new BigInteger(IPv6Address.of("0:0:0:0:0:2c0f:fe38:232a").toArray()).toString());
        Ipv6SearchProcessor processor = new Ipv6SearchProcessor();
        byte[] ip = processor.toByteArray("0:0:12:c::12:226");
        ip = processor.toByteArray("0:0:0:0:26:240e:e1:60ce");
        int length = ip.length;
        if (length < 16) {
            byte[] bytes = new byte[16];
            System.arraycopy(ip, 0, bytes, 16 - length, length);
            ip = bytes;
        }
        System.out.println(IPv6Address.of(ip).toString());
    }

    public static void convertContent() throws IOException {
        File file = new File("C:\\Users\\wb-jjb318191\\Desktop\\ips.txt");
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = Files.newReader(file, Charset.forName("UTF-8"));) {
            String line;
            while ((line = br.readLine()) != null) {
                byte[] data = new BigInteger(line).toByteArray();
                int l = data.length;
                if (l < 16) {
                    byte[] bytes = new byte[16];
                    System.arraycopy(data, 0, bytes, 16 - l, l);
                    data = bytes;
                }
                lines.add(IPv6Address.of(data).toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileUtils.writeLines(new File("C:\\Users\\wb-jjb318191\\Desktop\\ipv6s.txt"), lines);
    }

}
