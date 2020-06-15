package entities;

import java.awt.Point;

public class Bishop extends Piece {
    public Bishop(boolean isWhite, int row, int column, Point topLeft) {
        super(isWhite, row, column, topLeft);
        setImage("Bishop");
    }

    public boolean canMove(int newRow, int newColumn, Board board, boolean mouseReleased) {
        Square[][] grid = board.getGrid();

        if (isJumping(newRow, newColumn, grid) || sameColor(newRow, newColumn, grid)) return false;

        int rowDiff = Math.abs(row-newRow);
        int columnDiff = Math.abs(column-newColumn);

        return (rowDiff == columnDiff);
    }

}
