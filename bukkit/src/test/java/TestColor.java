import org.junit.jupiter.api.Test;

import java.awt.*;

public class TestColor {
    @Test
    public void testFormat() {
        System.out.printf("#%02x%02x%02x%n", 255, 102, 255);
    }

    @Test
    public void testMiniMessage() {
        Color[] colors = {  // THESE ARE RANDOM
                new Color(255, 102, 255),
                new Color(255, 255, 255),
                new Color(255, 255, 116),
        };
        StringBuilder mini = new StringBuilder();
        mini.append("<gradient");
        for (Color color : colors) {
            mini.append(":").append("#%02x%02x%02x".formatted(color.getRed(), color.getGreen(), color.getBlue()));
        }
        mini.append(">").append("TEST").append("</gradient>");
        System.out.println(mini);

        System.out.printf("%06x%n", 0x00d7fb);
    }
}
