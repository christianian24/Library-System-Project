/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.awt.*;
import javax.swing.JPanel;

public class RoundedPanel extends JPanel {

    private int cornerRadius = 20;           // radius of corners
    private Color cornerColor = null;        // fill color
    private Color borderColor = Color.BLACK; // border color

    // Constructors
    public RoundedPanel() {
        setOpaque(false); // make panel transparent so rounded corners work
    }

    public RoundedPanel(int radius) {
        this.cornerRadius = radius;
        setOpaque(false);
    }

    public RoundedPanel(int radius, Color fillColor, Color borderColor) {
        this.cornerRadius = radius;
        this.cornerColor = fillColor;
        this.borderColor = borderColor;
        setOpaque(false);
    }

    // Setters
    public void setCornerColor(Color color) {
        this.cornerColor = color;
        repaint();
    }

    public void setBorderColor(Color color) {
        this.borderColor = color;
        repaint();
    }

    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = getWidth();
        int height = getHeight();
        Dimension arcs = new Dimension(cornerRadius, cornerRadius);

        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fill color: use cornerColor if set, otherwise fallback to background
        Color fill = (cornerColor != null) ? cornerColor : getBackground();
        graphics.setColor(fill);
        graphics.fillRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height);

        // Border color
        graphics.setColor(borderColor);
        graphics.drawRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height);
    }
}
