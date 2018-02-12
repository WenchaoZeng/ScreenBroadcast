package com.zwc.screenbroadcast;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 日志
 */
public class Log {

    public static void error(Exception ex) {
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        error(sw.toString());
    }

    public static void info(String msg, Object... params) {
        msg = "[INFO] " + formatMsg(msg, params);
        System.out.println(msg);
        synchronized (Global.logs) {
            Global.logs.append(msg + "\n");
        }
    }

    public static void error(String msg, Object... params) {
        msg = "[ERROR] " + formatMsg(msg, params);
        System.err.println(msg);
        synchronized (Global.logs) {
            Global.logs.append(msg + "\n");
        }
    }

    static String formatMsg(String msg, Object... params) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        msg = dateFormat.format(new Date()) + ": " + String.format(msg, params);
        return msg;
    }

}
