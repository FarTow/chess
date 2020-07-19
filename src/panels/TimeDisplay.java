package panels;

import entities.Player;

import javax.swing.JPanel;
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
    }
    public void actionPerformed(ActionEvent ae) {
        repaint();
    }

}
