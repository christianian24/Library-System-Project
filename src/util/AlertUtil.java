/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import javax.swing.*;
import java.awt.*;
/**
 *
 * @Cabilen
 */
public class AlertUtil {

    public static void showRoundedToastTopRight(Component parent, String message, Color bgColor) {
        JWindow toast = new JWindow();

        // 🟢 Rounded background
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

        // 📍 Final top-right position
        Point location = parent.getLocationOnScreen();
        int targetX = location.x + parent.getWidth() - toast.getWidth() - 20;
        int targetY = location.y + 20;

        // 🪄 Start slightly to the right (inside frame area)
        int startX = targetX + 50;

        toast.setLocation(startX, targetY);
        toast.setOpacity(0f);
        toast.setAlwaysOnTop(true);
        toast.setVisible(true);

        // 🎬 Slide in (right → left)
        new Thread(() -> {
            for (int i = 0; i <= 10; i++) {
                float t = i / 10f;
                int x = startX - (int) ((startX - targetX) * t);
                float opacity = t;
                SwingUtilities.invokeLater(() -> {
                    toast.setOpacity(opacity);
                    toast.setLocation(x, targetY);
                });
                sleep(25);
            }

            // ⏳ stay visible
            sleep(2500);

            // 🎬 Slide out (left → right)
            for (int i = 10; i >= 0; i--) {
                float t = i / 10f;
                int x = startX - (int) ((startX - targetX) * t);
                float opacity = t;
                SwingUtilities.invokeLater(() -> {
                    toast.setOpacity(opacity);
                    toast.setLocation(x, targetY);
                });
                sleep(25);
            }

            SwingUtilities.invokeLater(toast::dispose);
        }).start();
    }

    private static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }
}