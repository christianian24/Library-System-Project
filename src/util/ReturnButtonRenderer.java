//author @ian

package util;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

public class ReturnButtonRenderer implements TableCellRenderer {
    private final JButton button = new JButton();
    private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 4));
    
    public interface ReturnHandler {
        void onReturn(int row);
    }
    
    public ReturnButtonRenderer() {
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        panel.setOpaque(true);
        panel.add(button);
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        String status = table.getValueAt(row, 7).toString();
        
        if ("Active".equals(status)) {
            button.setText("Return");
            button.setEnabled(true);
            button.setBackground(new Color(67, 165, 103));
            button.setForeground(Color.WHITE);
        } else {
            button.setText("Returned");
            button.setEnabled(false);            
            button.setBackground(new Color(238, 238, 238));
            button.setForeground(new Color(100, 100, 100));
        }
        
        panel.setBackground(Color.WHITE);
        
        return panel;
    }
    
    // Static method to add mouse listener for button clicks
    public static void addClickListener(JTable table, int columnIndex, ReturnHandler handler) {
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = table.columnAtPoint(e.getPoint());
                int row = table.rowAtPoint(e.getPoint());
                
                if (row >= 0 && column == columnIndex) {
                    // FIXED: Changed from column 5 to column 7 for status
                    String status = table.getValueAt(row, 7).toString();
                    if ("Active".equals(status)) {
                        handler.onReturn(row);
                    }
                }
            }
        });
    }
}