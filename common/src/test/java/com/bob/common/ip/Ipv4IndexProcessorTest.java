package com.bob.common.ip;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.bob.common.utils.ip.v4.Ipv4IndexProcessor;
import org.apache.commons.io.FileUtils;

/**
 * @author wb-jjb318191
 * @create 2020-03-23 9:55
 */
public class Ipv4IndexProcessorTest {

    private static final int LOOP_TIMES = 2;

    private static final int LOOP_TIMES_THREE = 3;

    public static void main(String[] args) throws Exception {
        File txt = new File("C:\\Users\\wb-jjb318191\\Desktop\\ipv4.txt");
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
                text = " | | | | ";
            } else {
                StringBuilder sb = new StringBuilder();
                for (int i = LOOP_TIMES; i < splits.length; i++) {
                    sb.append(splits[i]).append("|");
                }
                text = sb.toString();
                text = text.substring(0, text.length() - 1);
            }
            indexer.processContent(splits[1], text);
            ips.add(splits[0]);
            if (k % (1000 * 100) == 0) {
                System.out.println("写了" + k + "条数据");
            }
            k++;
        }
        reader.close();
        indexer.finish();
        File dat = new File("C:\\Users\\wb-jjb318191\\Desktop\\ipv4-utf8-index.dat");
        if (dat.exists()) {
            dat.delete();
        }
        FileUtils.writeByteArrayToFile(dat, indexer.getData(), 0, indexer.getContentOffset());
        File ipList = new File("C:\\Users\\wb-jjb318191\\Desktop\\ipv4-ip.txt");
        if (ipList.exists()) {
            ipList.delete();
        }
        FileUtils.writeLines(ipList, ips);
    }

    private static int calculateIpSize() throws IOException {
        File txt = new File("C:\\Users\\wb-jjb318191\\Desktop\\ipv4.txt");
        List<String> lines = FileUtils.readLines(txt, "utf-8");
        return lines.size();
    }

    /**
     * 计算IP段的int值
     *
     * @param ip         IP
     * @param startIndex Start
     * @param endIndex   End
     * @return int
     */
    private int calculateIpSegmentInt(String ip, int startIndex, int endIndex) {
        int num = 0;
        for (int i = startIndex; i <= endIndex; i++) {
            int radix = i == endIndex ? 1 : i == endIndex - 1 ? 10 : 100;
            num += radix * (ip.charAt(i) - 48);
        }
        return num;
    }

}
