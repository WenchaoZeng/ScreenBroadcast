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
     * 日志
     */
    public static StringBuilder logs = new StringBuilder();

    public static boolean enablePushLog = false;
    public static boolean enableFrameLog = false;
}
