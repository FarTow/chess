package entities;

public class Pawn extends Piece {
    private boolean enPassantCapturable;

    public Pawn(boolean isWhite, Square square) {
        super(isWhite, square);
        imageName = "pawn";
        initImage();
    }

    @Override
    public boolean canMove(int newRow, int newCol, Square[][] grid) {
        int movementModifier = isWhite ? -1 : 1;

        if (!jumping(newRow, newCol, grid) && !(Math.abs(getCol() - newCol) > 1)) {
            if (grid[newRow][newCol].getPiece() == null) { // move to empty square on same col
                if (getCol() == newCol) {
                    if (firstMove) { // move 2
                        return getRow() + movementModifier * 2 == newRow || getRow() + movementModifier == newRow;
                    }
                    return getRow() + movementModifier == newRow;
                } else { // en passant
                    if (Math.abs(getRow() - newRow) == 1 &&
                            (newRow + movementModifier * -1 >= 0 && newRow + movementModifier * -1 <= 7)) {
                        Square pawnSquare = grid[newRow + movementModifier * -1][newCol];

                        if (pawnSquare.getPiece() instanceof Pawn && isWhite != pawnSquare.getPiece().isWhite) {
                            return ((Pawn) pawnSquare.getPiece()).isEnPassantCapturable();
                        }
                    }
                }
            } else { // move to taken square
                if (getCol() != newCol && isWhite != grid[newRow][newCol].getPiece().isWhite()) {
                    return getRow() - newRow == movementModifier * -1;
                }
            }
        }

        return false;
    }

    public void setEnPassantCapturable(boolean enPassantCapturable) { this.enPassantCapturable = enPassantCapturable; }

    public boolean isEnPassantCapturable() { return enPassantCapturable; }

    @Override
    public char getNotation() { return (char) 0; }
}
