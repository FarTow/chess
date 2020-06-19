package entities;

import java.awt.*;

public class Square {
    private final int row, column;
    private Rectangle rect;
    private Piece piece;

    public Square(int row, int column, Point topLeft, int length, Piece piece) {
        this.row = row;
        this.column = column;
        rect = new Rectangle(topLeft.x, topLeft.y, length, length);
        this.piece = piece;
    }

    public void setPiece(Piece piece) { this.piece = piece; }
    public void setRect(Rectangle rect) { this.rect = rect; }

    public int getRow() { return row; }
    public int getColumn() { return column; }
    public Rectangle getRect() { return rect; }
    public Point getTopLeft() { return new Point(rect.x, rect.y); }
    public Point getBottomRight() { return new Point(rect.x+rect.width, rect.y+rect.height); }
    public Piece getPiece() { return piece; }
}
