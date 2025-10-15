//author @ian

package util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Modern, reusable dialog component for forms
 */
public class ModernDialog extends JDialog {
    private final Map<String, JComponent> fields = new HashMap<>();
    private boolean confirmed = false;
    private final Color bgColor = new Color(245, 239, 231);
    private final Color panelColor = new Color(230, 216, 195);
    private final Color buttonColor = new Color(222, 207, 187);
    private final Color buttonHoverColor = new Color(211, 196, 174);
    
    private ModernDialog(Frame parent, String title) {
        super(parent, title, true);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        
        JPanel mainPanel = new RoundedPanel(20);
        mainPanel.setLayout(new BorderLayout(0, 0));
        mainPanel.setBackground(bgColor);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
        
        setContentPane(mainPanel);
    }
    
    public static class Builder {
        private final ModernDialog dialog;
        private final JPanel fieldsPanel;
        private int fieldCount = 0;
        
        public Builder(Frame parent, String title, String subtitle) {
            dialog = new ModernDialog(parent, title);
            
            // Header Panel
            JPanel headerPanel = new JPanel();
            headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
            headerPanel.setBackground(dialog.panelColor);
            headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 15, 25));
            
            JLabel titleLabel = new JLabel(title);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
            titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            JLabel subtitleLabel = new JLabel(subtitle);
            subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            subtitleLabel.setForeground(new Color(100, 100, 100));
            subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            headerPanel.add(titleLabel);
            headerPanel.add(Box.createVerticalStrut(5));
            headerPanel.add(subtitleLabel);
            
            dialog.add(headerPanel, BorderLayout.NORTH);
            
            // Fields Panel
            fieldsPanel = new JPanel();
            fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
            fieldsPanel.setBackground(dialog.bgColor);
            fieldsPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
            
            JScrollPane scrollPane = new JScrollPane(fieldsPanel);
            scrollPane.setBorder(null);
            scrollPane.setBackground(dialog.bgColor);
            scrollPane.getViewport().setBackground(dialog.bgColor);
            
            //Apply modern scroll styling to match table scrollbars
            ScrollStyleUtil.styleModernScroll(scrollPane);
            
            dialog.add(scrollPane, BorderLayout.CENTER);
        }
        
        public Builder addTextField(String key, String label, String defaultValue, boolean editable) {
            JLabel labelComp = new JLabel(label);
            labelComp.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            labelComp.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            JTextField textField = new JTextField(defaultValue);
            textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            textField.setPreferredSize(new Dimension(400, 35));
            textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
            textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
            textField.setEditable(editable);
            if (!editable) {
                textField.setBackground(new Color(220, 220, 220));
            }
            textField.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            if (fieldCount > 0) {
                fieldsPanel.add(Box.createVerticalStrut(15));
            }
            fieldsPanel.add(labelComp);
            fieldsPanel.add(Box.createVerticalStrut(5));
            fieldsPanel.add(textField);
            
            dialog.fields.put(key, textField);
            fieldCount++;
            
            return this;
        }
        
        public Builder addComboBox(String key, String label, String[] options, String selectedValue) {
            JLabel labelComp = new JLabel(label);
            labelComp.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            labelComp.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            JComboBox<String> comboBox = new JComboBox<>(options);
            comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            comboBox.setPreferredSize(new Dimension(400, 35));
            comboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
            comboBox.setBackground(dialog.bgColor);
            if (selectedValue != null) {
                comboBox.setSelectedItem(selectedValue);
            }
            comboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            if (fieldCount > 0) {
                fieldsPanel.add(Box.createVerticalStrut(15));
            }
            fieldsPanel.add(labelComp);
            fieldsPanel.add(Box.createVerticalStrut(5));
            fieldsPanel.add(comboBox);
            
            dialog.fields.put(key, comboBox);
            fieldCount++;
            
            return this;
        }
        
        public Builder addSpinner(String key, String label, int defaultValue, int min, int max, int step) {
            JLabel labelComp = new JLabel(label);
            labelComp.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            labelComp.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            JSpinner spinner = new JSpinner(new SpinnerNumberModel(defaultValue, min, max, step));
            spinner.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            spinner.setPreferredSize(new Dimension(400, 35));
            spinner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
            spinner.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            if (fieldCount > 0) {
                fieldsPanel.add(Box.createVerticalStrut(15));
            }
            fieldsPanel.add(labelComp);
            fieldsPanel.add(Box.createVerticalStrut(5));
            fieldsPanel.add(spinner);
            
            dialog.fields.put(key, spinner);
            fieldCount++;
            
            return this;
        }
        
        public ModernDialog build() {
            // Button Panel
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
            buttonPanel.setBackground(dialog.bgColor);
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 25, 15, 25));
            
            JButton cancelButton = createStyledButton("Cancel", dialog.buttonColor);
            cancelButton.addActionListener(e -> {
                dialog.confirmed = false;
                dialog.dispose();
            });
            
            JButton confirmButton = createStyledButton("Confirm", new Color(67, 165, 103));
            confirmButton.setForeground(Color.WHITE);
            confirmButton.addActionListener(e -> {
                dialog.confirmed = true;
                dialog.dispose();
            });
            
            buttonPanel.add(cancelButton);
            buttonPanel.add(confirmButton);
            
            dialog.add(buttonPanel, BorderLayout.SOUTH);
            
            dialog.pack();
            dialog.setSize(Math.max(500, dialog.getWidth()), Math.min(600, dialog.getHeight()));
            dialog.setLocationRelativeTo(dialog.getParent());
            
            // ESC to close
            dialog.getRootPane().registerKeyboardAction(
                e -> {
                    dialog.confirmed = false;
                    dialog.dispose();
                },
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW
            );
            
            // Enter to confirm
            dialog.getRootPane().registerKeyboardAction(
                e -> {
                    dialog.confirmed = true;
                    dialog.dispose();
                },
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW
            );
            
            return dialog;
        }
        
        private JButton createStyledButton(String text, Color bgColor) {
            JButton button = new JButton(text);
            button.setFont(new Font("Segoe UI", Font.BOLD, 13));
            button.setPreferredSize(new Dimension(100, 35));
            button.setBackground(bgColor);
            button.setForeground(text.equals("Confirm") ? Color.WHITE : Color.BLACK);
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
    }
    
    public boolean showDialog() {
        setVisible(true);
        return confirmed;
    }
    
    public String getTextFieldValue(String key) {
        JComponent component = fields.get(key);
        if (component instanceof JTextField) {
            return ((JTextField) component).getText().trim();
        }
        return "";
    }
    
    public String getComboBoxValue(String key) {
        JComponent component = fields.get(key);
        if (component instanceof JComboBox) {
            return ((JComboBox<?>) component).getSelectedItem().toString();
        }
        return "";
    }
    
    public int getSpinnerValue(String key) {
        JComponent component = fields.get(key);
        if (component instanceof JSpinner) {
            return (Integer) ((JSpinner) component).getValue();
        }
        return 0;
    }
    
    // Rounded Panel class
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