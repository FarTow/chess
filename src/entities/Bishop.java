package entities;

import panels.Board;

import java.awt.Point;

public class Bishop extends Piece {
    public Bishop(boolean isWhite, int row, int column, Point topLeft) {
        super(isWhite, row, column, topLeft);
        setImage("bishop");
    }

    public boolean canMove(int newRow, int newColumn, Board board, boolean mouseReleased) {
        Square[][] grid = board.getGrid();

        if (isJumping(newRow, newColumn, grid)) return false;

        int rowDiff = Math.abs(row-newRow);
        int columnDiff = Math.abs(column-newColumn);

        return (rowDiff == columnDiff);
    }

    public void update(Board board) {
        Square[][] grid = board.getGrid();

        for (Square[] squareRow : grid) {
            for (Square square : squareRow) {
                int newRow = square.getRow();
                int newColumn = square.getColumn();

                if (!isJumping(newRow, newColumn, grid)) {
                    if (Math.abs(row-newRow) == Math.abs(column-newColumn)) moveableSquares.add(square);
                }
            }
        }
    }

    public char getSymbol() { return isWhite ? '♗' : '♝'; }
}
