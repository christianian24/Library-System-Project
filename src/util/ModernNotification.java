//author @ian

package util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ModernNotification extends JDialog {
    
    public enum Type {
        SUCCESS(new Color(67, 165, 103), "✓"),
        ERROR(new Color(239, 68, 68), "✕"),
        WARNING(new Color(251, 146, 60), "⚠"),
        INFO(new Color(59, 130, 246), "ℹ");
        
        final Color color;
        final String icon;
        
        Type(Color color, String icon) {
            this.color = color;
            this.icon = icon;
        }
    }
    
    private ModernNotification(Frame parent, String message, Type type) {
        super(parent, false);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        
        JPanel mainPanel = new RoundedPanel(15);
        mainPanel.setLayout(new BorderLayout(15, 0));
        mainPanel.setBackground(new Color(245, 239, 231));
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(type.color, 2),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        // Icon
        JLabel iconLabel = new JLabel(type.icon);
        iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        iconLabel.setForeground(type.color);
        mainPanel.add(iconLabel, BorderLayout.WEST);
        
        // Message
        JLabel messageLabel = new JLabel("<html><body style='width: 300px'>" + message + "</body></html>");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mainPanel.add(messageLabel, BorderLayout.CENTER);
        
        // Close button
        JLabel closeLabel = new JLabel("×");
        closeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        closeLabel.setForeground(new Color(150, 150, 150));
        closeLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fadeOut();
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                closeLabel.setForeground(Color.BLACK);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                closeLabel.setForeground(new Color(150, 150, 150));
            }
        });
        mainPanel.add(closeLabel, BorderLayout.EAST);
        
        setContentPane(mainPanel);
        pack();
        
        // Position at top-right of parent
        if (parent != null) {
            Point parentLocation = parent.getLocation();
            Dimension parentSize = parent.getSize();
            setLocation(
                parentLocation.x + parentSize.width - getWidth() - 30,
                parentLocation.y + 80
            );
        }
        
        // Auto-close after 4 seconds
        Timer timer = new Timer(4000, e -> fadeOut());
        timer.setRepeats(false);
        timer.start();
    }
    
    private void fadeOut() {
        Timer timer = new Timer(10, null);
        timer.addActionListener(e -> {
            float opacity = ((Window) SwingUtilities.getWindowAncestor(getContentPane())).getOpacity();
            opacity -= 0.05f;
            if (opacity <= 0) {
                timer.stop();
                dispose();
            } else {
                ((Window) SwingUtilities.getWindowAncestor(getContentPane())).setOpacity(opacity);
            }
        });
        timer.start();
    }
    
    public static void show(Frame parent, String message, Type type) {
        SwingUtilities.invokeLater(() -> {
            ModernNotification notification = new ModernNotification(parent, message, type);
            notification.setVisible(true);
        });
    }
    
    public static void success(Frame parent, String message) {
        show(parent, message, Type.SUCCESS);
    }
    
    public static void error(Frame parent, String message) {
        show(parent, message, Type.ERROR);
    }
    
    public static void warning(Frame parent, String message) {
        show(parent, message, Type.WARNING);
    }
    
    public static void info(Frame parent, String message) {
        show(parent, message, Type.INFO);
    }
    
    static class RoundedPanel extends JPanel {
        private final int radius;
        
        public RoundedPanel(int radius) {
            super();
            this.radius = radius;
            setOpaque(false);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}