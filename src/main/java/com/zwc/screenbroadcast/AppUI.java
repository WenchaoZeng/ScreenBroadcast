package com.zwc.screenbroadcast;

import java.awt.*;

import javax.swing.*;

/**
 * app端显示和交互
 * TODO: 界面美化, 显示复制url按钮
 */
public class AppUI {
    public AppUI() throws Exception {

        // 窗口
        JFrame frame = new JFrame("屏幕分享");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 100);
        frame.setLocation(700, 400);

        // Log显示框
        TextArea textArea = new TextArea("");
        Runnable updateLog = () -> {
            synchronized(Global.logs) {
                textArea.setText(Global.logs.toString());
            }
            frame.repaint();
        };
        textArea.addTextListener(e -> {
            if (textArea.getText().equals("logs")) {
                updateLog.run();
            }
        });

        // 文本框
        TextField textField = new TextField("启动中");
        frame.getContentPane().add(textField);
        textField.addTextListener(e -> {
            if (textField.getText().equals("logs")) {
                frame.getContentPane().remove(textField);
                frame.getContentPane().add(textArea);
                frame.setResizable(true);
                frame.revalidate();
                updateLog.run();
            }
        });

        frame.setVisible(true);

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