package entities;

import java.awt.*;

public class Square {
    private final int row, column;
    private Rectangle rectangle;
    private Piece piece;

    public Square(int row, int column, Point topLeft, Dimension size, Piece piece) {
        this.row = row;
        this.column = column;
        rectangle = new Rectangle(topLeft.x, topLeft.y, size.width, size.height);
        this.piece = piece;
    }

    public void setPiece(Piece piece) { this.piece = piece; }
    public void setRect(Rectangle rectangle) { this.rectangle = rectangle; }

    public int getRow() { return row; }
    public int getColumn() { return column; }
    public Rectangle getRect() { return rectangle; }
    public Point getTopLeft() { return new Point(rectangle.x, rectangle.y); }
    public Point getBottomRight() { return new Point(rectangle.x+rectangle.width, rectangle.y+rectangle.height); }
    public Piece getPiece() { return piece; }
}
