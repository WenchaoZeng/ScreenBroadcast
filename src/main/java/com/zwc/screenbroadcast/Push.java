package com.zwc.screenbroadcast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.sun.net.httpserver.HttpExchange;
import com.zwc.screenbroadcast.Global.MouseInfo;

/**
 * 推送
 * TODO: 每个客户端一个单独的推送线程
 * TODO: 减少重复数据的推送
 */
public class Push {

    static byte[] mouseScript;

    public static void doPush(OutputStream os) throws IOException {
        byte[] mouseScript = null;
        while (true) {
            if (mouseScript != Push.mouseScript) {
                mouseScript = Push.mouseScript;
                if (!write(os, mouseScript)) {
                    return;
                }
            }

            Utils.sleep(30);
        }
    }

    static boolean write(OutputStream os, byte[] bytes) throws IOException {
        try {
            os.write(bytes);
            os.flush();
        } catch (IOException ex) {
            if (!ex.getMessage().equals("Broken pipe")) {
                Utils.print(ex);
            }

            return false;
        }

        return true;
    }

    public Push() {
        Utils.backend(() -> {
            MouseInfo mouseInfo = null;
            while (true) {

                // 鼠标事件
                if (mouseInfo != Global.mouseInfo) {
                    mouseInfo = Global.mouseInfo;

                    StringBuilder builder = new StringBuilder();
                    builder.append("<script type=\"text/javascript\">");
                    builder.append(String.format("setMouse(%d, %d);", mouseInfo.x, mouseInfo.y));
                    builder.append("</script>");

                    String script = builder.toString();
                    mouseScript = script.getBytes();
                }

                Utils.sleep(30);
            }
        });
    }
}
