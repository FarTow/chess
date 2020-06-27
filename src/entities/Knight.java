package entities;

import panels.Board;

import java.awt.Point;

public class Knight extends Piece {
    public Knight(boolean isWhite, int row, int column, Point topLeft) {
        super(isWhite, row, column, topLeft);
        setImage("knight");
    }

    public void update(Board board) {
        Square[][] grid = board.getGrid();

        for (Square[] squareRow : grid) {
            for (Square square : squareRow) {
                int newRow = square.getRow();
                int newColumn = square.getColumn();

                if (!isJumping(newRow, newColumn, grid)) {
                    if (Math.abs(row-newRow)+Math.abs(column-newColumn) == 3) moveableSquares.add(square);
                }
            }
        }
    }

    public char getSymbol() { return isWhite ? '♘' : '♞'; }
}
