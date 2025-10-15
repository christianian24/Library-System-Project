//author @ian

package util;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

public class ButtonRendererEditor extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {
    
    private JPanel renderPanel;
    private JPanel editorPanel;
    private JButton renderEditButton;
    private JButton renderDeleteButton;
    private JButton editorEditButton;
    private JButton editorDeleteButton;
    private int editingRow = -1;
    
    public interface ActionHandler {
        void onEdit(int row);
        void onDelete(int row);
    }
    
    private final ActionHandler handler;
    
    public ButtonRendererEditor(ActionHandler handler) {
        this.handler = handler;
        
        // Create separate panels for rendering and editing
        renderPanel = createButtonPanel();
        editorPanel = createButtonPanel();
        
        // Create separate button sets
        renderEditButton = createStyledButton("Edit", new Color(222, 207, 187), Color.BLACK);
        renderDeleteButton = createStyledButton("Delete", new Color(200, 60, 60), Color.WHITE);
        
        editorEditButton = createStyledButton("Edit", new Color(222, 207, 187), Color.BLACK);
        editorDeleteButton = createStyledButton("Delete", new Color(200, 60, 60), Color.WHITE);
        
        // Add action listeners only to editor buttons
        editorEditButton.addActionListener(e -> {
            int row = editingRow;
            stopCellEditing();
            if (row >= 0) {
                SwingUtilities.invokeLater(() -> handler.onEdit(row));
            }
        });
        
        editorDeleteButton.addActionListener(e -> {
            int row = editingRow;
            stopCellEditing();
            if (row >= 0) {
                SwingUtilities.invokeLater(() -> handler.onDelete(row));
            }
        });
        
        // Add buttons to panels
        renderPanel.add(renderEditButton);
        renderPanel.add(renderDeleteButton);
        
        editorPanel.add(editorEditButton);
        editorPanel.add(editorDeleteButton);
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 5));
        panel.setOpaque(true);
        panel.setBackground(Color.WHITE);
        return panel;
    }
    
    private JButton createStyledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(true);
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bg.darker(), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        // Prevent color change on press by overriding the UI behavior
        btn.setRolloverEnabled(false);
        btn.getModel().addChangeListener(e -> {
            ButtonModel model = (ButtonModel) e.getSource();
            if (model.isPressed()) {
                btn.setBackground(bg);
            } else if (model.isRollover()) {
                btn.setBackground(bg.brighter());
            } else {
                btn.setBackground(bg);
            }
        });
        
        return btn;
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        // Set panel background based on selection
        if (isSelected) {
            renderPanel.setBackground(table.getSelectionBackground());
        } else {
            renderPanel.setBackground(Color.WHITE);
        }
        
        // Ensure buttons are visible
        renderEditButton.setVisible(true);
        renderDeleteButton.setVisible(true);
        
        // Force repaint to avoid disappearing buttons
        renderPanel.revalidate();
        renderPanel.repaint();
        
        return renderPanel;
    }
    
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        
        editingRow = row;
        
        // Set editor panel background
        editorPanel.setBackground(table.getSelectionBackground());
        
        // Ensure buttons are visible
        editorEditButton.setVisible(true);
        editorDeleteButton.setVisible(true);
        
        // Force repaint
        editorPanel.revalidate();
        editorPanel.repaint();
        
        return editorPanel;
    }
    
    @Override
    public Object getCellEditorValue() {
        return "";
    }
    
    @Override
    public boolean stopCellEditing() {
        editingRow = -1;
        fireEditingStopped();
        return true;
    }
    
    @Override
    public void cancelCellEditing() {
        editingRow = -1;
        fireEditingCanceled();
    }
}