package skill.issue.dim2d.text;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.Objects;

public class VRCFont {
    public static void init(int size) throws IOException, FontFormatException {
        System.out.println("Initiating bitmap constructor with image resolution " + size + "x" + size);
        InputStream stream = VRCFont.class.getResourceAsStream("/VRC.ttf");
        assert stream != null;
        Font font = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(Font.PLAIN, size);

        System.out.println("Bitmap constructor initiated with image resolution " + size + "x" + size);

        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789*+".toCharArray();
        char[] letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        for (char c : chars) {
            if (font.canDisplay(c)) {
                String fileName;
                if (Arrays.toString(letters).contains("" + c)) {
                    fileName = c + "C";
                } else {
                    fileName = "" + c;
                }
                if (c == '*') fileName = "asterisk";
                System.out.println("Loading bitmap texture " + c);
                createFile("/resources/bitmap/" + fileName + ".png");
                drawImage(size, c, font, fileName);
                System.out.println("Finished drawing bitmap texture " + c);
            }
        }
    }
    public static void cleanupBitmaps() {
        System.out.println("Cleaning up bitmap textures");
        File file = new File("src/main/resources/bitmap");
        for (File f : Objects.requireNonNull(file.listFiles())) {
            if (f.getName().endsWith(".png")) {
                if (!f.delete()) System.out.println("Failed to delete file " + f.getAbsolutePath());
                else System.out.println("Cleaned file " + f.getAbsolutePath());
            }
        }
    }
    private static void createFile(String path) throws IOException {
        if (VRCFont.class.getResource(path) == null) {
            System.out.println("Attempting to create file /src/main" + path);
            File file = new File("src/main" + path);
            file.createNewFile();
            System.out.println("Created file " + file.getAbsolutePath());
        }
    }
    private static void drawImage(int size, char c, Font f, String fileName) throws IOException {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.setFont(f.deriveFont(Font.PLAIN, size));
        FontMetrics fm = g2d.getFontMetrics();
        int x = (size - fm.charWidth(c)) / 2;
        int y = ((size - fm.getHeight()) / 2) + fm.getAscent();
        g2d.drawString("" + c, x, y);
        g2d.dispose();
        ImageIO.write(image, "png", new File("src/main/resources/bitmap/" + fileName + ".png"));
    }
}
