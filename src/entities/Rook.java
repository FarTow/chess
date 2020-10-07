package entities;

import panels.Board;

import java.awt.Point;
import java.util.ArrayList;

public class Rook extends Piece {
    public Rook(boolean isWhite, Square square) {
        super(isWhite, square);
        imageName = "rook";
        setImage();
    }

    public void update(Board board) {
        moveableSquares = new ArrayList<>();
        Square[][] grid = board.getGrid();

        for (Square[] squareRow : grid) {
            for (Square square : squareRow) {
                int newRow = square.getRow();
                int newCol = square.getCol();

                if (jumping(newRow, newCol, grid)) {
                    continue;
                }

                if (getRow() == newRow || getCol() == newCol) {
                    moveableSquares.add(square);
                }
            }
        }
    }

    public char getNotation() { return 'R'; }
}
