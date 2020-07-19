package panels;

import entities.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TakenPieces extends JPanel {
    private final Image[] pieceImages;
    private final Player player;

    private int[] advantageCount;
    private final boolean isWhite;

    public TakenPieces(Player player) {
        setBackground(new Color(229, 228, 228));
        this.player = player;
        this.isWhite = player.isWhite();
        pieceImages = new Image[5];
        advantageCount = new int[5];

        updateAdvantageCount();

        pieceImages[0] = Main.greyPieceImages[Main.PAWN_IMAGE_INDEX];
        pieceImages[1] = Main.greyPieceImages[Main.KNIGHT_IMAGE_INDEX];
        pieceImages[2] = Main.greyPieceImages[Main.BISHOP_IMAGE_INDEX];
        pieceImages[3] = Main.greyPieceImages[Main.ROOK_IMAGE_INDEX];
        pieceImages[4] = Main.greyPieceImages[Main.QUEEN_IMAGE_INDEX];
    }

    protected void updateAdvantageCount() {
        if (player.getPieces().size() == player.getEnemyPlayer().getPieces().size()) return;

        advantageCount[0] = player.getPieceCount()[0]-player.getEnemyPlayer().getPieceCount()[0];
        advantageCount[1] = player.getPieceCount()[1]-player.getEnemyPlayer().getPieceCount()[1];
        advantageCount[2] = player.getPieceCount()[2]-player.getEnemyPlayer().getPieceCount()[2];
        advantageCount[3] = player.getPieceCount()[3]-player.getEnemyPlayer().getPieceCount()[3];
        advantageCount[4] = player.getPieceCount()[4]-player.getEnemyPlayer().getPieceCount()[4];

    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int rowModifier = -2;
        for (int i=0; i<pieceImages.length; i++, rowModifier++) {

            int pieceSpacingOffset = rowModifier*pieceImages[i].getWidth(null);

            g.drawImage(
                    pieceImages[i],
                    (getWidth()/2 - pieceImages[i].getWidth(null)/2) + pieceSpacingOffset,
                    0,
                    null
            );

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(Color.black);
            g2d.setFont(Main.MULISH_LIGHT.deriveFont(Math.min((float) getWidth()/30, (float) getHeight()/20)));

            String count;
            if (advantageCount[i] == 0) {
                count = "-";
            } else {
                count = Integer.toString(advantageCount[i]);
            }
            Dimension countDimensions = new Dimension(
                    g2d.getFontMetrics(g.getFont()).stringWidth(count),
                    g2d.getFontMetrics(g.getFont()).getHeight()
            );

            g2d.drawString(
                    count,
                    (getWidth()/2 - pieceImages[i].getWidth(null)/2 + countDimensions.width*7/2) + pieceSpacingOffset,
                    pieceImages[i].getHeight(null)+countDimensions.height
            );
        }
    }
    public void actionPerformed(ActionEvent ae){
        updateAdvantageCount();
        repaint();
    }
}
