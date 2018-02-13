package com.zwc.screenbroadcast;

import java.awt.*;

import com.zwc.screenbroadcast.entity.ScreenSize;

/**
 * 屏幕尺寸捕捉
 */
public class ScreenSizeCapture {
    public ScreenSizeCapture() {
        ScreenSize size = new ScreenSize();
        Utils.backendFrameLoop("ScreenSizeCapture", 30, () -> {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            if (size.width != screenSize.width || size.height != screenSize.height) {
                size.width = screenSize.width;
                size.height = screenSize.height;
                Push.push(size);
            }
        });
    }
}
