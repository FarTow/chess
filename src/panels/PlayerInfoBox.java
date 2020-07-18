package panels;

import entities.Player;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;

public class PlayerInfoBox extends JPanel {
    private final Player player;

    private JLabel label;
    private final TimeDisplay timer;
    private final TakenPieces takenPieces;

    public PlayerInfoBox(Player player) {
        this.player = player;

        timer = new TimeDisplay(player);
        takenPieces = new TakenPieces(player);

        initUI();
    }

    public void initUI() {
        setBackground(new Color(234, 229, 221));
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        label = new JLabel((player.isWhite() ? "White" : "Black") + " Stats", JLabel.CENTER) {
            public void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.translate(-getWidth()/4, 0);
                g2d.rotate(-Math.PI/2,(getX() + getWidth()/2.0f), (getY() + getHeight()/2.0f));

                super.paintComponent(g);

                g2d.dispose();
            }
        };
        label.setFont((Main.MULISH_LIGHT.deriveFont(20.0f))); // configure settings
        label.setOpaque(true);

        // === SCUFFED VERTICAL === //
        //String labelText = player.isWhite() ? "<html>W<br>H<br>I<br>T<br>E<br>" : "<html>B<br>L<br>A<br>C<br>K<br>";
        //label = new JLabel("<html><br>" + labelText + " <html><br>S<br>T<br>A<br>T<br>S");
        //label.setVerticalAlignment(JLabel.TOP);
        //label.setHorizontalAlignment(JLabel.CENTER);

        Main.setGridBagLayoutConstraints(
                c, new Insets(0, 0, 0, 0), GridBagConstraints.BOTH,
                0, 0, 1, 2, 0.0, .8, GridBagConstraints.PAGE_START);
        add(label, c);

        Main.setGridBagLayoutConstraints(
                c, new Insets(0, 0, 0, 0), GridBagConstraints.BOTH,
                1, 0, 1, 1, 1.0, 0.2, GridBagConstraints.CENTER);
        add(timer, c);

        Main.setGridBagLayoutConstraints(
                c, new Insets(0, 0, 0, 0), GridBagConstraints.BOTH,
                1, 1, 1, 1, 1.0, 0.8, GridBagConstraints.LINE_END);
        add(takenPieces, c);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.black);
    }

    public void actionPerformed(ActionEvent ae) {
        timer.actionPerformed(ae);
        takenPieces.actionPerformed(ae);

        repaint();
    }
}
