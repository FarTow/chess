package entities;

import panels.Board;

import java.awt.*;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.ArrayList;

public abstract class Piece {
    protected String imageName;
    protected Square square;
    protected Point topLeft;
    protected ArrayList<Square> moveableSquares;
    protected Image image;
    protected Image defaultImage;
    protected boolean isWhite;
    protected boolean firstMove;

    public Piece(boolean isWhite, Square square) {
        imageName = "";
        this.isWhite = isWhite;
        this.square = square;
        topLeft = square.getTopLeft();
        firstMove = true;
        moveableSquares = new ArrayList<>();
    }

    // pre-condition: toSquare != null
    public boolean canMove(Square toSquare) {
        if (toSquare == null) {
            throw new IllegalArgumentException("Violation of pre-condition: toSquare != null");
        }

        return moveableSquares.contains(toSquare);
    }
    public abstract void update(Board board);

    // Jumping Checkers (no pun intended)

    // pre-condition: newRow >= 0 && newRow < 8, newCol >= 0 && newCol < 8 && grid != null
    protected boolean jumping(int newRow, int newCol, Square[][] grid) {
        if (newRow < 0 || newRow >= 8 || newCol < 0 || newCol >= 8 || grid == null) {
            throw new IllegalArgumentException("Violation of pre-condition: " +
                    "newRow >= 0 && newRow < 8, newCol >= 0 && newCol < 8 && grid != null");
        }

        return (jumpingVert(newRow, newCol, grid) ||
                jumpingHoriz(newRow, newCol, grid) ||
                jumpingDiag(newRow, newCol, grid));
    }
    protected boolean jumpingVert(int newRow, int newCol, Square[][] grid) {
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
    protected boolean jumpingHoriz(int newRow, int newCol, Square[][] grid) {
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
    protected boolean jumpingDiag(int newRow, int newCol, Square[][] grid) {
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

        /*
        for (int currRow = square.getRow() + incRowBy, currCol = square.getCol() + incColBy;
             (posRowDiff ? currRow > newRow : currRow < newRow) &&
                     (posColDiff ? currCol > newCol : currCol < newCol);
             currRow += incRowBy, currCol += incColBy) {
            if (grid[currRow][currCol].getPiece() != null) {
                return true;
            }
        }

         */

        return false;
    }

    public void scaleImage(int length) {
        image = defaultImage.getScaledInstance(length, length, 0);
    }

    public void setSquare(Square square) {
        this.square = square;
    }
    protected void setImage() {
        try {
            image = defaultImage = ImageIO.read(new File("res/pieces/" +
                    (isWhite ? "white" : "black") + "-" + imageName + ".png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setTopLeft(Point topLeft) {
        this.topLeft = topLeft;
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
    public boolean isWhite() {
        return isWhite;
    }
    public boolean isFirstMove() {
        return firstMove;
    }
    public ArrayList<Square> getMoveableSquares() {
        return moveableSquares;
    }
}
