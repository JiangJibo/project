package com.bob.root.concrete.file;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

/**
 * @author wb-jjb318191
 * @create 2019-10-25 10:21
 */
public class FileOSCacheTest {

    @Test
    public void testWrite() throws IOException {
        File file = new File("C:\\Users\\wb-jjb318191\\Desktop\\bob.txt");
        FileUtils.writeLines(file, Arrays.asList("sadfasdfasdfas"));
        List<String> lines = FileUtils.readLines(file, "UTF-8");
        System.out.println("书写成功！");
    }



}
