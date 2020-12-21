package entities;

public class Bishop extends Piece {
    public Bishop(boolean isWhite, Square square) {
        super(isWhite, square);
        imageName = "bishop";
        initImage();
    }

    @Override
    public boolean canMove(int newRow, int newCol, Square[][] grid) {
        if (jumping(newRow, newCol, grid) || getRow() == newRow || getCol() == newCol) {
            return false;
        }

        return Math.abs(getRow() - newRow) == Math.abs(getCol() - newCol);
    }

    @Override
    public char getNotation() { return 'B'; }
}
