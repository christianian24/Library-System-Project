//author @ian

package util;

import javax.swing.JFrame;

public class FrameUtil {
    public static void setupFrame(JFrame frame) {
        frame.setSize(1600, 900);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
