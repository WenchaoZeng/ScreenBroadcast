package com.zwc.screenbroadcast;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 主入口函数
 * TODO: 固定帧率, 而不是直接sleep一个固定值
 */
public class Main {

    public static void main(String[] args) throws Exception {
        Log.info("App version: %s", Global.version);
        new AppUI();
        new WebServer();
        new MouseCapture();
        new Push();
    }

}
