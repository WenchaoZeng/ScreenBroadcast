package com.zwc.screenbroadcast;

/**
 * 主入口函数
 * TODO: 换个更加恰当的app图标
 */
public class ScreenBroadcast {

    public static void main(String[] args) throws Exception {
        Log.info("App version: %s", Global.version);
        new AppUI();
        new ScreenSizeCapture();
        new MouseCapture();
        new ScreenImageCapture();
        new Push();
        new WebServer();
    }

}
