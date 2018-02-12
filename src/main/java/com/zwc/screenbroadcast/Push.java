package com.zwc.screenbroadcast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;

import com.zwc.screenbroadcast.entity.MouseLocation;
import com.zwc.screenbroadcast.entity.ScreenImage;
import com.zwc.screenbroadcast.entity.ScreenSize;

/**
 * 推送
 * TODO: 减少重复数据的推送
 */
public class Push {

    static byte[] mouseScript;
    static byte[] screenSizeScript;
    static byte[] screenImageScript;

    public static void doPush(OutputStream os) throws IOException {
        byte[] mouseScript = null;
        byte[] screenSizeScript = null;
        byte[] screenImageScript = null;
        while (true) {

            // 鼠标
            if (mouseScript != Push.mouseScript) {
                mouseScript = Push.mouseScript;
                Log.info("pushing mouse, size: %d", mouseScript.length);
                if (!write(os, mouseScript)) {
                    return;
                }
            }

            // 屏幕尺寸
            if (screenSizeScript != Push.screenSizeScript) {
                screenSizeScript = Push.screenSizeScript;
                Log.info("pushing screen size, size: %d", screenSizeScript.length);
                if (!write(os, screenSizeScript)) {
                    return;
                }
            }

            // 屏幕图像
            if (screenImageScript != Push.screenImageScript) {
                screenImageScript = Push.screenImageScript;
                Log.info("pushing screen image, size: %d", screenImageScript.length);
                if (!write(os, screenImageScript)) {
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
        mouseScript = script.getBytes();
    }

    public static void push(ScreenSize size) {
        StringBuilder builder = new StringBuilder();
        builder.append("<script>");
        builder.append(String.format("setScreenSize(%d, %d);", size.width, size.height));
        builder.append("</script>");

        String script = builder.toString();
        screenSizeScript = script.getBytes();
    }

    public static void push(ScreenImage screen) {
        StringBuilder builder = new StringBuilder();
        builder.append("<script>");

        String base64String = Base64.getEncoder().encodeToString(screen.image);
        builder.append(String.format("setScreenImage('%s');", base64String));

        builder.append("</script>");
        String script = builder.toString();
        screenImageScript = script.getBytes();
    }
}
