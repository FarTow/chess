package entities;

import panels.Board;

public class King extends Piece {
    public King(boolean isWhite, Square square) {
        super(isWhite, square);
        imageName = "king";
        setImage();
    }

    @Override
    public boolean canMove(int newRow, int newCol, Square[][] grid) {
        int rowDiff = Math.abs(getRow() - newRow);
        int columnDiff = Math.abs(getCol() - newCol);

        if (jumping(newRow, newCol, grid) || rowDiff >= 2 || columnDiff >= 3) {
            return false;
        }

        if (columnDiff == 2 && rowDiff == 0) { // if player moves king two spaces horizontally
            if (firstMove) { // if it's the king's first move
                boolean kingSideCastle = getCol() - newCol < 0;
                Piece rook = kingSideCastle ? grid[getRow()][7].getPiece() : grid[getRow()][0].getPiece();

                if (rook instanceof Rook) { // piece is a rook
                    return rook.isFirstMove() && rook.isWhite() == isWhite; // rook's first move
                }
            }
        } else {
            return rowDiff + columnDiff <= 2 && columnDiff <= 1;
        }

        return false;
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

    @Override
    public char getNotation() { return 'K'; }
}
