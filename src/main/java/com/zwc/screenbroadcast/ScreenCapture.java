package com.zwc.screenbroadcast;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

/**
 * 屏幕录制
 */
public class ScreenCapture {
    public ScreenCapture() {
        Utils.backend(() -> {
            while (true) {
                Global.ScreenInfo screenInfo = new Global.ScreenInfo();

                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                screenInfo.width = screenSize.width;
                screenInfo.height = screenSize.height;

                try {
                    Rectangle screenRect = new Rectangle(screenSize);
                    BufferedImage capture = new Robot().createScreenCapture(screenRect);
                    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                        ImageIO.write(capture, "jpeg", outputStream);
                        screenInfo.image = outputStream.toByteArray();
                    }
                } catch (Exception ex) {
                    Log.error(ex);
                }

                Global.screenInfo = screenInfo;

                Utils.sleep(30);
            }
        });
    }
}
