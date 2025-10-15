//author @ian

package util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SegmentedToggle extends JPanel {
    private JToggleButton leftButton;
    private JToggleButton rightButton;
    private ButtonGroup buttonGroup1;

    public SegmentedToggle(String leftText, String rightText) {
        setLayout(new GridLayout(1, 2));
        setOpaque(false);

        leftButton = new JToggleButton(leftText);
        rightButton = new JToggleButton(rightText);

        buttonGroup1 = new ButtonGroup();
        buttonGroup1.add(leftButton);
        buttonGroup1.add(rightButton);

        leftButton.setSelected(true);
        styleButton(leftButton, true);
        styleButton(rightButton, false);

        leftButton.addActionListener(e -> switchStyle(leftButton, rightButton));
        rightButton.addActionListener(e -> switchStyle(rightButton, leftButton));

        add(leftButton);
        add(rightButton);
    }

    private void styleButton(JToggleButton btn, boolean active) {
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setOpaque(true);
        btn.setBackground(active ? new Color(0, 200, 180) : Color.LIGHT_GRAY);
        btn.setForeground(active ? Color.WHITE : Color.DARK_GRAY);
    }

    private void switchStyle(JToggleButton active, JToggleButton inactive) {
        styleButton(active, true);
        styleButton(inactive, false);
    }

    public void addStudentListener(ActionListener l) {
        leftButton.addActionListener(l);
    }

    public void addAdminListener(ActionListener l) {
        rightButton.addActionListener(l);
    }

    public boolean isStudentSelected() {
        return leftButton.isSelected();
    }
}
