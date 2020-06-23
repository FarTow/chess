package panels;

import entities.Piece;

import java.awt.*;
import java.awt.event.ActionEvent;

import java.util.ArrayList;
import java.util.Arrays;

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

        for (Piece takenPiece : takenPieces) {
            g.drawImage(takenPiece.getImage(), 200, 200, null);
            System.out.println("drew piece");
        }

        g.setColor(Color.red);
        g.fillRect(100, 100, 100, 100);

        System.out.println(Arrays.deepToString(takenPieces.toArray()));
    }
}
