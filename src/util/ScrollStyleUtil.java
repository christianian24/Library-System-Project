package util;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class ScrollStyleUtil {

    public static void styleModernScroll(JScrollPane scrollPane) {
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(new Color(245, 239, 231)); // matches table bg
        scrollPane.setBackground(new Color(245, 239, 231));

        // Vertical scrollbar
        JScrollBar vertical = scrollPane.getVerticalScrollBar();
        vertical.setUnitIncrement(16);
        vertical.setUI(new WarmBeigeScrollBarUI());
        vertical.setOpaque(false);

        // Horizontal scrollbar (optional)
        JScrollBar horizontal = scrollPane.getHorizontalScrollBar();
        if (horizontal != null) {
            horizontal.setUI(new WarmBeigeScrollBarUI());
            horizontal.setOpaque(false);
        }
    }

    // --- Inner class defining the warm beige style ---
    private static class WarmBeigeScrollBarUI extends BasicScrollBarUI {

        private final Color thumbColorNormal = new Color(190, 170, 150);
        private final Color thumbColorHover = new Color(160, 140, 120);
        private final Color customTrackColor = new Color(245, 239, 231);

        @Override
        public Dimension getPreferredSize(JComponent c) {
            if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
                return new Dimension(10, 0);
            } else {
                return new Dimension(0, 10);
            }
        }

        // We remove configureScrollBarColors(), since we manually paint track
        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(customTrackColor);
            g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
            g2.dispose();
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            if (!scrollbar.isEnabled() || thumbBounds.width > thumbBounds.height && scrollbar.getOrientation() == JScrollBar.VERTICAL)
                return;

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color color = isThumbRollover() ? thumbColorHover : thumbColorNormal;
            g2.setPaint(color);
            g2.fillRoundRect(thumbBounds.x + 2, thumbBounds.y + 2,
                    thumbBounds.width - 4, thumbBounds.height - 4, 10, 10);

            g2.dispose();
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createInvisibleButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createInvisibleButton();
        }

        private JButton createInvisibleButton() {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            button.setVisible(false);
            button.setOpaque(false);
            button.setBorder(null);
            return button;
        }
    }
}
