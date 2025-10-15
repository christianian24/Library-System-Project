package util;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

public class ButtonRendererEditor extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {
    private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 5));
    private final JButton editButton = new JButton("Edit");
    private final JButton deleteButton = new JButton("Delete");
    private int editingRow = -1;
    private JTable currentTable;

    public interface ActionHandler {
        void onEdit(int row);
        void onDelete(int row);
    }

    private final ActionHandler handler;

    public ButtonRendererEditor(ActionHandler handler) {
        this.handler = handler;
        
        // --- Styling ---
        styleButton(editButton, new Color(222, 207, 187), Color.BLACK);
        styleButton(deleteButton, new Color(200, 60, 60), Color.WHITE);
        
        editButton.addActionListener(e -> {
            if (editingRow >= 0) {
                handler.onEdit(editingRow);
            }
            stopCellEditing();
        });
        
        deleteButton.addActionListener(e -> {
            if (editingRow >= 0) {
                handler.onDelete(editingRow);
            }
            stopCellEditing();
        });
        
        panel.setOpaque(true);
        panel.add(editButton);
        panel.add(deleteButton);
    }

    private void styleButton(JButton btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            panel.setBackground(new Color(250,250,250));
        } else {
            panel.setBackground(Color.WHITE);
        }
        editButton.setVisible(true);
        deleteButton.setVisible(true);
        return panel;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        editingRow = row;
        currentTable = table;
        panel.setBackground(new Color(250, 250, 250));
        editButton.setVisible(true);
        deleteButton.setVisible(true);
        return panel;
    }
    
    
    
    @Override
    public Object getCellEditorValue() {
        return "";
    }
    
    @Override
    public boolean stopCellEditing() {
        // Don't actually update the table cell, just stop editing
        fireEditingStopped();
        return true;
        
    }
}