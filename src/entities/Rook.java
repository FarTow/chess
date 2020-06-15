package entities;

import java.awt.Point;

public class Rook extends Piece {
    private boolean firstMove;

    public Rook(boolean isWhite, int row, int column, Point topLeft) {
        super(isWhite, row, column, topLeft);
        setImage("Rook");

        firstMove = true;
    }

    public boolean canMove(int newRow, int newColumn, Board board, boolean mouseReleased) {
        Square[][] grid = board.getGrid();

        if (isJumping(newRow, newColumn, grid) || sameColor(newRow, newColumn, grid)) return false;
        boolean canMove = false;

        canMove = row == newRow || column == newColumn;

        if (canMove && firstMove && mouseReleased) firstMove = false;

        return canMove;
    }

    public boolean isFirstMove() { return firstMove; }
}
