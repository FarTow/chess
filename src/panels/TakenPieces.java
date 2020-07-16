package panels;

import entities.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TakenPieces extends JPanel {
    private final Player player;

    public TakenPieces(Player player) {
        setOpaque(false);
        this.player = player;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (player.getDeadPieces().size() == 0) return;

        for (int i=0; i<player.getDeadPieces().size(); i++) {
            if (player.getDeadPieces().get(i) != null) {
                g.drawImage(player.getDeadPieces().get(i).getImage(),
                        i * player.getDeadPieces().get(i).getImage().getWidth(null), 0, null);
            }
        }
    }
    public void actionPerformed(ActionEvent ae) {
        repaint();
    }
}
