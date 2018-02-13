package com.zwc.screenbroadcast;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

import com.zwc.screenbroadcast.entity.ScreenImage;
import com.zwc.screenbroadcast.entity.ScreenSize;

/**
 * 屏幕图像录制
 * TODO: 性能优化
 */
public class ScreenImageCapture {
    public ScreenImageCapture() throws Exception {
        ScreenImage screen = new ScreenImage();
        Robot robot = new Robot();
        Utils.backendFrameLoop("ScreenImageCapture", 30, () -> {
            try {
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                Rectangle screenRect = new Rectangle(screenSize);
                BufferedImage capture = robot.createScreenCapture(screenRect);
                try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                    ImageIO.write(capture, "jpeg", outputStream);
                    byte[] bytes = outputStream.toByteArray();

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

                    if (!theSame) {
                        screen.image = bytes;
                        Push.push(screen);
                    }
                }
            } catch (Exception ex) {
                Log.error(ex);
            }
        });
    }
}
