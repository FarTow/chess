package entities;

import panels.Board;

import java.awt.Point;
import java.util.ArrayList;

public class Bishop extends Piece {

    public Bishop(boolean isWhite, Square square) {
        super(isWhite, square);
        setImage("bishop");
    }

    public void update(Board board) {
        moveableSquares = new ArrayList<>();
        Square[][] grid = board.getGrid();

        for (Square[] squareRow : grid) {
            for (Square square : squareRow) {
                int newRow = square.getRow();
                int newColumn = square.getColumn();

                if (isJumping(newRow, newColumn, grid) || getRow() == newRow || getColumn() == newColumn) continue;

                if (Math.abs(getRow() - newRow) == Math.abs(getColumn() - newColumn)) moveableSquares.add(square);
            }
        }
    }

    public char getSymbol() { return isWhite ? '♗' : '♝'; }
}
