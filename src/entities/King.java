package entities;

import panels.Board;
import java.util.ArrayList;

public class King extends Piece {
    private int castled; // 0 -> not castled, 1 -> castled king's side, 2 -> castled queen's side

    public King(boolean isWhite, Square square) {
        super(isWhite, square);
        setImage("king");
    }

    public void update(Board board) {
        moveableSquares = new ArrayList<>();
        Square[][] grid = board.getGrid();

        for (Square[] squareRow : grid) {
            for (Square square : squareRow) {
                int newRow = square.getRow();
                int newColumn = square.getColumn();
                int rowDiff = Math.abs(getRow() - newRow);
                int columnDiff = Math.abs(getColumn() - newColumn);

                if (isJumping(newRow, newColumn, grid)) continue;
                if (rowDiff >= 2 || columnDiff >= 3) continue;

                if (columnDiff == 2 && rowDiff == 0) { // if player moves king two spaces horizontally
                    if (firstMove) { // if it's the king's first move
                        boolean kingSideCastle = getColumn() - newColumn < 0;
                        Piece rook = kingSideCastle ? grid[getRow()][7].getPiece() : grid[getRow()][0].getPiece(); // rook on the side player moved

                        if (rook instanceof Rook) { // piece is a rook
                            if (rook.isFirstMove() && rook.isWhite() == isWhite && castled == 0) { // rook's first move
                                moveableSquares.add(square);
                            }
                        }
                    }
                } else {
                    if (rowDiff + columnDiff <= 2 && columnDiff <= 1) {
                        moveableSquares.add(square);
                    }
                }

            }
        }

        if(isWhite == board.getWhiteTurn()) {
            if (castled != 0) castled = 0;
        }
    }

    public void setCastled(int castled) { this.castled = castled; }

    public int getCastled() { return castled; }
    public char getSymbol() { return isWhite ? '♔' : '♚'; }
}
