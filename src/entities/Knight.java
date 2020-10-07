package entities;

import panels.Board;

import java.util.ArrayList;

public class Knight extends Piece {
    public Knight(boolean isWhite, Square square) {
        super(isWhite, square);
        imageName = "knight";
        setImage();
    }

    @Override
    public boolean canMove(int newRow, int newCol, Square[][] grid) {
        if (getRow() == newRow || getCol() == newCol) {
            return false;
        }

        return Math.abs(getRow() - newRow) + Math.abs(getCol() - newCol) == 3;
    }

    public char getNotation() { return 'N'; }
}
