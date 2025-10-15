//author @ian


package util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Modern confirmation dialog
 */
public class ModernConfirm extends JDialog {
    private boolean confirmed = false;
    
    private ModernConfirm(Frame parent, String title, String message, String confirmText, String cancelText) {
        super(parent, title, true);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        
        JPanel mainPanel = new RoundedPanel(20);
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(245, 239, 231));
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(230, 216, 195));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 15, 25));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(245, 239, 231));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        
        JLabel messageLabel = new JLabel("<html><body style='width: 350px'>" + message + "</body></html>");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(messageLabel);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        buttonPanel.setBackground(new Color(245, 239, 231));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 25, 15, 25));
        
        JButton cancelButton = createStyledButton(cancelText, new Color(222, 207, 187), Color.BLACK);
        cancelButton.addActionListener(e -> {
            confirmed = false;
            dispose();
        });
        
        JButton confirmButton = createStyledButton(confirmText, new Color(239, 68, 68), Color.WHITE);
        confirmButton.addActionListener(e -> {
            confirmed = true;
            dispose();
        });
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(confirmButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
        pack();
        setSize(450, getHeight());
        setLocationRelativeTo(parent);
        
        // ESC to cancel
        getRootPane().registerKeyboardAction(
            e -> {
                confirmed = false;
                dispose();
            },
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );
    }
    
    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setPreferredSize(new Dimension(100, 35));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    public static boolean show(Frame parent, String title, String message) {
        return show(parent, title, message, "Delete", "Cancel");
    }
    
    public static boolean show(Frame parent, String title, String message, String confirmText, String cancelText) {
        ModernConfirm dialog = new ModernConfirm(parent, title, message, confirmText, cancelText);
        dialog.setVisible(true);
        return dialog.confirmed;
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