package com.zwc.screenbroadcast;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

import com.zwc.screenbroadcast.entity.ScreenImage;

/**
 * 屏幕录制
 */
public class ScreenCapture {
    public ScreenCapture() {
        Utils.backend(() -> {
            ScreenImage screen = new ScreenImage();
            while (true) {

                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

                try {
                    Rectangle screenRect = new Rectangle(screenSize);
                    BufferedImage capture = new Robot().createScreenCapture(screenRect);
                    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                        ImageIO.write(capture, "jpeg", outputStream);
                        screen.image = outputStream.toByteArray();
                        Push.push(screen);
                    }
                } catch (Exception ex) {
                    Log.error(ex);
                }

                Utils.sleep(30);
            }
        });
    }
}
