package entities;

import panels.Board;

import java.awt.Point;

public class King extends Piece {
    private boolean check;
    private int castled; // 0 -> not castled, 1 -> castled king's side, 2 -> castled queen's side

    public King(boolean isWhite, int row, int column, Point pos) {
        super(isWhite, row, column, pos);
        setImage("king");
    }

    public boolean canMove(int newRow, int newColumn, Board board, boolean mouseReleased) {
        Square[][] grid = board.getGrid();

        if (isJumping(newRow, newColumn, grid)) return false;
        boolean canMove = false;

        int rowDiff = Math.abs(row-newRow);
        int columnDiff = Math.abs(column-newColumn);

        if (columnDiff == 2 && rowDiff == 0) { // if player moves king two spaces horizontally
            if (firstMove) { // if it's the king's first move
                Piece rook = column-newColumn < 0 ? grid[row][7].getPiece() : grid[row][0].getPiece(); // rook on the side player moved

                if (rook instanceof Rook) { // piece is a rook
                    if (rook.isFirstMove() && rook.isWhite() == isWhite && castleable((Rook) rook, board)) { // rook's first move
                        canMove = true;
                        if (mouseReleased) {
                            board.movePiece(rook, grid[row][column - newColumn < 0 ? 5 : 3], true);
                            castled = column-newColumn>0 ? 2 : 1;
                        }
                    }
                }
            }
        } else {
            canMove = (rowDiff + columnDiff <= 2 && rowDiff != 2);
        }

        return canMove;
        //if (mouseReleased && castled) castled = false;
    }

    public void update(Board board) {

        if(isWhite == board.getWhiteTurn()) {
            if (castled>0) castled = 0;
        }
    }

    public boolean castleable(Rook rook, Board board) {
        Square[][] grid = board.getGrid();
        int dangerSquares = 0;
        boolean castleLeftRook = rook.getColumn() > column;

        for (int currentColumn=column+(castleLeftRook ? 1 : -1);
             (castleLeftRook ? currentColumn<rook.getColumn() : currentColumn>rook.getColumn());
             currentColumn += (castleLeftRook ? 1 : -1)) {

            if (!board.mayMove(this, grid[row][currentColumn])) dangerSquares++;
        }

        return dangerSquares == 0;
    }

    public void setCheck(boolean check) { this.check = check; }

    public boolean getCheck() { return check; }
    public int getCastled() { return castled; }
    public char getSymbol() { return isWhite ? '♔' : '♚'; }
}
