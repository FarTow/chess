package panels;

import entities.Piece;

import java.awt.*;
import java.awt.event.ActionEvent;

import java.util.ArrayList;

public class TakenPieces extends GameComponent {
    private final Board board;
    private final ArrayList<Piece> takenPieces;

    private final boolean trackWhite;
    private boolean whiteTurn;

    public TakenPieces(Point initialTopLeft, boolean trackWhite, Board board) {
        super(initialTopLeft);

        this.board = board;
        takenPieces = new ArrayList<>();
        this.trackWhite = trackWhite;
        whiteTurn = true;
    }

    public void resize(int ... properties) {
        int newImageSize = properties[0];

        for (Piece takenPiece : takenPieces) {
            takenPiece.scaleImage(newImageSize);
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (whiteTurn != board.getWhiteTurn()) {
            if (board.getTakenPiece() != null) {
                if (board.getTakenPiece().isWhite() == trackWhite) takenPieces.add(board.getTakenPiece());
            }

            whiteTurn = board.getWhiteTurn();
        }

        repaint();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (takenPieces.size() == 0) return;

        for (int i=0; i<takenPieces.size(); i++) {
            g.drawImage(takenPieces.get(i).getImage(), i*60, 0, null);
        }
    }
}
