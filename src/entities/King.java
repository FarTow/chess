package entities;

import panels.Board;
import java.util.ArrayList;

public class King extends Piece {
    public King(boolean isWhite, Square square) {
        super(isWhite, square);
        imageName = "king";
        setImage();
    }

    public void update(Board board) {
        moveableSquares = new ArrayList<>();
        Square[][] grid = board.getGrid();

        for (Square[] squareRow : grid) {
            for (Square square : squareRow) {
                int newRow = square.getRow();
                int newCol = square.getCol();
                int rowDiff = Math.abs(getRow() - newRow);
                int columnDiff = Math.abs(getCol() - newCol);

                if (jumping(newRow, newCol, grid)) {
                    continue;
                }
                if (rowDiff >= 2 || columnDiff >= 3) {
                    continue;
                }

                if (columnDiff == 2 && rowDiff == 0) { // if player moves king two spaces horizontally
                    if (firstMove) { // if it's the king's first move
                        boolean kingSideCastle = getCol() - newCol < 0;
                        Piece rook = kingSideCastle ? grid[getRow()][7].getPiece() : grid[getRow()][0].getPiece();

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
        Square kingSide = grid[getRow()][6];
        Square queenSide = grid[getRow()][2];

        if (moveableSquares.contains(kingSide) && !moveableSquares.contains(grid[getRow()][5])) {
            moveableSquares.remove(queenSide);
        }

        if (moveableSquares.contains(kingSide) && !moveableSquares.contains(grid[getRow()][3])) {
            moveableSquares.remove(queenSide);
        }
    }

    public char getNotation() { return 'K'; }
}
