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

        // Sharpen text
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setFont(Main.MULISH_LIGHT.deriveFont(Math.min((float) getWidth()/2, (float) getHeight()/2)));
        g2d.drawString("2:30", getWidth()/2, getHeight()/2);
    }
    public void actionPerformed(ActionEvent ae) {
        repaint();
    }

}
