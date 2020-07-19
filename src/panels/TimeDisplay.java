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

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Initialize graphics
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // sharper text
        g2d.setFont(Main.MULISH_LIGHT.deriveFont(Math.min((float) getWidth()/2, (float) getHeight()/2))); // font

        // Draw separation line
        g2d.setColor(Color.gray);
        g2d.drawLine(getWidth()/25, getHeight()*9/10, getWidth()*24/25, getHeight()*9/10);

        // Draw time
        Dimension timeDimensions = new Dimension(
                g2d.getFontMetrics(g2d.getFont()).stringWidth("2:30"),
                g2d.getFontMetrics(g2d.getFont()).getHeight()
        );
        g2d.setColor(Color.black);
        g2d.drawString("2:30", getWidth()/2 - timeDimensions.width/2, getHeight()/2 + timeDimensions.height/4);
    }
    public void actionPerformed(ActionEvent ae) {
        repaint();
    }

}
