package com.bob.common.ip;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import com.bob.common.utils.ip.Ipv4IndexProcessor;
import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

/**
 * @author wb-jjb318191
 * @create 2020-03-23 9:55
 */
public class IpV4IndexProcessorTest {

    @Test
    public void testIndex() throws Exception {
        File txt = new File("C:\\Users\\wb-jjb318191\\Desktop\\ip.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(txt)));
        Ipv4IndexProcessor indexer = new Ipv4IndexProcessor(calculateIpSize());
        System.gc();
        List<String> ips = new ArrayList<>();
        int k = 0;
        String line;
        while ((line = reader.readLine()) != null) {
            String[] splits = line.split(",");
            String text;
            if (splits.length == 2) {
                text = " ";
            } else {
                StringBuilder sb = new StringBuilder();
                for (int i = 2; i < splits.length; i++) {
                    sb.append(splits[i]).append("|");
                }
                text = sb.toString();
                text = text.substring(0, text.length() - 1);
            }
            indexer.indexIpInfo(splits[0], splits[1], text);
            ips.add(splits[0]);
            if (k % (1000 * 100) == 0) {
                System.out.println("写了" + k + "条数据");
            }
            k++;
        }
        reader.close();
        indexer.finishProcessing();
        File dat = new File("C:\\Users\\wb-jjb318191\\Desktop\\ipv4-utf8-index.dat");
        if (dat.exists()) {
            dat.delete();
        }
        indexer.flushData(dat.getPath());
        File ipList = new File("C:\\Users\\wb-jjb318191\\Desktop\\ips.txt");
        if (ipList.exists()) {
            ipList.delete();
        }
        FileUtils.writeLines(ipList, ips);
    }

    private int calculateIpSize() throws IOException {
        File txt = new File("C:\\Users\\wb-jjb318191\\Desktop\\ip.txt");
        List<String> lines = FileUtils.readLines(txt, "utf-8");
        return lines.size();
    }

    @Test
    public void sortIpInfo() throws IOException {
        File txt = new File("C:\\Users\\wb-jjb318191\\Desktop\\ip.txt");
        List<String> lines = FileUtils.readLines(txt, "utf-8");
        Comparator<String> comparator = (o1, o2) -> {
            String ip1 = o1.split(",")[0];
            String ip2 = o1.split(",")[0];
            return calculateIpLong(ip1) - calculateIpLong(ip2) > 0 ? 1 : -1;
        };
        Collections.sort(lines, comparator);
        File newIp = new File("C:\\Users\\wb-jjb318191\\Desktop\\newip.txt");
        if (newIp.exists()) {
            newIp.delete();
        }
        FileUtils.writeLines(newIp, lines);
    }

    /**
     * 计算ip的long值
     *
     * @param ip
     * @return
     */
    private long calculateIpLong(String ip) {
        long result = 0;
        int num, dot = 0, i = 0;
        do {
            int dotIndex = ip.indexOf(".", dot) - 1;
            if (dotIndex >= 0) {
                num = calculateIpSegmentInt(ip, dot, dotIndex);
            }
            // 最后一段
            else {
                num = calculateIpSegmentInt(ip, ip.lastIndexOf(".") + 1, ip.length() - 1);
            }
            result <<= 8;
            result |= num & 0xff;
            dot = dotIndex + 2;
            if (i++ == 3) {
                return result;
            }
        } while (true);
    }

    /**
     * 计算IP段的int值
     *
     * @param ip
     * @param startIndex
     * @param endIndex
     * @return
     */
    private int calculateIpSegmentInt(String ip, int startIndex, int endIndex) {
        int num = 0;
        for (int i = startIndex; i <= endIndex; i++) {
            int radix = i == endIndex ? 1 : i == endIndex - 1 ? 10 : 100;
            num += radix * (ip.charAt(i) - 48);
        }
        return num;
    }

    @Test
    public void testTreeSetSize() {
        System.out.println(" ".length());
        TreeSet<String> treeSet = new TreeSet<>();
        treeSet.add("a");
        treeSet.add("h");
        treeSet.add("l");
        treeSet.add("o");
        treeSet.add("e");
        treeSet.add("m");
        treeSet.add("x");
        treeSet.add("z");
        System.out.println(ObjectSizeCalculator.getObjectSize(new char[] {'a', 'a', 'a',}));
    }

}
