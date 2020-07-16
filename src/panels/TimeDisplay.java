package panels;

import entities.Player;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TimeDisplay extends JPanel {
    private final Player player;

    public TimeDisplay(Player player) {
        //setOpaque(false);
        this.player = player;

        setBackground(player.isWhite() ? Color.white : Color.black);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
    public void actionPerformed(ActionEvent ae) {
        repaint();
    }

}
