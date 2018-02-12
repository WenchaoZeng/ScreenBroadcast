package com.zwc.screenbroadcast;

import java.awt.*;

import javax.swing.*;

/**
 * app端显示和交互
 * TODO: 界面美化, 显示复制url按钮
 * TODO: 支持多次查看日志
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

        // 日志显示
        textField.addTextListener(e -> {
            if (textField.getText().equals("logs")) {
                textField.setText("");
                frame.getContentPane().remove(textField);

                TextArea textArea;
                synchronized(Global.logs) {
                    textArea = new TextArea(Global.logs.toString());
                }
                frame.getContentPane().add(textArea);
                frame.revalidate();
                frame.repaint();
            }
        });

        // 显示URL
        Utils.backend(() -> {
            while (Global.url == null) {
                Utils.sleep(30);
            }
            textField.setText(Global.url);
            frame.repaint();
        });
    }
}