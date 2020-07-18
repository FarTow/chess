package entities;

import panels.Board;

import java.awt.Point;
import java.util.ArrayList;

public class Rook extends Piece {
    public Rook(boolean isWhite, Square square) {
        super(isWhite, square);
        setImage("rook");
    }

    public Rook(boolean isWhite) {
        super(isWhite);
    }

    public void update(Board board) {
        moveableSquares = new ArrayList<>();
        Square[][] grid = board.getGrid();

        for (Square[] squareRow : grid) {
            for (Square square : squareRow) {
                int newRow = square.getRow();
                int newColumn = square.getColumn();

                if (isJumping(newRow, newColumn, grid)) continue;

                if (getRow() == newRow || getColumn() == newColumn) moveableSquares.add(square);
            }
        }
    }

    public char getNotation() { return 'R'; }
}
