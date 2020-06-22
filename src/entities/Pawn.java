package entities;

import panels.Board;

import java.awt.Point;

public class Pawn extends Piece {
    private boolean movedTwo, enPassantCapturable, promotable;

    public Pawn(boolean isWhite, int row, int column, Point topLeft) {
        super(isWhite, row, column, topLeft);
        setImage("pawn");
    }

    public boolean canMove(int newRow, int newColumn, Board board, boolean mouseReleased) {
        Square[][] grid = board.getGrid();

        if (isJumping(newRow, newColumn, grid)) return false;
        if (Math.abs(column-newColumn) > 1) return false;

        boolean canMove = false;
        int movementModifier = isWhite ? -1 : 1;

        if (grid[newRow][newColumn].getPiece() == null) { // moving to a spot with no piece
            if (column == newColumn) { // moving to a spot in the same column
                if (firstMove) { // move two on first turn
                    canMove = (row + movementModifier == newRow || row + movementModifier*2 == newRow);
                } else {
                    canMove = (row + movementModifier == newRow);
                }
            } else { // only other case is en passant when moving to a spot with no piece
                if (Math.abs(row-newRow) == 1) {
                    if (!(newRow + movementModifier*-1 >= 0 && newRow + movementModifier*-1 <=7)) return false;
                    Square currentSquare = grid[newRow + movementModifier*-1][newColumn];

                    if (currentSquare.getPiece() instanceof Pawn) {
                        canMove = ((Pawn) currentSquare.getPiece()).isEnPassantCapturable();
                        if (canMove && mouseReleased) currentSquare.setPiece(null);
                    }
                }
            }
        } else { // moving to a spot with a piece
            if (column != newColumn) { // moving to a spot on a different column
                if (isWhite != grid[newRow][newColumn].getPiece().isWhite()) { // the piece is a different color
                    canMove = row - newRow == movementModifier * -1;
                }
            }
        }

        if (mouseReleased && canMove && firstMove && board.mayMove(this, grid[newRow][newColumn])) {
            if (row + movementModifier * 2 == newRow) movedTwo = true;
            if (movedTwo) enPassantCapturable = true;
        }

        return canMove;
    }
    public void update(boolean whiteTurn) {
        if (isWhite ? row==0 : row==7) promotable = true;

        if(whiteTurn == isWhite) {
            if (movedTwo) movedTwo = false;
            if (enPassantCapturable) enPassantCapturable = false;
        }
    }

    public boolean isEnPassantCapturable() { return enPassantCapturable; }
    public boolean isPromotable() { return promotable; }
    public String getSymbol() { return isWhite ? "♙" : "♟"; }
}
