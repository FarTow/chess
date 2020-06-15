package entities;

import java.awt.Point;

public class Knight extends Piece {
    public Knight(boolean isWhite, int row, int column, Point topLeft) {
        super(isWhite, row, column, topLeft);
        setImage("Knight");
    }

    public boolean canMove(int newRow, int newColumn, Board board, boolean mouseReleased) {
        if (board == null) return false;

        Square[][] grid = board.getGrid();

        if (sameColor(newRow, newColumn, grid)) return false;

        if (row == newRow || column == newColumn ) return false;
        int rowDiff = Math.abs(row-newRow);
        int columnDiff = Math.abs(column-newColumn);

        return (rowDiff+columnDiff == 3);
    }

}
