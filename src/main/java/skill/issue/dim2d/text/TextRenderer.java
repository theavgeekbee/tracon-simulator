package skill.issue.dim2d.text;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTruetype;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

public class TextRenderer {
    private static final int BITMAP_SIZE = 512;

    private int textureId;
    private STBTTBakedChar.Buffer chars;

    private ByteBuffer ioResourceToByteBuffer(String resource) throws IOException {
        ByteBuffer buffer;

        InputStream sourceStream = TextRenderer.class.getResourceAsStream(resource);
        if (sourceStream == null) {
            System.err.println("Source stream is null: resource address at " + resource);
            System.exit(1);
        }
        ReadableByteChannel rbc = Channels.newChannel(sourceStream);
        buffer = ByteBuffer.allocateDirect(1048576);

        while (rbc.read(buffer) > 0);
        buffer.flip();
        return buffer;
    }

    public void init(String fontFile) {
        try (MemoryStack ignored = MemoryStack.stackPush()) {
            ByteBuffer fontData = ioResourceToByteBuffer(fontFile);

            ByteBuffer bitmap = BufferUtils.createByteBuffer(BITMAP_SIZE * BITMAP_SIZE);
            chars = STBTTBakedChar.malloc(96);

            STBTruetype.stbtt_BakeFontBitmap(fontData, 32.0f, bitmap, BITMAP_SIZE, BITMAP_SIZE, 32, chars);

            textureId = glGenTextures();

            glBindTexture(GL_TEXTURE_2D, textureId);
            glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RED, BITMAP_SIZE, BITMAP_SIZE, 0,
                    GL_RED, GL_UNSIGNED_BYTE, bitmap);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glBindTexture(GL_TEXTURE_2D, 0);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load font", e);
        }
    }

    public void renderText(String text, float x, float y, float scale) {
        glBindTexture(GL_TEXTURE_2D, textureId);
        glPushMatrix();
        glTranslatef(x, y, 0);
        glScalef(scale, scale, 1);

        for(int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c >= 32 && c < 128) {
                STBTTBakedChar bakedChar = chars.get(c - 32);

                float xPos = x + bakedChar.xoff() * scale;
                float yPos = y - bakedChar.yoff() * scale;
                float w = bakedChar.x1() - bakedChar.xoff();
                float h = bakedChar.y1() - bakedChar.yoff();

                float quadWidth = w * scale;
                float quadHeight = h * scale;

                float xTex = ((float) bakedChar.x0()) / BITMAP_SIZE;
                float yTex = ((float) bakedChar.y0()) / BITMAP_SIZE;
                float texWidth = w / BITMAP_SIZE;
                float texHeight = h / BITMAP_SIZE;

                glBegin(GL_QUADS);
                glColor3d(1,1,1);
                glTexCoord2f(xTex, yTex);
                glVertex2f(xPos, yPos);
                glTexCoord2f(xTex, yTex + texHeight);
                glVertex2f(xPos, yPos + quadHeight);
                glTexCoord2f(xTex + texWidth, yTex + texHeight);
                glVertex2f(xPos + quadWidth, yPos + quadHeight);
                glTexCoord2f(xTex + texWidth, yTex);
                glVertex2f(xPos + quadWidth, yPos);
                glEnd();

                x += (bakedChar.xadvance() - 32.0f) * scale;
            }
        }

        glPopMatrix();
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}
