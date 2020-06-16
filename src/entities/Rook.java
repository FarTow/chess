package entities;

import java.awt.Point;

public class Rook extends Piece {

    public Rook(boolean isWhite, int row, int column, Point topLeft) {
        super(isWhite, row, column, topLeft);
        setImage("Rook");
    }

    public boolean canMove(int newRow, int newColumn, Board board, boolean mouseReleased) {
        Square[][] grid = board.getGrid();

        if (isJumping(newRow, newColumn, grid) || sameColor(newRow, newColumn, grid)) return false;

        return row == newRow || column == newColumn;
    }
}
