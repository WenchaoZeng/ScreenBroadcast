package com.zwc.screenbroadcast;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 主入口函数
 */
public class Main {

    public static void main(String[] args) throws Exception {

        System.out.println("Hello");

        Files.write(Paths.get("/Users/reid.zeng/Desktop/yit/gitlab/ScreenBroadcast/target/release/test.txt"), "Hello world".getBytes());
    }

}
