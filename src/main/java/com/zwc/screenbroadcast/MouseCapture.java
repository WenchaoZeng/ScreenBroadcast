package com.zwc.screenbroadcast;

import java.awt.*;

import com.zwc.screenbroadcast.entity.MouseLocation;

/**
 * 鼠标捕捉
 * TODO: 捕捉鼠标样式
 */
public class MouseCapture {
    public MouseCapture() {
        MouseLocation mouse = new MouseLocation();
        Utils.backendFrameLoop("MouseCapture", 30, () -> {
            Point point = MouseInfo.getPointerInfo().getLocation();
            if (mouse.x != point.x
                || mouse.y != point.y) {
                mouse.x = point.x;
                mouse.y = point.y;
                Push.push(mouse);
            }
        });
    }
}
