//@ian

package util;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;

public class btnBorderless {

    // Neutral grayscale palette
    private static final Color TEXT_COLOR = new Color(90, 80, 70);
    private static final Color HOVER_BG = new Color(240, 230, 210);
    private static final Color PRESSED_BG = new Color(215, 200, 180);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);

    private static void baseStyle(JButton button) {
        button.setFocusPainted(false);
        button.setFont(BUTTON_FONT);
        button.setForeground(TEXT_COLOR);
        button.setBackground(new Color(255, 255, 255, 0)); // fully transparent
        button.setUI(new BasicButtonUI());
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(10, 25, 10, 25)); // no visible line
        button.setText(button.getText().toUpperCase());
    }

    public static void styleBorderlessButton(JButton button) {
        baseStyle(button);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setOpaque(true);
                button.setBackground(HOVER_BG);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setOpaque(false);
                button.setBackground(new Color(255, 255, 255, 0));
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                button.setBackground(PRESSED_BG);
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                if (button.contains(evt.getPoint())) {
                    button.setBackground(HOVER_BG);
                } else {
                    button.setBackground(new Color(255, 255, 255, 0));
                }
            }
        });
    }
}