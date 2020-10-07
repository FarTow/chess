package entities;

public class Rook extends Piece {
    public Rook(boolean isWhite, Square square) {
        super(isWhite, square);
        imageName = "rook";
        setImage();
    }

    @Override
    public boolean canMove(int newRow, int newCol, Square[][] grid) {
        if (jumping(newRow, newCol, grid)) {
            return false;
        }

        return getRow() == newRow || getCol() == newCol;
    }

    @Override
    public char getNotation() { return 'R'; }
}
