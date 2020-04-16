package com.bob.root.concrete.nio;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.junit.Test;

/**
 * @author wb-jjb318191
 * @create 2020-04-16 16:01
 */
public class FilesTest {

    @Test
    public void testOpenOption() throws IOException {
        Path path = Paths.get("C:\\Users\\wb-jjb318191\\Desktop\\test.txt");
        BufferedWriter bw = Files.newBufferedWriter(path, StandardOpenOption.TRUNCATE_EXISTING);
        bw.write("");
    }

    @Test
    public void testTempFile() throws IOException {
        Path parent = Paths.get("C:\\Users\\wb-jjb318191\\Desktop");
        Path path = Files.createTempFile(parent, "demo", ".txt");
        BufferedWriter bw = Files.newBufferedWriter(path,StandardOpenOption.DELETE_ON_CLOSE);
        bw.write("ssssssssssssss");
        bw.flush();
        System.out.println(Files.newBufferedReader(path).readLine());
    }
}
