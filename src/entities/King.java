package entities;

import java.awt.Point;

public class King extends Piece {
    private boolean firstMove, check;

    public King(boolean isWhite, int row, int column, Point pos) {
        super(isWhite, row, column, pos);
        setImage("King");

        firstMove = true;
    }

    public boolean canMove(int newRow, int newColumn, Board board, boolean mouseReleased) {
        Square[][] grid = board.getGrid();

        if (isJumping(newRow, newColumn, grid) || sameColor(newRow, newColumn, grid)) return false;
        boolean canMove = false;

        int rowDiff = Math.abs(row-newRow);
        int columnDiff = Math.abs(column-newColumn);

        if (columnDiff == 2 && rowDiff == 0) { // if player moves king two spaces horizontally
            if (firstMove) { // if it's the king's first move
                Piece rook = column-newColumn < 0 ? grid[row][7].getPiece() : grid[row][0].getPiece(); // rook on the side player moved

                if (rook instanceof Rook) { // piece is a rook
                    if (((Rook) rook).isFirstMove() && rook.isWhite() == isWhite) { // rook's first move
                        canMove = true;
                        if (mouseReleased) board.movePiece(rook, grid[row][column-newColumn < 0 ? 5 : 3], true);
                    }
                }
            }
        } else {
            canMove = (rowDiff + columnDiff <= 2 && rowDiff != 2);
        }

        if (canMove && firstMove && mouseReleased) {
            System.out.println("first move is now false");
            firstMove = false;
        }
        return canMove;
    }

    public void setCheck(boolean check) { this.check = check; }

    public boolean getCheck() { return check; }
}
