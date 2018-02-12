package com.zwc.screenbroadcast;

/**
 * 全局存储区
 */
public class Global {

    /**
     * 网页端主入口
     */
    public static String url;

    /**
     * 鼠标信息
     */
    public static MouseInfo mouseInfo = new MouseInfo();
    public static class MouseInfo {
        public int x;
        public int y;
    }

    /**
     * 日志
     */
    public static StringBuilder logs = new StringBuilder();
}
