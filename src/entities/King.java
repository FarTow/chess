package entities;

import panels.Board;
import java.util.ArrayList;

public class King extends Piece {
    public King(boolean isWhite, Square square) {
        super(isWhite, square);
        setImage("king");
    }

    public King(boolean isWhite) {
        super(isWhite);
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
                            if (rook.isFirstMove() && rook.isWhite() == isWhite) { // rook's first move
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
    }

    public void castleCheck(Board board) {
        Square[][] grid = board.getGrid();
        Square kingSideCastleSquare = grid[getRow()][6];
        Square queenSideCastleSquare = grid[getRow()][2];

        if (moveableSquares.contains(kingSideCastleSquare) && !moveableSquares.contains(grid[getRow()][5])) {
            moveableSquares.remove(kingSideCastleSquare);
        }

        if (moveableSquares.contains(queenSideCastleSquare) && !moveableSquares.contains(grid[getRow()][3])) {
            moveableSquares.remove(queenSideCastleSquare);
        }
    }

    public char getSymbol() { return isWhite ? '♔' : '♚'; }
}
