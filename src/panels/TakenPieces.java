package panels;

import entities.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TakenPieces extends JPanel {
    private final Image[] pieceImages;

    public TakenPieces(boolean isWhite) {
        setBackground(new Color(229, 228, 228));
        pieceImages = new Image[5];

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
            g.drawImage(pieceImages[i], (getWidth()/2 - pieceImages[i].getWidth(null)/2) + (rowModifier*pieceImages[i].getWidth(null)), 0, null);
        }
    }
    public void actionPerformed(ActionEvent ae) {
        repaint();
    }
}
