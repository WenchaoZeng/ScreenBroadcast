package com.zwc.screenbroadcast;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 新线程
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
}
