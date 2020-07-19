package panels;

import entities.Player;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Rectangle2D;

public class PlayerInfoBox extends JPanel {
    private final Player player;

    private JPanel labelPanel;
    private final TimeDisplay timer;
    private final TakenPieces takenPieces;

    public PlayerInfoBox(Player player, Board board) {
        this.player = player;

        timer = new TimeDisplay(player);
        takenPieces = new TakenPieces(!player.isWhite(), board);

        initUI();
    }

    public void initUI() {
        setBackground(new Color(234, 229, 221));
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        labelPanel = new JPanel() {
            public void paintComponent(Graphics g) { // Tanvir carrying me once again
                super.paintComponent(g);

                Graphics2D g2d = (Graphics2D) g;

                // Initialize g2d properties
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.black);
                g2d.setFont(Main.MULISH_LIGHT.deriveFont((float) getHeight()/15));

                // Create and draw text
                String label = (player.isWhite() ? "White" : "Black") + " Stats";
                Dimension labelDimensions = new Dimension(g2d.getFontMetrics(g2d.getFont()).stringWidth(label), g2d.getFontMetrics(g2d.getFont()).getHeight());

                g2d.rotate(-Math.PI/2,(getX() + getWidth()/2.0f), (getY() + getHeight()/2.0f)); // rotate -90 degrees
                g2d.drawString(label, -(getWidth()/2 + labelDimensions.width/4), getHeight()/2 + labelDimensions.height/3); // draw the string (x and y reversed since we rotated)
                g2d.dispose();
            }
        };

        Main.setGridBagLayoutConstraints(
                c, new Insets(0, 0, 0, 0), GridBagConstraints.VERTICAL,
                0, 0, 1, 2, 0.0, .8, GridBagConstraints.CENTER);
        add(labelPanel, c);

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
    }

    public void actionPerformed(ActionEvent ae) {
        timer.actionPerformed(ae);
        takenPieces.actionPerformed(ae);

        labelPanel.repaint();
        repaint();
    }

    public JPanel getLabelPanel() { return labelPanel; }
}
