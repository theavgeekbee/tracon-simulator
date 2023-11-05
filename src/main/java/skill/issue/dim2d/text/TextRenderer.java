package skill.issue.dim2d.text;

import org.joml.Vector2d;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import skill.issue.dim2d.utils.Alias;

import java.awt.*;
import java.nio.ByteBuffer;
import java.io.*;
import java.nio.IntBuffer;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class TextRenderer {
    private static HashMap<Character, Alias> alias = new HashMap<>();
    public static void init() throws IOException, FontFormatException {
        VRCFont.init(64);
        System.out.print("Generating alias map...");
        char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        for (char c : chars) {
            try (MemoryStack stack = MemoryStack.stackPush()) {
                System.out.println("Generating alias for " + c);
                String filePath = Character.isUpperCase(c) ? "src/main/resources/bitmap/" + c + "C.png" : "src/main/resources/bitmap/" + c + ".png";
                IntBuffer widthBuffer = stack.mallocInt(1);
                IntBuffer heightBuffer = stack.mallocInt(1);
                IntBuffer channelsBuffer = stack.mallocInt(1);

                ByteBuffer imageBuffer = STBImage.stbi_load(filePath, widthBuffer, heightBuffer, channelsBuffer, 4);
                if (imageBuffer == null) {
                    throw new RuntimeException("Failed to load texture image: " + STBImage.stbi_failure_reason());
                }
                alias.put(c, new Alias(imageBuffer, widthBuffer.get(), heightBuffer.get()));
            }
        }
        System.out.println("Generated " + chars.length + " aliases.");
    }
    public static void cleanupAlias() {
        System.out.println("Cleaning up aliases...");
        for (Alias a : alias.values()) {
            STBImage.stbi_image_free(a.buf());
        }
    }
    public static void renderText(String s, Vector2d pos, Vector2d dim) {
        double charWidth = dim.x/s.length();
        double x = pos.x;
        for (char c : s.toCharArray()) {

            if (c == ' ') {
                x += charWidth;
                continue;
            }
            renderChar(c, new Vector2d(x, pos.y), new Vector2d(charWidth, dim.y));
            x += charWidth;
        }
    }
    private static void renderChar(char s, Vector2d pos, Vector2d dim) {
        Alias a = alias.get(s);

        int width = a.width(), height = a.height(), textureId;

        textureId = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, textureId);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, a.buf());

        glGenerateMipmap(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, textureId);
        glEnable(GL_TEXTURE_2D);

        glBegin(GL_QUADS);
        glTexCoord2d(0, 1);
        glVertex2d(pos.x, pos.y);
        glTexCoord2d(1, 1);
        glVertex2d(pos.x + dim.x, pos.y);
        glTexCoord2d(1, 0);
        glVertex2d(pos.x + dim.x, pos.y + dim.y);
        glTexCoord2d(0, 0);
        glVertex2d(pos.x, pos.y + dim.y);
        glEnd();

        glDisable(GL_TEXTURE_2D);
        glDeleteTextures(textureId);
    }
}
