package com.zwc.screenbroadcast;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

import com.zwc.screenbroadcast.entity.ScreenImage;
import com.zwc.screenbroadcast.entity.ScreenSize;

/**
 * 屏幕录制
 */
public class ScreenCapture {
    public ScreenCapture() {
        Utils.backend(() -> {
            ScreenImage screen = new ScreenImage();
            ScreenSize size = new ScreenSize();
            while (true) {

                // 屏幕尺寸
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                if (size.width != screenSize.width || size.height != screenSize.height) {
                    size.width = screenSize.width;
                    size.height = screenSize.height;
                    Push.push(size);
                }


                // 屏幕图像
                try {
                    Rectangle screenRect = new Rectangle(screenSize);
                    BufferedImage capture = new Robot().createScreenCapture(screenRect);
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

                Utils.sleep(30);
            }
        });
    }
}
