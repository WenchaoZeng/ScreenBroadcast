package com.zwc.screenbroadcast;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 主入口函数
 */
public class ScreenBroadcast {

    public static void main(String[] args) throws Exception {
        Log.info("App version: %s", Global.version);
        new AppUI();
        new WebServer();
        new MouseCapture();
        new ScreenCapture();
        new Push();
    }

}
