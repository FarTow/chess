package entities;

import panels.Board;

import java.util.ArrayList;

public class Knight extends Piece {
    public Knight(boolean isWhite, Square square) {
        super(isWhite, square);
        imageName = "knight";
        setImage();
    }

    public void update(Board board) {
        moveableSquares = new ArrayList<>();
        Square[][] grid = board.getGrid();

        for (Square[] squareRow : grid) {
            for (Square square : squareRow) {
                int newRow = square.getRow();
                int newCol = square.getCol();

                if (getRow() == newRow || getCol() == newCol) continue;

                if ((Math.abs(getRow() - newRow) + Math.abs(getCol() - newCol)) == 3) moveableSquares.add(square);
            }
        }
    }

    public char getNotation() { return 'N'; }
}
