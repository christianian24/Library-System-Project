//@ian

package util;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class TableStyleUtil {

    public static void styleModernTable(JTable table, JScrollPane scrollPane) {
        // --- Scroll Pane & Table Setup ---
        scrollPane.getViewport().setBackground(new Color(245, 239, 231));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        table.setRowHeight(45);
        table.setIntercellSpacing(new Dimension(0, 5));
        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(false);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setBackground(Color.WHITE);
        table.setForeground(new Color(50, 50, 50));
        table.setBorder(BorderFactory.createEmptyBorder());
        table.setFocusable(false);
        table.getTableHeader().setReorderingAllowed(false);

        // --- Custom Cell Renderer (Row Styling) ---
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            private final Color EVEN_ROW_COLOR = Color.WHITE;
            private final Color ODD_ROW_COLOR = new Color(250, 250, 250);
            private final Color SELECTED_COLOR = new Color(0, 120, 215);

            @Override
            public Component getTableCellRendererComponent(
                    JTable tbl, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {

                JLabel c = (JLabel) super.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, column);
                c.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                c.setOpaque(true);
                c.setBorder(new EmptyBorder(0, 25, 0, 25)); // Equal padding left/right
                c.setToolTipText(value != null ? value.toString() : ""); // show full email on hover
                c.setText(value != null ? value.toString() : ""); // show full text
                c.setHorizontalAlignment(SwingConstants.CENTER); // center all cells
                c.setVerticalAlignment(SwingConstants.CENTER);

                // Color logic
                if (isSelected) {
                    c.setBackground(SELECTED_COLOR);
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(row % 2 == 0 ? EVEN_ROW_COLOR : ODD_ROW_COLOR);
                    c.setForeground(new Color(50, 50, 50));
                }

                return c;
            }
        });

        // --- Header Styling ---
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 15));
        header.setPreferredSize(new Dimension(header.getWidth(), 50));
        header.setBackground(Color.WHITE);
        header.setForeground(new Color(90, 90, 90));
        header.setBorder(BorderFactory.createEmptyBorder());

        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

                JLabel headerLabel = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                headerLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 15));
                headerLabel.setBackground(Color.WHITE);
                headerLabel.setForeground(new Color(90, 90, 90));
                headerLabel.setOpaque(true);
                headerLabel.setBorder(new EmptyBorder(0, 25, 0, 25));
                headerLabel.setHorizontalAlignment(SwingConstants.CENTER); // center headers
                headerLabel.setVerticalAlignment(SwingConstants.CENTER);

                return headerLabel;
            }
        });
    }

    // --- Optional alignment methods (if you need manual overrides) ---
    public static void centerColumn(JTable table, int columnIndex) {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(columnIndex).setCellRenderer(centerRenderer);
    }

    public static void rightAlignColumn(JTable table, int columnIndex) {
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        table.getColumnModel().getColumn(columnIndex).setCellRenderer(rightRenderer);
    }
    
    // --- Column Width Setup ---
    public static void setColumnWidths(JTable table, int... widths) {
        for (int i = 0; i < widths.length && i < table.getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(widths[i]);
            column.setMinWidth(widths[i] - 20); // Allow a little flexibility
            column.setMaxWidth(widths[i] + 100); // Allow resize if needed
        }
    }
}
