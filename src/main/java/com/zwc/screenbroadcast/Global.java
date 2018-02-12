package com.zwc.screenbroadcast;

/**
 * 全局存储区
 */
public class Global {

    /**
     * 软件版本号
     */
    public static String version = "0.1.0-dev";

    /**
     * 网页端主入口
     */
    public static String url;

    /**
     * 屏幕信息
     */
    public static ScreenInfo screenInfo = new ScreenInfo();
    public static class ScreenInfo {
        public int width;
        public int height;
        public byte[] image = new byte[0];
    }

    /**
     * 日志
     */
    public static StringBuilder logs = new StringBuilder();
}
