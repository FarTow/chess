package panels;

import entities.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PieceDiffDisplay extends JPanel { // pawn promotion lags
    private final Image[] pieceImages;
    private final Player player;

    private final int[] diffCount;

    public PieceDiffDisplay(Player player) {
        setBackground(new Color(229, 228, 228));
        this.player = player;
        pieceImages = new Image[5];
        diffCount = new int[5];

        updateDiffCount();

        pieceImages[0] = Main.greyPieceImages[Main.PAWN_IMAGE_INDEX];
        pieceImages[1] = Main.greyPieceImages[Main.KNIGHT_IMAGE_INDEX];
        pieceImages[2] = Main.greyPieceImages[Main.BISHOP_IMAGE_INDEX];
        pieceImages[3] = Main.greyPieceImages[Main.ROOK_IMAGE_INDEX];
        pieceImages[4] = Main.greyPieceImages[Main.QUEEN_IMAGE_INDEX];
    }

    protected void updateDiffCount() {
        diffCount[0] = player.getPieceCount()[0]-player.getEnemyPlayer().getPieceCount()[0];
        diffCount[1] = player.getPieceCount()[1]-player.getEnemyPlayer().getPieceCount()[1];
        diffCount[2] = player.getPieceCount()[2]-player.getEnemyPlayer().getPieceCount()[2];
        diffCount[3] = player.getPieceCount()[3]-player.getEnemyPlayer().getPieceCount()[3];
        diffCount[4] = player.getPieceCount()[4]-player.getEnemyPlayer().getPieceCount()[4];
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int rowModifier = -2;
        for (int i=0; i<pieceImages.length; i++, rowModifier++) {
            int pieceSpacingOffset = rowModifier*pieceImages[i].getWidth(null);
            Point pieceImageTopLeft = new Point((getWidth()/2 - pieceImages[i].getWidth(null)/2) + pieceSpacingOffset, 0);

            // Draw image at specified location
            g.drawImage(pieceImages[i], pieceImageTopLeft.x, pieceImageTopLeft.y, null);

            // Edit graphics as desired
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // sharper text

            g2d.setColor(Color.black); // black text
            g2d.setFont(Main.MULISH_LIGHT.deriveFont(Math.min((float) getWidth()/15, (float) getHeight()/15))); // font

            // Draw the piece difference
            String count;
            if (diffCount[i] == 0) {
                count = "-";
            } else if (diffCount[i] < 0) {
                count = diffCount[i] + " ";
            } else {
                count = "+" + diffCount[i] + " ";
            }

            Dimension countDimensions = new Dimension(
                    g2d.getFontMetrics(g.getFont()).stringWidth(count),
                    g2d.getFontMetrics(g.getFont()).getHeight()
            );

            g2d.drawString(
                    count,
                    pieceImageTopLeft.x + pieceImages[i].getWidth(null)/2 - countDimensions.width/2,
                    pieceImageTopLeft.y + pieceImages[i].getHeight(null)*5/4
            );
        }
    }
    public void actionPerformed(ActionEvent ae){
        updateDiffCount();
        repaint();
    }
}
