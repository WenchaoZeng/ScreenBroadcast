package com.zwc.screenbroadcast;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;

import com.zwc.screenbroadcast.entity.ScreenImage;
import com.zwc.screenbroadcast.entity.ScreenSize;

/**
 * 屏幕图像录制
 * TODO: 性能优化
 */
public class ScreenImageCapture {

    final int framesPerSecond = 30;
    ScreenImage screen = new ScreenImage();

    Robot robot = new Robot();
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Rectangle screenRect = new Rectangle(screenSize);

    public ScreenImageCapture() throws Exception {

        //planJobs();

        Utils.backendFrameLoop("ScreenImageCapture", framesPerSecond, ()  -> {
            captureScreen();
        });

    }

    void planJobs() {
        final int frameMilliseconds = 1000 / framesPerSecond;
        int index = 0;
        while (index < 30) {
            planJob(index * frameMilliseconds, () -> captureScreen());
            index++;
        }

        planJob(index * frameMilliseconds, () -> planJobs());
    }

    Executor executor = null;
    void planJob(long sleepMilliseconds, Runnable action) {
        if (executor == null) {
            executor = Executors.newFixedThreadPool(60);
        }
        executor.execute(() -> {
            Utils.sleep(sleepMilliseconds);
            action.run();
        });
    }

    void captureScreen() {
        try {

            long frameTime = System.currentTimeMillis();

            BufferedImage capture = robot.createScreenCapture(screenRect);
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                ImageIO.write(capture, "jpeg", outputStream);
                byte[] bytes = outputStream.toByteArray();

                synchronized (screen) {

                    // 保证时间顺序不错乱
                    if (frameTime <= screen.frameTime) {
                        Log.error("帧延时大于 %d 毫秒, 放弃该截图", (screen.frameTime - frameTime));
                        return;
                    }

                    // 防重复
                    boolean theSame = true;
                    if (bytes.length != screen.image.length) {
                        theSame = false;
                    }
                    if (theSame) {
                        for (int index = 0; index < bytes.length && index < screen.image.length; ++index) {
                            if (bytes[index] != screen.image[index]) {
                                theSame = false;
                                break;
                            }
                        }
                    }
                    if (theSame) {
                        return;
                    }

                    // 推送
                    screen.frameTime = frameTime;
                    screen.image = bytes;
                    Push.push(screen);
                }
            }
        } catch (Exception ex) {
            Log.error(ex);
        }
    }

    // screencapture -m -r -t gif ./screen.gif
    void captureScreen2() {
        try {
            String path = "/Users/reid.zeng/Desktop/yit/gitlab/ScreenBroadcast/captureScreen2_screen.gif";

            // 调用系统命令截图
            Process p = Runtime.getRuntime().exec("screencapture -m -r -t gif " + path);
            p.waitFor();

            // 读取图片
            screen.image = Files.readAllBytes(Paths.get(path));
            Push.push(screen);

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
