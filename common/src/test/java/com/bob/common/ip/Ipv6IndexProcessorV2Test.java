package com.bob.common.ip;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.bob.common.utils.ip.IpGeoMetaInfo;
import com.bob.common.utils.ip.v6.Ipv6IndexProcessor;
import org.apache.commons.io.FileUtils;

/**
 * @author wb-jjb318191
 * @create 2020-04-13 14:30
 */
@SuppressWarnings("Duplicates")
public class Ipv6IndexProcessorV2Test {

    public static final String BASE_URL = "C:\\Users\\wb-jjb318191\\Desktop\\";

    //public static final String DAT_FILE_NAME = BASE_URL + "v6-utf8-index.dat";
    public static final String DAT_FILE_NAME = BASE_URL + "v6-utf8-index-v2.dat";

    public static final String DAT_ENCRYPT_FILE_NAME = BASE_URL + "v6-utf8-index-encrypt.dat";

    public static final String IP_FILE_NAME = BASE_URL + "ips.txt";

    private static final String IP_SOURCE_NAME = BASE_URL + "v6.txt";

    private static final int LOOP_TIMES = 2;

    private static final int LOOP_TIMES_THREE = 3;

    public static void main(String[] args) throws Exception {
        File txt = new File(IP_SOURCE_NAME);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(txt)));
        Ipv6IndexProcessor indexer = new Ipv6IndexProcessor();
        List<String> lines = FileUtils.readLines(txt, "utf-8");
        int k = 0;
        List<String> ips = new ArrayList<>();
        for (String s : lines) {
            String[] splits = s.split(",");
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
            if (k == 3961) {
                System.out.println("....");
            }
            indexer.index(new BigInteger(splits[0]).toString(), new BigInteger(splits[1]).toString(), text);
            ips.add(splits[0]);
            if (k % (1000 * 100) == 0) {
                System.out.println("写了" + k + "条数据");
            }
            k++;
        }
        reader.close();
        indexer.finish();
        File dat = new File(DAT_FILE_NAME);
        if (dat.exists()) {
            dat.delete();
        }
        FileUtils.writeByteArrayToFile(dat, indexer.getData(), 0, indexer.getContentOffset());
        File ipList = new File(BASE_URL + "ips.txt");
        if (ipList.exists()) {
            ipList.delete();
        }
        FileUtils.writeLines(ipList, ips);
    }

    /**
     * 生成文件meta信息
     *
     * @return Ipv6MetaInfo
     */
    private static IpGeoMetaInfo newIpv6MetaInfo() {
        IpGeoMetaInfo metaInfo = new IpGeoMetaInfo();
        metaInfo.setVersion(null);
        metaInfo.setChecksum(null);
        metaInfo.setGmtCreate(null);
        metaInfo.setIpversion(null);
        metaInfo.setExpireAt(null);
        metaInfo.setStoredProperties(new String[] {"country", "province", "city", "county", "isp"});
        return metaInfo;
    }

}
