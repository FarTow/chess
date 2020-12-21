package entities;

import panels.Board;

import java.awt.*;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.ArrayList;

/**
 * Abstract class containing all standard characteristics of a chess piece
 */
public abstract class Piece {
    protected String imageName;
    protected Square square;
    protected Point topLeft;
    protected ArrayList<Square> moveableSquares;
    protected Image image;
    protected Image defaultImage;
    protected boolean isWhite;
    protected boolean firstMove;

    /**
     * Default constructor for a chess piece
     * @param isWhite color of the piece
     * @param square initial square to be placed on
     */
    public Piece(boolean isWhite, Square square) {
        imageName = "";
        this.isWhite = isWhite;
        this.square = square;
        topLeft = square.getTopLeft();
        firstMove = true;
        moveableSquares = new ArrayList<>();
    }

    /**
     * Update this piece's available squares to move to
     * <br>pre: board != null
     * @param board board being played on
     */
    public void update(Board board) {
        if (board == null) {
            throw new IllegalArgumentException("Violation of pre-condition: board != null");
        }

        moveableSquares = new ArrayList<>();
        Square[][] grid = board.getGrid();

        for (Square[] squareRow : grid) {
            for (Square square : squareRow) {
                if (canMove(square.getRow(), square.getCol(), grid)) {
                    moveableSquares.add(square);
                }
            }
        }
    }

    /**
     * Check if this piece MAY move to a certain square
     * (square is stored in moveableSquares)
     * <br>pre: toSquare != null
     * @param toSquare destination square to see if possible to move to
     * @return boolean of if piece MAY move to given square
     */
    public boolean mayMove(Square toSquare) {
        if (toSquare == null) {
            throw new IllegalArgumentException("Violation of pre-condition: toSquare != null");
        }

        return moveableSquares.contains(toSquare);
    }

    /**
     * Check if this piece CAN move to a certain square
     * (square is technically possible to move to)
     * @param newRow row of the square wanted to move to
     * @param newCol column of the square wanted to move to
     * @param grid board in 2D array form
     * @return boolean of if piece CAN move to given square
     */
    public abstract boolean canMove(int newRow, int newCol, Square[][] grid);


    /**
     * Check if a piece is "jumping" over another piece
     * <br> pre: newRow and newCol are valid squares on the board, grid != null
     * @param newRow row of the square to be checked
     * @param newCol column of the square to be checked
     * @param grid board in 2D array
     * @return boolean of if given square is being jumped over
     */
    protected boolean jumping(int newRow, int newCol, Square[][] grid) {
        if (!validSquareOnBoard(newRow, newCol) || grid == null) {
            throw new IllegalArgumentException("Violation of pre-condition: " +
                    "newRow and newCol are valid squares on the board, grid != null");
        }

        return (jumpingVert(newRow, newCol, grid) ||
                jumpingHoriz(newRow, newCol, grid) ||
                jumpingDiag(newRow, newCol, grid));
    }

    // Determine if the input square is being "jumped" over vertically
    // newRow: row of square to be checked
    // newCol: column of the square to be checked
    // returns boolean of if given square is being jumped over vertically
    private boolean jumpingVert(int newRow, int newCol, Square[][] grid) {
        if (square.getCol() != newCol) {
            return false;
        }

        boolean posRowDiff = square.getRow() < newRow;
        int incRowBy = posRowDiff ? 1 : -1;

        for (int currRow = square.getRow() + incRowBy;
             (posRowDiff ? currRow<newRow : currRow>newRow);
             currRow += incRowBy) {
            if (grid[currRow][square.getCol()].getPiece() != null) {
                return true;
            }
        }

        return false;
    }

    // Determine if the input square is being "jumped" over horizontally
    // newRow: row of square to be checked
    // newCol: column of the square to be checked
    // returns boolean of if given square is being jumped over horizontally
    private boolean jumpingHoriz(int newRow, int newCol, Square[][] grid) {
        if (square.getRow() != newRow) {
            return false;
        }

        boolean posColDiff = square.getCol() < newCol;
        int incColBy = posColDiff ? 1 : -1;

        for (int currCol = square.getCol() + incColBy;
             (square.getCol()<newCol ? currCol<newCol : currCol>newCol);
             currCol += incColBy) {
            if (grid[square.getRow()][currCol].getPiece() != null) {
                return true;
            }
        }

        return false;
    }

    // Determine if the input square is being "jumped" over diagonally
    // newRow: row of square to be checked
    // newCol: column of the square to be checked
    // returns boolean of if given square is being jumped over diagonally
    private boolean jumpingDiag(int newRow, int newCol, Square[][] grid) {
        int rowDiff = square.getRow()-newRow; // positive result = moving "up"
        int colDiff = square.getCol()-newCol; // positive result = moving "left"

        if(Math.abs(rowDiff) != Math.abs(colDiff)) {
            return false;
        }

        boolean posRowDiff = rowDiff > 0;
        boolean posColDiff = colDiff > 0;
        int incRowBy = (posRowDiff ? -1 : 1);
        int incColBy = (posColDiff ? -1 : 1);
        int currRow = square.getRow() + incRowBy;
        int currCol = square.getCol() + incColBy;

        while ((posRowDiff ? currRow > newRow : currRow < newRow) &&
                (posColDiff ? currCol > newCol : currCol < newCol)) {
            if (grid[currRow][currCol].getPiece() != null) {
                return true;
            }

            currRow += incRowBy;
            currCol += incColBy;
        }
        return false;
    }


    // Helper method to determine if a given piece is a valid square on a standard chess board
    // row: row of square to be checked
    // col: column of square to be checked
    // returns boolean of if given square is on a standard chess board
    private boolean validSquareOnBoard(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8; // magic numbers
    }

    // Setters
    protected void initImage() {
        try {
            image = defaultImage = ImageIO.read(new File("res/pieces/" +
                    (isWhite ? "white" : "black") + "-" + imageName + ".png"));
        } catch (Exception e) {
            throw new IllegalStateException("Unrecognized image");
        }
    }

    public void setTopLeft(Point topLeft) {
        this.topLeft = topLeft;
    }

    public void scaleImage(int length) {
        image = defaultImage.getScaledInstance(length, length, 0);
    }

    public void setSquare(Square square) {
        this.square = square;
    }

    public void setPos(Point pos) {
        this.topLeft = new Point(pos.x-image.getWidth(null)/2,
                pos.y-image.getHeight(null)/2);
    }

    public void setFirstMove(boolean firstMove) {
        this.firstMove = firstMove;
    }

    public void setMoveableSquares(ArrayList<Square> moveableSquares) {
        this.moveableSquares = moveableSquares;
    }

    // Getters
    public abstract char getNotation();

    public Square getSquare() {
        return square;
    }

    public Image getImage() {
        return image;
    }

    public Point getTopLeft() {
        return topLeft;
    }

    public Point getPos() {
        return new Point(topLeft.x+image.getWidth(null)/2,
                topLeft.y+image.getHeight(null)/2);
    }

    public int getRow() {
        return square.getRow();
    }

    public int getCol() {
        return square.getCol();
    }

    public ArrayList<Square> getMoveableSquares() {
        return moveableSquares;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public boolean isFirstMove() {
        return firstMove;
    }
}
