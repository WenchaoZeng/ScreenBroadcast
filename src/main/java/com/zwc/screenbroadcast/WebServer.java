package com.zwc.screenbroadcast;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * Web服务器
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
        server.createContext("/", new RootHandler());
        server.start();
        Global.url = String.format("http://127.0.0.1:%s/", port);
    }

    static class RootHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange t) throws IOException {

            // 请求参数
            String path = t.getRequestURI().getPath();
            String response = null;

            // 处理静态文件
            String filePath = path.substring(1);
            if (Files.exists(Paths.get(filePath))) {
                byte[] bytes = Files.readAllBytes(Paths.get(filePath));
                response = new String(bytes);
                sendResponse(t, response);
                return;
            }

            // 列出所有文件
            List<String> files = Files.list(Paths.get(".")).map(x -> x.toString()).collect(Collectors.toList());
            response = String.join("\n", files);
            sendResponse(t, response);
            return;

        }

        /**
         * 发送请求到客户端
         */
        void sendResponse(HttpExchange t, String response) throws IOException {
            // 状态码
            int code = 200;
            if (response == null) {
                response = "Page not found, path is " + t.getRequestURI().getPath();
                code = 404;
            }

            // 输出内容
            t.sendResponseHeaders(code, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
