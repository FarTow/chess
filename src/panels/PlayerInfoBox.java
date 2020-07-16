package panels;

import entities.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PlayerInfoBox extends JPanel {
    private Player player;

    private TimeDisplay timer;
    private TakenPieces takenPieces;

    public PlayerInfoBox(Player player) {
        this.player = player;

        timer = new TimeDisplay(player);
        takenPieces = new TakenPieces(player);

        initUI();
    }

    public void initUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        Main.forceSize(new Dimension(getWidth()/4, getHeight()/10), timer); // force the size of info box components

        add(new JLabel((player.isWhite() ? "White" : "Black") + " Stats", JLabel.CENTER)); // add components to holders
        add(timer);
        add(takenPieces);

        getComponent(0).setFont(new Font("Serif", Font.PLAIN, 20)); // configure settings
        ((JComponent) getComponent(0)).setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    public void actionPerformed(ActionEvent ae) {
        timer.actionPerformed(ae);
        takenPieces.actionPerformed(ae);

        repaint();
    }
}
