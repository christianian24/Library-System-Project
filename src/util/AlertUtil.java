//@ian

package util;

import javax.swing.*;
import java.awt.*;

public class AlertUtil {

    // 🧠 Track the currently displayed toast
    private static JWindow currentToast = null;
    private static Thread currentThread = null;

    public static synchronized void showRoundedToastTopRight(Component parent, String message, Color bgColor) {
        // 🛑 Close the current toast if it's still visible
        if (currentToast != null) {
            currentToast.dispose();
            currentToast = null;

            if (currentThread != null && currentThread.isAlive()) {
                currentThread.interrupt(); // stop any ongoing animation
            }
        }

        JWindow toast = new JWindow();
        currentToast = toast; // save reference

        // 🟢 Rounded background panel
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        panel.setOpaque(false);
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        JLabel label = new JLabel(message);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(Color.WHITE);
        panel.add(label);

        toast.add(panel);
        toast.pack();

        // 📍 Top-right position relative to parent
        Point location = parent.getLocationOnScreen();
        int targetX = location.x + parent.getWidth() - toast.getWidth() - 20;
        int targetY = location.y + 45;

        int startX = targetX + 15;

        toast.setLocation(startX, targetY);
        toast.setOpacity(0f);
        toast.setAlwaysOnTop(true);
        toast.setVisible(true);

        // 🪄 Animation thread
        Thread t = new Thread(() -> {
            try {
                // 🎬 Slide in
                for (int i = 0; i <= 10; i++) {
                    float p = i / 10f;
                    int x = startX - (int) ((startX - targetX) * p);
                    float opacity = p;
                    SwingUtilities.invokeLater(() -> {
                        toast.setOpacity(opacity);
                        toast.setLocation(x, targetY);
                    });
                    sleep(25);
                }

                // ⏳ Stay visible
                sleep(2500);

                // 🎬 Slide out
                for (int i = 10; i >= 0; i--) {
                    float p = i / 10f;
                    int x = startX - (int) ((startX - targetX) * p);
                    float opacity = p;
                    SwingUtilities.invokeLater(() -> {
                        toast.setOpacity(opacity);
                        toast.setLocation(x, targetY);
                    });
                    sleep(25);
                }

            } catch (InterruptedException ignored) {
                // 🧹 If interrupted, just close fast
            } finally {
                SwingUtilities.invokeLater(() -> {
                    toast.dispose();
                    if (toast == currentToast) {
                        currentToast = null;
                        currentThread = null;
                    }
                });
            }
        });

        currentThread = t;
        t.start();
    }

    private static void sleep(long ms) throws InterruptedException {
        Thread.sleep(ms);
    }
}