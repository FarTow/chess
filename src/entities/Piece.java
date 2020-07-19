package entities;

import panels.Board;

import java.awt.*;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.ArrayList;

public abstract class Piece {
    protected Square square;
    protected Point topLeft;
    protected Image image, defaultImage;
    protected ArrayList<Square> moveableSquares;
    protected boolean isWhite, firstMove;

    public Piece(boolean isWhite, Square square) {
        this.isWhite = isWhite;
        this.square = square;
        topLeft = square.getTopLeft();
        firstMove = true;
        moveableSquares = new ArrayList<>();
    }

    public boolean canMove(Square toSquare) {
        return moveableSquares.contains(toSquare);
    }
    public abstract void update(Board board);

    // Jumping Checkers (no pun intended)
    protected boolean isJumping(int newRow, int newColumn, Square[][] grid) {
        return (isJumpingVertically(newRow, newColumn, grid) ||
                isJumpingHorizontally(newRow, newColumn, grid) ||
                isJumpingDiagonally(newRow, newColumn, grid));
    }
    protected boolean isJumpingVertically(int newRow, int newColumn, Square[][] grid) {
        if (square.getColumn() != newColumn) return false;

        for (int currentRow = square.getRow() + (square.getRow()<newRow ? 1 : -1);
             (square.getRow()<newRow ? currentRow<newRow : currentRow>newRow);
             currentRow += (square.getRow()<newRow ? 1 : -1)) {
            if (grid[currentRow][square.getColumn()].getPiece() != null) return true;
        }

        return false;
    }
    protected boolean isJumpingHorizontally(int newRow, int newColumn, Square[][] grid) {
        if (square.getRow() != newRow) return false;

        for (int currentColumn = square.getColumn() + (square.getColumn()<newColumn ? 1 : -1);
             (square.getColumn()<newColumn ? currentColumn<newColumn : currentColumn>newColumn);
             currentColumn += (square.getColumn()<newColumn ? 1 : -1)) {
            if (grid[square.getRow()][currentColumn].getPiece() != null) return true;
        }

        return false;
    }
    // Review isJumpingDiagonally
    protected boolean isJumpingDiagonally(int newRow, int newColumn, Square[][] grid) {
        int rowDiff = square.getRow()-newRow; // positive result = moving "up"
        int columnDiff = square.getColumn()-newColumn; // positive result = moving "left"

        if(Math.abs(rowDiff) != Math.abs(columnDiff)) return false;

        if (rowDiff > 0 && columnDiff > 0) {
            for (int currentRow=square.getRow()-1, currentColumn=square.getColumn()-1; currentRow > newRow && currentColumn > newColumn; currentRow--, currentColumn--) {
                if (grid[currentRow][currentColumn].getPiece() != null) return true;
            }
        } else if (rowDiff > 0 && columnDiff < 0) {
            for (int currentRow=square.getRow()-1, currentColumn=square.getColumn()+1; currentRow > newRow && currentColumn < newColumn; currentRow--, currentColumn++) {
                if (grid[currentRow][currentColumn].getPiece() != null) return true;
            }
        } else if (rowDiff < 0 && columnDiff > 0) {
            for (int currentRow=square.getRow()+1, currentColumn=square.getColumn()-1; currentRow < newRow && currentColumn > newColumn; currentRow++, currentColumn--) {
                if (grid[currentRow][currentColumn].getPiece() != null) return true;
            }
        } else { // if (rowDiff < 0 && columnDiff < 0)
            for (int currentRow=square.getRow()+1, currentColumn=square.getColumn()+1; currentRow < newRow && currentColumn < newColumn; currentRow++, currentColumn++) {
                if (grid[currentRow][currentColumn].getPiece() != null) return true;
            }
        }

        return false;
    }

    public void scaleImage(int length) { image = defaultImage.getScaledInstance(length, length, 0); }

    public void setSquare(Square square) { this.square = square; }
    protected void setImage(String name) {
        // getClass().getName().substring(getClass().getName().indexOf('.')+1)
        try {
            image = defaultImage = ImageIO.read(new File("res/pieces/" + (isWhite ? "white" : "black") + "-" + name + ".png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setTopLeft(Point topLeft) { this.topLeft = topLeft; }
    public void setPos(Point pos) { this.topLeft = new Point(pos.x-image.getWidth(null)/2, pos.y-image.getHeight(null)/2); }
    public void setFirstMove(boolean firstMove) { this.firstMove = firstMove; }
    public void setMoveableSquares(ArrayList<Square> moveableSquares) { this.moveableSquares = moveableSquares; }

    public abstract char getNotation();
    public Square getSquare() { return square; }
    public Image getImage() { return image; }
    public Point getTopLeft() { return topLeft; }
    public Point getPos() { return new Point(topLeft.x+image.getWidth(null)/2, topLeft.y+image.getHeight(null)/2); }
    public int getRow() { return square.getRow(); }
    public int getColumn() { return square.getColumn(); }
    public boolean isWhite() { return isWhite; }
    public boolean isFirstMove() { return firstMove; }
    public ArrayList<Square> getMoveableSquares() { return moveableSquares; }


}
