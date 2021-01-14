package com.bob.root.concrete.kfc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.ToIntFunction;

import com.alibaba.fastjson.JSON;

import com.google.common.io.Files;
import com.taobao.kfc.client.constant.DictLoadModeEnum;
import com.taobao.kfc.client.constant.EnvironmentEnum;
import com.taobao.kfc.client.constant.SerializationTypeEnum;
import com.taobao.kfc.client.dict.WordDict;
import com.taobao.kfc.client.lite.bean.KeywordMatchResult;
import com.taobao.kfc.client.lite.config.GlobalConfig;
import com.taobao.kfc.client.lite.service.LiteMergeSearchService;
import com.taobao.kfc.client.util.HessianSerializationUtils;
import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.StaticApplicationContext;

/**
 * @author wb-jjb318191
 * @create 2020-12-09 13:58
 */
public class KfcLoadBenchmarkTest {

    private LiteMergeSearchService liteMergeSearchService;

    @Before
    public void init() throws InterruptedException {
        System.gc();
        Thread.sleep(10000);
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setEnvironment(EnvironmentEnum.DAILY.getEnvironment());
        globalConfig.setLoadMode(DictLoadModeEnum.SERIALIZATION.getLoadMode());
        globalConfig.setSerializationType(SerializationTypeEnum.HESSIAN.getType());
        globalConfig.setProjectName("ffp-test1");
        globalConfig.setGroupName("default3");
        globalConfig.setPackageVersion("20200714");
        globalConfig.setReloadDelay(1);
        StaticApplicationContext applicationContext = new StaticApplicationContext();
        liteMergeSearchService = new LiteMergeSearchService();
        liteMergeSearchService.setApplicationContext(applicationContext);
        liteMergeSearchService.setGlobalConfig(globalConfig);
        liteMergeSearchService.afterPropertiesSet();
    }

    /**
     * 80976条关键词 中文词包
     *
     * @throws InterruptedException
     */
    @Test
    public void testSearch() throws InterruptedException, FileNotFoundException {
        System.gc();
        Thread.sleep(10000);
        for (int i = 0; i < 10; i++) {
            liteMergeSearchService.search("贱人撕国旗", Arrays.asList("A3415"));
        }
        Thread.sleep(10000);
        System.gc();
        System.out.println(liteMergeSearchService.search("贱人撕国旗", Arrays.asList("A3415")));
        System.out.println(ObjectSizeCalculator.getObjectSize(liteMergeSearchService));
        Thread.sleep(10000);
        WordDict wordDict = HessianSerializationUtils.deserialize(new FileInputStream("C:\\Users\\wb-jjb318191\\Desktop\\chinese_all.hessian"));
        System.out.println(ObjectSizeCalculator.getObjectSize(wordDict));
        Thread.sleep(10000);
        System.gc();
        Thread.sleep(1000 * 30);
    }

    @Test
    public void emptyStart() throws InterruptedException {
        System.gc();
        Thread.sleep(10000);
        System.out.println(ObjectSizeCalculator.getObjectSize(liteMergeSearchService));
        new Thread(){
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    liteMergeSearchService.search("贱人撕国旗", Arrays.asList("A3415"));
                }
            }
        }.start();
        new Thread(){
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    liteMergeSearchService.search("我就知道膜蛤今天是一个传言好天气", Arrays.asList("A3415"));
                }
            }
        }.start();
        System.out.println(ObjectSizeCalculator.getObjectSize(liteMergeSearchService));
        for (int i = 0; i < 10; i++) {
            liteMergeSearchService.search("我就知道膜蛤今天是一个传言好天气", Arrays.asList("A3415"));
        }
        System.out.println(ObjectSizeCalculator.getObjectSize(liteMergeSearchService));
        Thread.sleep(200000);
        System.gc();
        Thread.sleep(20000);
    }

    @Test
    public void introspectLines() throws Exception {
        Path path = Paths.get("C:\\Users\\wb-jjb318191\\Desktop\\chinese_all.txt");
        List<String> strings = Files.readLines(path.toFile(), Charset.forName("GB18030"));
        Set<String> keywords = new HashSet<>();
        for (int i = 0; i < strings.size(); i++) {
            String line = strings.get(i);
            String[] split = line.split("\t");
            boolean exists = keywords.add(split[0]);
            if (!exists) {
                throw new IllegalArgumentException(String.format("行{}已存在", i + 1));
            }
        }
        System.out.println(keywords.size());

        System.out.println(keywords.stream().mapToInt(new ToIntFunction<String>() {
            @SneakyThrows
            @Override
            public int applyAsInt(String value) {
                return value.getBytes("GB18030").length;
            }
        }).sum());
    }

    @Test
    public void hessionDeserialize() throws FileNotFoundException {

    }
}
