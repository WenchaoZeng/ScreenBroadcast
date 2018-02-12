package com.zwc.screenbroadcast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;

import com.zwc.screenbroadcast.entity.MouseLocation;
import com.zwc.screenbroadcast.entity.ScreenImage;
import com.zwc.screenbroadcast.entity.ScreenSize;

/**
 * 推送
 */
public class Push {

    static PushData serverData = new PushData();

    public static class PushData {
        public byte[] mouseScript = null;
        public byte[] screenSizeScript = null;
        public byte[] screenImageScript = null;
    }

    public static void doPush(OutputStream os) throws IOException {
        PushData clientData = new PushData();
        Utils.frameLoop("doPush", 30, () -> {
            // 鼠标
            if (clientData.mouseScript != serverData.mouseScript) {
                clientData.mouseScript = serverData.mouseScript;
                if (Global.enablePushLog) {
                    Log.info("pushing mouse, size: %d", clientData.mouseScript.length);
                }
                if (!write(os, clientData.mouseScript)) {
                    return;
                }
            }

            // 屏幕尺寸
            if (clientData.screenSizeScript != serverData.screenSizeScript) {
                clientData.screenSizeScript = serverData.screenSizeScript;
                if (Global.enablePushLog) {
                    Log.info("pushing screen size, size: %d", clientData.screenSizeScript.length);
                }
                if (!write(os, clientData.screenSizeScript)) {
                    return;
                }
            }

            // 屏幕图像
            if (clientData.screenImageScript != serverData.screenImageScript) {
                clientData.screenImageScript = serverData.screenImageScript;
                if (Global.enablePushLog) {
                    Log.info("pushing screen image, size: %d", clientData.screenImageScript.length);
                }
                if (!write(os, clientData.screenImageScript)) {
                    return;
                }
            }
        });
    }

    static boolean write(OutputStream os, byte[] bytes) {
        try {
            os.write(bytes);
            os.flush();
        } catch (IOException ex) {
            if (!ex.getMessage().equals("Broken pipe")) {
                Log.error(ex);
            }

            return false;
        }

        return true;
    }

    public static void push(MouseLocation mouse) {
        StringBuilder builder = new StringBuilder();
        builder.append("<script>");
        builder.append(String.format("setMouse(%d, %d);", mouse.x, mouse.y));
        builder.append("</script>");

        String script = builder.toString();
        serverData.mouseScript = script.getBytes();
    }

    public static void push(ScreenSize size) {
        StringBuilder builder = new StringBuilder();
        builder.append("<script>");
        builder.append(String.format("setScreenSize(%d, %d);", size.width, size.height));
        builder.append("</script>");

        String script = builder.toString();
        serverData.screenSizeScript = script.getBytes();
    }

    public static void push(ScreenImage screen) {
        StringBuilder builder = new StringBuilder();
        builder.append("<script>");

        String base64String = Base64.getEncoder().encodeToString(screen.image);
        builder.append(String.format("setScreenImage('%s');", base64String));

        builder.append("</script>");
        String script = builder.toString();
        serverData.screenImageScript = script.getBytes();
    }
}
