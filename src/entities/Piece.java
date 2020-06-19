package entities;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import java.io.File;

public abstract class Piece {
    protected boolean isWhite, firstMove;
    protected int row, column;
    protected Point topLeft;
    protected Image image, defaultImage;

    public Piece(boolean isWhite, int row, int column, Point topLeft) {
        this.isWhite = isWhite;
        this.row = row;
        this.column = column;
        this.topLeft = topLeft;
        firstMove = true;
    }

    public abstract boolean canMove(int newRow, int newColumn, Board board, boolean mouseReleased);
    protected boolean sameColor(int newRow, int newColumn, Square[][] grid) {
        if (grid[newRow][newColumn].getPiece() == null) return false;
        return (isWhite == grid[newRow][newColumn].getPiece().isWhite());
    }

    // Jumping Checkers (no pun intended)
    protected boolean isJumping(int newRow, int newColumn, Square[][] grid) {
        return (isJumpingVertically(newRow, newColumn, grid) ||
                isJumpingHorizontally(newRow, newColumn, grid) ||
                isJumpingDiagonally(newRow, newColumn, grid));
    }
    protected boolean isJumpingVertically(int newRow, int newColumn, Square[][] grid) {
        if (column != newColumn) return false;

        for (int currentRow = row + (row<newRow ? 1 : -1);
             (row<newRow ? currentRow<newRow : currentRow>newRow);
             currentRow += (row<newRow ? 1 : -1)) {
            if (grid[currentRow][column].getPiece() != null) return true;
        }

        return false;
    }
    protected boolean isJumpingHorizontally(int newRow, int newColumn, Square[][] grid) {
        if (row != newRow) return false;

        for (int currentColumn = column + (column<newColumn ? 1 : -1);
             (column<newColumn ? currentColumn<newColumn : currentColumn>newColumn);
             currentColumn += (column<newColumn ? 1 : -1)) {
            if (grid[row][currentColumn].getPiece() != null) return true;
        }

        return false;
    }
    // Review isJumpingDiagonally
    protected boolean isJumpingDiagonally(int newRow, int newColumn, Square[][] grid) {
        int rowDiff = row-newRow; // positive result = moving "up"
        int columnDiff = column-newColumn; // positive result = moving "left"

        if(Math.abs(rowDiff) != Math.abs(columnDiff)) return false;

        if (rowDiff > 0 && columnDiff > 0) {
            for (int currentRow=row-1, currentColumn=column-1; currentRow > newRow && currentColumn > newColumn; currentRow--, currentColumn--) {
                if (grid[currentRow][currentColumn].getPiece() != null) return true;
            }
        } else if (rowDiff > 0 && columnDiff < 0) {
            for (int currentRow=row-1, currentColumn=column+1; currentRow > newRow && currentColumn < newColumn; currentRow--, currentColumn++) {
                if (grid[currentRow][currentColumn].getPiece() != null) return true;
            }
        } else if (rowDiff < 0 && columnDiff > 0) {
            for (int currentRow=row+1, currentColumn=column-1; currentRow < newRow && currentColumn > newColumn; currentRow++, currentColumn--) {
                if (grid[currentRow][currentColumn].getPiece() != null) return true;
            }
        } else { // if (rowDiff < 0 && columnDiff < 0)
            for (int currentRow=row+1, currentColumn=column+1; currentRow < newRow && currentColumn < newColumn; currentRow++, currentColumn++) {
                if (grid[currentRow][currentColumn].getPiece() != null) return true;
            }
        }

        return false;
    }

    public void scaleImage(int length) { image = defaultImage.getScaledInstance(length, length, 0); }

    protected void setImage(String name) {
        // getClass().getName().substring(getClass().getName().indexOf('.')+1)
        try {
            image = defaultImage = ImageIO.read(new File("res/" + (isWhite ? "white" : "black") + "-" + name + ".png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setTopLeft(Point topLeft) { this.topLeft = topLeft; }
    public void setPos(Point pos) { this.topLeft = new Point(pos.x-image.getWidth(null)/2, pos.y-image.getHeight(null)/2); }
    public void setRow(int row) {this.row = row; }
    public void setColumn(int column) {this.column = column; }
    public void setFirstMove(boolean firstMove) { this.firstMove = firstMove; }

    public Image getImage() { return image; }
    public Point getTopLeft() { return topLeft; }
    public Point getPos() { return new Point(topLeft.x+image.getWidth(null)/2, topLeft.y+image.getHeight(null)/2); }
    public int getRow() { return row; }
    public int getColumn() { return column; }
    public boolean isWhite() { return isWhite; }
    public boolean isFirstMove() { return firstMove; }

}
