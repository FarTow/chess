package panels;

import entities.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TimeDisplay extends JPanel {
    private final Player player;

    public TimeDisplay(Player player) {
        setBackground(new Color(229, 228, 228));
        this.player = player;
    }

    public void reset() { }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Initialize graphics
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.black);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // sharper text
        g2d.setFont(Main.MULISH_LIGHT.deriveFont(Math.min((float) getWidth()/2, (float) getHeight()/2))); // font

        // Draw separation line
        g2d.setColor(Color.gray);
        g2d.drawLine(getWidth()/25, getHeight()*9/10, getWidth()*24/25, getHeight()*9/10);

        // Determine what to draw based on if time controls are being used or not
        String timeLeftString;
        String incrementString;

        if (player.getTimeProperty(0) == -1 && player.getTimeProperty(1) == -1) {
            timeLeftString = "--:--";
            incrementString = null;
        } else {
            timeLeftString = player.getTimeProperty(0) + ":" + (player.getTimeProperty(1) < 10 ? "0" : "") + player.getTimeProperty(1);
            incrementString = "+" + player.getTimeProperty(2);
        }

        // Draw time
        Dimension timeDimensions = new Dimension(
                g2d.getFontMetrics(g2d.getFont()).stringWidth(timeLeftString),
                g2d.getFontMetrics(g2d.getFont()).getHeight()
        );

        Point timeBottomRight = new Point(getWidth()/2 - timeDimensions.width/2, getHeight()/2 + timeDimensions.height/4);

        g2d.drawString(timeLeftString, timeBottomRight.x, timeBottomRight.y);

        if (incrementString != null) {
            g2d.setFont(g2d.getFont().deriveFont((float) timeDimensions.height/4));
            g2d.drawString(incrementString, timeBottomRight.x + timeDimensions.width, timeBottomRight.y - timeDimensions.height*3/5);
        }

    }
    public void actionPerformed(ActionEvent ae) {
        repaint();
    }

}
