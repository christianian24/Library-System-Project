package util;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ModernDialog {
    
    private static final Color BACKGROUND_COLOR = new Color(230, 216, 195);
    private static final Color INPUT_BACKGROUND = new Color(245, 239, 231);
    private static final Color LABEL_COLOR = new Color(50, 50, 50);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font INPUT_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    
    /**
     * Shows a modern input dialog with multiple fields
     * @param parent The parent frame
     * @param title Dialog title
     * @param fields Map of field labels and their types ("text", "combo", "number")
     * @param comboOptions Map of combo box options (only for combo fields)
     * @return Map of field labels and their values, or null if cancelled
     */
    public static Map<String, Object> showInputDialog(
            JFrame parent, 
            String title, 
            Map<String, String> fields,
            Map<String, String[]> comboOptions) {
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        Map<String, JComponent> components = new HashMap<>();
        
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            String fieldName = entry.getKey();
            String fieldType = entry.getValue();
            
            JLabel label = new JLabel(fieldName + ":");
            label.setFont(LABEL_FONT);
            label.setForeground(LABEL_COLOR);
            panel.add(label, gbc);
            
            JComponent component;
            
            switch (fieldType.toLowerCase()) {
                case "combo":
                    JComboBox<String> comboBox = new JComboBox<>(comboOptions.get(fieldName));
                    comboBox.setBackground(INPUT_BACKGROUND);
                    comboBox.setFont(INPUT_FONT);
                    comboBox.setPreferredSize(new Dimension(300, 35));
                    component = comboBox;
                    break;
                    
                case "number":
                    JSpinner spinner = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
                    spinner.setFont(INPUT_FONT);
                    spinner.setPreferredSize(new Dimension(300, 35));
                    ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().setBackground(INPUT_BACKGROUND);
                    component = spinner;
                    break;
                    
                case "text":
                default:
                    JTextField textField = new JTextField();
                    textField.setFont(INPUT_FONT);
                    textField.setBackground(INPUT_BACKGROUND);
                    textField.setPreferredSize(new Dimension(300, 35));
                    textField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)
                    ));
                    component = textField;
                    break;
            }
            
            components.put(fieldName, component);
            panel.add(component, gbc);
        }
        
        // Custom buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        
        int result = JOptionPane.showConfirmDialog(
            parent,
            panel,
            title,
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );
        
        if (result == JOptionPane.OK_OPTION) {
            Map<String, Object> values = new HashMap<>();
            
            for (Map.Entry<String, JComponent> entry : components.entrySet()) {
                String fieldName = entry.getKey();
                JComponent component = entry.getValue();
                
                if (component instanceof JTextField) {
                    values.put(fieldName, ((JTextField) component).getText().trim());
                } else if (component instanceof JComboBox) {
                    values.put(fieldName, ((JComboBox<?>) component).getSelectedItem());
                } else if (component instanceof JSpinner) {
                    values.put(fieldName, ((JSpinner) component).getValue());
                }
            }
            
            return values;
        }
        
        return null;
    }
    
    /**
     * Shows a simple text input dialog
     */
    public static Map<String, Object> showTextInputDialog(JFrame parent, String title, String... fieldNames) {
        Map<String, String> fields = new HashMap<>();
        for (String fieldName : fieldNames) {
            fields.put(fieldName, "text");
        }
        return showInputDialog(parent, title, fields, null);
    }
    
    /**
     * Shows a confirmation dialog with custom styling
     */
    public static boolean showConfirmDialog(JFrame parent, String message, String title) {
        UIManager.put("OptionPane.background", BACKGROUND_COLOR);
        UIManager.put("Panel.background", BACKGROUND_COLOR);
        UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 14));
        
        int result = JOptionPane.showConfirmDialog(
            parent,
            message,
            title,
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        return result == JOptionPane.YES_OPTION;
    }
    
    /**
     * Shows a success message dialog
     */
    public static void showSuccessDialog(JFrame parent, String message) {
        UIManager.put("OptionPane.background", BACKGROUND_COLOR);
        UIManager.put("Panel.background", BACKGROUND_COLOR);
        
        JOptionPane.showMessageDialog(
            parent,
            message,
            "Success",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    /**
     * Shows an error message dialog
     */
    public static void showErrorDialog(JFrame parent, String message) {
        UIManager.put("OptionPane.background", BACKGROUND_COLOR);
        UIManager.put("Panel.background", BACKGROUND_COLOR);
        
        JOptionPane.showMessageDialog(
            parent,
            message,
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
    }
    
    /**
     * Shows a warning message dialog
     */
    public static void showWarningDialog(JFrame parent, String message) {
        UIManager.put("OptionPane.background", BACKGROUND_COLOR);
        UIManager.put("Panel.background", BACKGROUND_COLOR);
        
        JOptionPane.showMessageDialog(
            parent,
            message,
            "Warning",
            JOptionPane.WARNING_MESSAGE
        );
    }
}