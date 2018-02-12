package com.zwc.screenbroadcast;

import java.awt.*;

import com.zwc.screenbroadcast.entity.MouseLocation;

/**
 * 鼠标捕捉
 * TODO: 捕捉鼠标样式
 */
public class MouseCapture {
    public MouseCapture() {
        Utils.backend(() -> {
            MouseLocation mouse = new MouseLocation();
            while (true) {
                Point point = MouseInfo.getPointerInfo().getLocation();
                if (mouse.x != point.x
                    || mouse.y != point.y) {
                    mouse.x = point.x;
                    mouse.y = point.y;
                    Push.push(mouse);
                }

                Utils.sleep(30);
            }
        });
    }
}
