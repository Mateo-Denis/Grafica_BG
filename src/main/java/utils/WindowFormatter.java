package utils;

import javax.swing.*;
import java.awt.*;

public class WindowFormatter {

	public static void relativeSizeAndCenter(JFrame windowFrame, double w, double h) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int width = (int) (screenSize.width * w);
        int height = (int) (screenSize.height * h);
        windowFrame.setSize(width, height);

        int x = (screenSize.width - width) / 2;
        int y = (screenSize.height - height) / 2;
        windowFrame.setLocation(x, y);
    }

    public static void topSizeAndCenter(JFrame windowFrame, double w, double h) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int width = (int) (screenSize.width * w);
        int height = (int) (screenSize.height * h);
        windowFrame.setSize(width, height);

        int x = (screenSize.width - width) / 2;
        int y = (screenSize.height - height) / 5;
        windowFrame.setLocation(x, y);
    }
}
