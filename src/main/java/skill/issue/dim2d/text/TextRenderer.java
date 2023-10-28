package skill.issue.dim2d.text;

import org.joml.Vector2d;

import java.nio.ByteBuffer;
import java.io.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class TextRenderer {
    static VRCFont font;
    static int texId;
    public static ByteBuffer imageToBuffer() throws IOException {
        File file = new File("bitmap.png");
        FileInputStream is = new FileInputStream(file);
        byte[] arr = is.readAllBytes();
        is.close();
        ByteBuffer buffer = ByteBuffer.allocateDirect(arr.length);
        buffer.put(arr);
        buffer.flip();
        return buffer;
    }
    public static void init() throws IOException {

    }
    public static void renderText(String s, Vector2d pos, Vector2d dim) {
    }
}
