package panels;

import entities.Piece;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class TakenPieces extends GameComponent {
    private boolean isWhite;
    private ArrayList<Piece> takenPieces;

    public TakenPieces(Point initialTopLeft, boolean isWhite, Board board) {
        super(initialTopLeft);
    }

    public void resize(int ... properties) {

    }

    public void actionPerformed(ActionEvent ae) {

    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Piece piece : takenPieces) {
            System.out.println(piece.getSymbol());
        }
    }
}
