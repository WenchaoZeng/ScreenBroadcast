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

    public static List<HttpExchange> clients = new ArrayList<>();
    public static void addClient(HttpExchange t) throws IOException {
        t.getResponseHeaders().add("content-type", "text/html; charset=utf-8");
        t.sendResponseHeaders(200, 0);
        clients.add(t);
        try {
            pushingThread.join();
        } catch (InterruptedException ex) {
        }
    }

    static Thread pushingThread = null;
    public Push() {
        pushingThread = Utils.backend(() -> {
            while (true) {

                StringBuilder builder = new StringBuilder();
                builder.append("<script type=\"text/javascript\">");

                // 推送鼠标事件
                MouseInfo mouseInfo = Global.mouseInfo;
                builder.append(String.format("setMouse(%d, %d);", mouseInfo.x, mouseInfo.y));

                builder.append("</script>");

                // 发送推送
                String script = builder.toString();
                byte[] bytes = script.getBytes();
                for (HttpExchange t : clients) {
                    try {
                        OutputStream os = t.getResponseBody();
                        os.write(bytes);
                        os.flush();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                
                Utils.sleep(30);
            }
        });
    }
}
