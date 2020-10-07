package entities;

public class Queen extends Piece {
    public Queen(boolean isWhite, Square square) {
        super(isWhite, square);
        imageName = "queen";
        setImage();
    }

    @Override
    public boolean canMove(int newRow, int newCol, Square[][] grid) {
        if (jumping(newRow, newCol, grid)) {
            return false;
        }

        return getRow() == newRow || getCol() == newCol || Math.abs(getRow()-newRow) == Math.abs(getCol()-newCol);
    }

    @Override
    public char getNotation() { return 'Q'; }
}
