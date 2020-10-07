package entities;

import panels.Board;

import java.awt.Point;
import java.util.ArrayList;

public class Bishop extends Piece {
    public Bishop(boolean isWhite, Square square) {
        super(isWhite, square);
        imageName = "bishop";
        setImage();
    }

    public void update(Board board) {
        moveableSquares = new ArrayList<>();
        Square[][] grid = board.getGrid();

        for (Square[] squareRow : grid) {
            for (Square square : squareRow) {
                int newRow = square.getRow();
                int newCol = square.getCol();

                if (jumping(newRow, newCol, grid) || getRow() == newRow || getCol() == newCol) {
                    continue;
                }

                if (Math.abs(getRow() - newRow) == Math.abs(getCol() - newCol)) {
                    moveableSquares.add(square);
                }
            }
        }
    }

    public char getNotation() { return 'B'; }
}
