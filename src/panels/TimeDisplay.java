package panels;

import entities.Player;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TimeDisplay extends JPanel {
    private final Player player;

    public TimeDisplay(Player player) {
        setOpaque(false);
        this.player = player;
    }

    protected void paintComponent(Graphics g) {
        g.setColor(Color.red);
        g.fillOval(10, 10, 10, 10);
        super.paintComponent(g);
    }
    public void actionPerformed(ActionEvent ae) {
        repaint();
    }

}
