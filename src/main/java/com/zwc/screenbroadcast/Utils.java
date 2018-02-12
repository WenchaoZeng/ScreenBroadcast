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

    public static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void backendFrameLoop(
        String taskName,
        int framesPerSecond,
        Runnable action
    ) {
        backend(() -> {
            frameLoop(taskName, framesPerSecond, action);
        });
    }

    public static void frameLoop(
        String taskName,
        int framesPerSecond,
        Runnable action
    ) {
        int frameMilliseconds = 1000 / framesPerSecond;
        while (true) {
            long startMillisecond = System.currentTimeMillis();
            try {
                action.run();
            } catch (Exception ex) {
                Log.error(ex);
            }
            long actualFrameMilliseconds = System.currentTimeMillis() - startMillisecond;

            if (actualFrameMilliseconds < frameMilliseconds) {
                Utils.sleep(frameMilliseconds - actualFrameMilliseconds);
            } else if (Global.enableFrameLog){
                Log.info("%s 帧处理时间过多, 期望为: %d 毫秒, 实际为: %d 毫秒", taskName, frameMilliseconds, actualFrameMilliseconds);
            }
        }
    }
}
