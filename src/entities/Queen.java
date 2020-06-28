package entities;

import panels.Board;

import java.awt.Point;
import java.util.ArrayList;

public class Queen extends Piece {
    public Queen(boolean isWhite, int row, int column, Point topLeft) {
        super(isWhite, row, column, topLeft);
        setImage("queen");
    }

    public void update(Board board) {
        moveableSquares = new ArrayList<>();
        Square[][] grid = board.getGrid();

        for (Square[] squareRow : grid) {
            for (Square square : squareRow) {
                int newRow = square.getRow();
                int newColumn = square.getColumn();

                if (isJumping(newRow, newColumn, grid)) continue;

                if (row == newRow || column == newColumn) moveableSquares.add(square);
                if (Math.abs(row-newRow) == Math.abs(column-newColumn)) moveableSquares.add(square);
            }
        }
    }

    public char getSymbol() { return isWhite ? '♕' : '♛'; }
}
