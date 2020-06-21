package entities;

import java.awt.Point;

public class Queen extends Piece {
    public Queen(boolean isWhite, int row, int column, Point topLeft) {
        super(isWhite, row, column, topLeft);
        setImage("queen");
    }

    public boolean canMove(int newRow, int newColumn, Board board, boolean mouseReleased) {
        Square[][] grid = board.getGrid();

        if (isJumping(newRow, newColumn, grid) || sameColor(newRow, newColumn, grid)) return false;
        if (row == newRow || column == newColumn) return true;

        int rowDiff = Math.abs(row-newRow);
        int columnDiff = Math.abs(column-newColumn);

        return (rowDiff == columnDiff);
    }

    public String getSymbol() { return isWhite ? "♕" : "♛"; }
}
