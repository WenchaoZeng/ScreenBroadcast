package com.zwc.screenbroadcast;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * Web服务器
 * TODO: 监听局域网IP, 而不是本地IP
 */
public class WebServer {
    public WebServer() throws Exception {
        HttpServer server = null;

        // 监听端口
        int port = 8000;
        while (port < 9000) {
            try {
                server = HttpServer.create(new InetSocketAddress(port), 0);
                break;
            } catch (java.net.BindException ex) {
                port++;
            }
        }

        // 开启web服务器
        server.setExecutor(Executors.newCachedThreadPool());
        server.createContext("/", new RootHandler());
        server.start();
        Global.url = String.format("http://127.0.0.1:%s/index.html", port);

        Log.info("App started. Url: %s", Global.url);
    }

    static class RootHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange t) throws IOException {

            // 请求参数
            String path = t.getRequestURI().getPath();

            // 推送
            if (path.endsWith("push.html")) {
                t.getResponseHeaders().add("content-type", fileHeaders.get(".html"));
                t.sendResponseHeaders(200, 0);
                Push.doPush(t.getResponseBody());
                Log.info("client connection closed. Remote address: %s", t.getRemoteAddress().toString());
                return;
            }

            // 处理静态文件
            String filePath = "web" + path;
            if (Files.exists(Paths.get(filePath))) {
                sendFile(t, filePath);
                return;
            }

            // 列出所有文件
            List<String> files = Files.list(Paths.get(".")).map(x -> x.toString()).collect(Collectors.toList());
            String response = String.join("\n", files);
            send(t, response);
            return;
        }

        static Map<String, String> fileHeaders = new HashMap<>();
        static {
            fileHeaders.put(".html", "text/html; charset=utf-8");
            fileHeaders.put(".css", "text/css; charset=utf-8");
            fileHeaders.put(".js", "application/x-javascript; charset=utf-8");
            fileHeaders.put(".jpg", "image/jpeg; charset=utf-8");
            fileHeaders.put(".gif", "image/gif; charset=utf-8");
        }

        public static void sendFile(HttpExchange t, String filePath) throws IOException {
            byte[] bytes = Files.readAllBytes(Paths.get(filePath));

            // 文件类型
            filePath = filePath.toLowerCase();
            for (Map.Entry<String, String> item : fileHeaders.entrySet()) {
                if (filePath.endsWith(item.getKey())) {
                    t.getResponseHeaders().add("content-type", item.getValue());
                }
            }

            send(t, bytes);
        }

        public static void send(HttpExchange t, String response) throws IOException {
            byte[] bytes = response.getBytes();
            send(t, bytes);
        }

        public static void send(HttpExchange t, byte[] bytes) throws IOException {
            send(t, 200, bytes);
        }

        public static void send(HttpExchange t, int code, byte[] bytes) throws IOException {
            t.sendResponseHeaders(code, bytes.length);
            OutputStream os = t.getResponseBody();
            os.write(bytes);
            os.close();
        }
    }
}
