package com.zwc.screenbroadcast;

import java.awt.*;

/**
 * 鼠标捕捉
 */
public class MouseCapture {
    public MouseCapture() {
        Utils.backend(() -> {
            while (true) {
                Global.MouseInfo mouseInfo = new Global.MouseInfo();

                Point point = MouseInfo.getPointerInfo().getLocation();
                mouseInfo.x = point.x;
                mouseInfo.y = point.y;

                Global.mouseInfo = mouseInfo;

                Utils.sleep(30);
            }

        });
    }
}
