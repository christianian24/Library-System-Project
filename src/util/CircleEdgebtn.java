//author @ian

package util;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;

public class CircleEdgebtn {

    // Colors and font
    private static final Color BORDER_COLOR = new Color(180, 165, 145);
    private static final Color TEXT_COLOR = new Color(90, 80, 70);
    private static final Color HOVER_BG = new Color(240, 230, 210);
    private static final Color PRESSED_BG = new Color(215, 200, 180);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);

    public static void styleForwardButton(JButton button) {
        button.setUI(new BasicButtonUI());
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFont(BUTTON_FONT);
        button.setForeground(TEXT_COLOR);
        button.setBorderPainted(false);

        // Rounded hover + press behavior handled via repaint
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.putClientProperty("hover", true);
                button.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.putClientProperty("hover", false);
                button.repaint();
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                button.putClientProperty("pressed", true);
                button.repaint();
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                button.putClientProperty("pressed", false);
                button.repaint();
            }
        });

        // Custom paint for capsule look
        button.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                JButton b = (JButton) c;
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int arc = b.getHeight(); // makes capsule ends
                boolean hover = Boolean.TRUE.equals(b.getClientProperty("hover"));
                boolean pressed = Boolean.TRUE.equals(b.getClientProperty("pressed"));

                // Background fill only inside round shape
                if (pressed) {
                    g2.setColor(PRESSED_BG);
                    g2.fillRoundRect(0, 0, b.getWidth(), b.getHeight(), arc, arc);
                } else if (hover) {
                    g2.setColor(HOVER_BG);
                    g2.fillRoundRect(0, 0, b.getWidth(), b.getHeight(), arc, arc);
                }

                // Border
                g2.setColor(BORDER_COLOR);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, b.getWidth() - 1, b.getHeight() - 1, arc, arc);

                // Text
                FontMetrics fm = g2.getFontMetrics();
                String text = b.getText();
                int textWidth = fm.stringWidth(text);
                int textHeight = fm.getAscent();
                g2.setColor(TEXT_COLOR);
                g2.drawString(text, (b.getWidth() - textWidth) / 2, (b.getHeight() + textHeight) / 2 - 2);

                g2.dispose();
            }
        });
    }
}
