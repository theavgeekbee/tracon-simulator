package skill.issue.dim2d.text;

import org.joml.Vector2d;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.awt.*;
import java.nio.ByteBuffer;
import java.io.*;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class TextRenderer {
    static VRCFont font;
    static int texId;
    public static ByteBuffer imageToBuffer(String path) throws IOException {
        File file = new File(path);
        FileInputStream is = new FileInputStream(file);
        byte[] arr = is.readAllBytes();
        is.close();
        ByteBuffer buffer = ByteBuffer.allocateDirect(arr.length);
        buffer.put(arr);
        buffer.flip();
        return buffer;
    }
    public static void init() throws IOException, FontFormatException {
        VRCFont.init(64);
    }
    public static void renderText(String s, Vector2d pos, Vector2d dim) {
        double charWidth = dim.x/s.length();
        double x = pos.x;
        for (char c : s.toCharArray()) {
            if (c == ' ') {
                x += charWidth;
                continue;
            }
            renderText$char("" + c, new Vector2d(x, pos.y), new Vector2d(charWidth, dim.y));
            x += charWidth;
        }
    }
    public static void renderText$char(String s, Vector2d pos, Vector2d dim) {
        for (char c : s.toCharArray()) {
            int width, height, textureId;
            String filePath = "src/main/resources/bitmap/" + c + ".png";
            if (Character.isUpperCase(c)) {
                filePath = "src/main/resources/bitmap/" + c + "C.png";
            }
            try (MemoryStack stack = MemoryStack.stackPush()) {
                IntBuffer widthBuffer = stack.mallocInt(1);
                IntBuffer heightBuffer = stack.mallocInt(1);
                IntBuffer channelsBuffer = stack.mallocInt(1);

                // Load the image using stb_image
                ByteBuffer imageBuffer = STBImage.stbi_load(filePath, widthBuffer, heightBuffer, channelsBuffer, 4);
                if (imageBuffer == null) {
                    throw new RuntimeException("Failed to load texture image: " + STBImage.stbi_failure_reason());
                }

                // Get the image width and height
                width = widthBuffer.get();
                height = heightBuffer.get();

                // Generate a new texture ID
                textureId = glGenTextures();

                // Bind the texture
                glBindTexture(GL_TEXTURE_2D, textureId);

                // Set texture parameters
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

                // Upload the image data to the texture
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, imageBuffer);

                // Generate mipmaps
                glGenerateMipmap(GL_TEXTURE_2D);

                // Free the image buffer
                STBImage.stbi_image_free(imageBuffer);
            }
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
}
