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
    
    private int targetX;
    private int startX;
    private static final int ANIMATION_DURATION = 250; // Faster animation (matching AlertUtil's 25ms x 10 steps)
    private static final int SLIDE_DISTANCE = 15; // Subtle slide (matching AlertUtil)
    
    // Track current notification to prevent overlapping
    private static ModernNotification currentNotification = null;
    private Timer autoCloseTimer; // Track auto-close timer
    private Timer animationTimer; // Track animation timer
    
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
        
        // Message - dynamic width based on content
        JLabel messageLabel = new JLabel("<html><body style='width: auto; max-width: 400px'>" + message + "</body></html>");
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
                slideOut();
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
        pack(); // Size based on content
        
        // Set initial opacity to 0 for fade-in effect (like AlertUtil)
        setOpacity(0f);
        
        // Position at top-right of parent (start slightly to the right, like AlertUtil)
        if (parent != null && parent.isShowing()) {
            Point parentLocation = parent.getLocationOnScreen();
            Dimension parentSize = parent.getSize();
            
            // Target position (final resting position)
            targetX = parentLocation.x + parentSize.width - getWidth() - 20;
            int targetY = parentLocation.y + 45; // Same as AlertUtil
            
            // Start position (slightly to the right, 15px like AlertUtil)
            startX = targetX + 15;
            
            setLocation(startX, targetY);
        } else if (parent != null) {
            // Fallback if parent is not showing yet
            Point parentLocation = parent.getLocation();
            Dimension parentSize = parent.getSize();
            
            // Target position (final resting position)
            targetX = parentLocation.x + parentSize.width - getWidth() - 20;
            int targetY = parentLocation.y + 45;
            
            // Start position (slightly to the right, 15px like AlertUtil)
            startX = targetX + 15;
            
            setLocation(startX, targetY);
        } else {
            // Fallback to screen center if no parent
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            targetX = screenSize.width - getWidth() - 20;
            int targetY = 45;
            startX = targetX + 15;
            setLocation(startX, targetY);
        }
        
        // Start slide-in animation
        slideIn();
        
        // Auto-close after 2.5 seconds (matching your AlertUtil timing)
        autoCloseTimer = new Timer(2500, e -> slideOut());
        autoCloseTimer.setRepeats(false);
        autoCloseTimer.start();
    }
    
    private void slideIn() {
        animationTimer = new Timer(10, null);
        final long startTime = System.currentTimeMillis();
        
        animationTimer.addActionListener(e -> {
            long elapsed = System.currentTimeMillis() - startTime;
            float progress = Math.min(1.0f, (float) elapsed / ANIMATION_DURATION);
            
            // Ease-out cubic for smooth deceleration
            float eased = 1 - (float) Math.pow(1 - progress, 3);
            
            int currentX = startX - (int)((startX - targetX) * eased);
            setLocation(currentX, getY());
            
            // Fade in simultaneously (like AlertUtil)
            setOpacity(progress);
            
            if (progress >= 1.0f) {
                animationTimer.stop();
                setLocation(targetX, getY());
                setOpacity(1.0f);
            }
        });
        animationTimer.start();
    }
    
    private void slideOut() {
        Timer slideTimer = new Timer(10, null);
        final long startTime = System.currentTimeMillis();
        final int currentX = getX();
        final int endX = startX; // Return to starting position (15px to the right)
        
        slideTimer.addActionListener(e -> {
            long elapsed = System.currentTimeMillis() - startTime;
            float progress = Math.min(1.0f, (float) elapsed / ANIMATION_DURATION);
            
            // Ease-in cubic for smooth acceleration
            float eased = (float) Math.pow(progress, 3);
            
            int newX = currentX + (int)((endX - currentX) * eased);
            setLocation(newX, getY());
            
            // Fade out simultaneously
            float opacity = 1.0f - progress;
            setOpacity(Math.max(0, opacity));
            
            if (progress >= 1.0f) {
                slideTimer.stop();
                dispose();
                currentNotification = null;
            }
        });
        slideTimer.start();
    }
    
    public static void show(Frame parent, String message, Type type) {
        SwingUtilities.invokeLater(() -> {
            // If there's already a notification showing, close it immediately
            if (currentNotification != null) {
                currentNotification.forceClose(); // Instant close, no animation
            }
            showNotification(parent, message, type);
        });
    }
    
    private static void showNotification(Frame parent, String message, Type type) {
        currentNotification = new ModernNotification(parent, message, type);
        currentNotification.setVisible(true);
    }
    
    // Force close without animation for instant replacement (like AlertUtil)
    private void forceClose() {
        // Stop all timers immediately
        if (autoCloseTimer != null) {
            autoCloseTimer.stop();
        }
        if (animationTimer != null) {
            animationTimer.stop();
        }
        
        // Dispose immediately without animation
        dispose();
        currentNotification = null;
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