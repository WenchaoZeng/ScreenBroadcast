package com.zwc.screenbroadcast;

import java.awt.*;

import javax.swing.*;

/**
 * app端显示和交互
 */
public class AppUI {
    public AppUI() throws Exception {

        // 窗口
        JFrame frame = new JFrame("屏幕分享");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 100);
        frame.setLocation(700, 400);

        // 文本框
        TextField textField = new TextField("启动中");
        frame.getContentPane().add(textField);

        frame.setVisible(true);

        // 显示URL
        new Thread(() -> {
            while (Global.url == null) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                }

            }
            textField.setText(Global.url);
            frame.repaint();
        }).start();
    }
}