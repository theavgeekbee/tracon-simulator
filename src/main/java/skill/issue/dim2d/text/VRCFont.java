package skill.issue.dim2d.text;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class VRCFont {
    public static void init() throws IOException, FontFormatException {
        InputStream stream = VRCFont.class.getResourceAsStream("/VRC.ttf");
        Font font = Font.createFont(Font.TRUETYPE_FONT, stream);
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
        for (char c : chars) {
            if (font.canDisplay(c)) {
                System.out.println(c);
            }
        }
    }
}
