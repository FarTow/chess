package panels;

import entities.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TakenPieces extends JPanel {
    private final Image[] pieceImages;
    private int[] deadPieceCount;
    private final boolean isWhite;

    public TakenPieces(boolean isWhite, Board board) {
        setBackground(new Color(229, 228, 228));

        this.isWhite = isWhite;
        pieceImages = new Image[5];
        deadPieceCount = new int[] {0, 0, 0, 0, 0};

        Image[] referencePictures = isWhite ? Main.whitePieceIcons : Main.blackPieceIcons;

        pieceImages[0] = referencePictures[Main.PAWN_IMAGE_INDEX];
        pieceImages[1] = referencePictures[Main.KNIGHT_IMAGE_INDEX];
        pieceImages[2] = referencePictures[Main.BISHOP_IMAGE_INDEX];
        pieceImages[3] = referencePictures[Main.ROOK_IMAGE_INDEX];
        pieceImages[4] = referencePictures[Main.QUEEN_IMAGE_INDEX];
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int rowModifier = -2;
        for (int i=0; i<pieceImages.length; i++, rowModifier++) {
            int pieceSpacingOffset = + rowModifier*pieceImages[i].getWidth(null);

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

            String count = Integer.toString(deadPieceCount[i]);
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
    public void actionPerformed(ActionEvent ae) {
        repaint();
    }
}
