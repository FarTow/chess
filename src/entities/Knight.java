package entities;

import panels.Board;

import java.awt.Point;
import java.util.ArrayList;

public class Knight extends Piece {

    public Knight(boolean isWhite, Square square) {
        super(isWhite, square);
        setImage("knight");
    }

    public void update(Board board) {
        moveableSquares = new ArrayList<>();
        Square[][] grid = board.getGrid();

        for (Square[] squareRow : grid) {
            for (Square square : squareRow) {
                int newRow = square.getRow();
                int newColumn = square.getColumn();

                if (row == newRow || column == newColumn) continue;

                if ((Math.abs(row - newRow) + Math.abs(column - newColumn)) == 3) moveableSquares.add(square);
            }
        }
    }

    public char getSymbol() { return isWhite ? '♘' : '♞'; }
}
