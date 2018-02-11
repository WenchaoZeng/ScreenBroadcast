package com.zwc.screenbroadcast;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 新线程
 * TODO: 日志收集
 */
public class Utils {

    public static Thread backend(Runnable action) {
        Thread thread = new Thread(action);
        thread.start();
        return thread;
    }

    public static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void print(Exception ex) {
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        printError(sw.toString());
    }

    public static void print(String msg, Object... params) {
        msg = "[INFO] " + formatMsg(msg, params);
        System.out.println(msg);
        synchronized (Global.logs) {
            Global.logs.append(msg);
        }
    }

    public static void printError(String msg, Object... params) {
        msg = "[ERROR] " + formatMsg(msg, params);
        System.err.println(msg);
        synchronized (Global.logs) {
            Global.logs.append(msg);
        }
    }

    static String formatMsg(String msg, Object... params) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        msg = dateFormat.format(new Date()) + ": " + String.format(msg, params);
        return msg;
    }
}
