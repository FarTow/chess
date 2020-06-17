package entities;

import panels.Game;

import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.*;
import java.awt.event.*;

public class Board extends JPanel implements ActionListener, MouseListener, MouseMotionListener {
    private final Square[][] grid;
    private final Point gridTopLeft, gridBottomRight;
    private final Dimension squareSize;

    private Piece selectedPiece;
    private int turnCount;
    private boolean whiteTurn;

    public Board(Point gridTopLeft, Dimension squareSize) {
        this.gridTopLeft = gridTopLeft;
        gridBottomRight = new Point(gridTopLeft.x + squareSize.width * 8, gridTopLeft.y + squareSize.height * 8);
        this.squareSize = squareSize;

        grid = new Square[8][8];
        turnCount = 0;
        whiteTurn = true;
        resetBoard();
    }

    // Graphics
    public void drawBoard(Graphics g) {
        for (Square[] squareRow : grid) {
            for (Square square : squareRow) {
                g.setColor(square.getRow()%2 == square.getColumn()%2 ?
                        new Color(240, 199, 134) : new Color(181, 136, 99));
                g.fillRect(square.getRect().x, square.getRect().y, square.getRect().width, square.getRect().height);
            }
        }

        Font indicatorsFont = new Font("Helvetica", Font.PLAIN, 18);

        g.setColor(Color.black);
        for (int row=0; row<grid.length; row++) {
            String rank = Integer.toString(row+1);
            Dimension stringSize = new Dimension(g.getFontMetrics(indicatorsFont).stringWidth(rank),
                    g.getFontMetrics(indicatorsFont).getHeight());
            g.drawString(rank, gridTopLeft.x-stringSize.width, (gridTopLeft.y+squareSize.height/2+stringSize.height/4)+(squareSize.height * row));
        }

        for (int column=0; column<grid[0].length; column++) {
            String file = String.valueOf((char) ((char) 97+column));
            Dimension stringSize = new Dimension(g.getFontMetrics(indicatorsFont).stringWidth(file),
                    g.getFontMetrics(indicatorsFont).getHeight());
            g.drawString(file, (gridTopLeft.x+squareSize.width/2+stringSize.width/4)+(squareSize.width * column), gridBottomRight.y+stringSize.height/2);
        }

    }
    public void drawPieces(Graphics g) {
        for (Square[] squareRow : grid) {
            for (Square square : squareRow) {
                if (square.getPiece() != null) {
                    g.drawImage(square.getPiece().getImage(), square.getPiece().getTopLeft().x, square.getPiece().getTopLeft().y, null); // draw the square's piece if it exists
                }
            }
        }
    }
    public void drawAvailableSquares(Graphics g) {
        if (selectedPiece == null) return;

        for (Square[] squareRow : grid) {
            for (Square square : squareRow) {
                if (selectedPiece.canMove(square.getRow(), square.getColumn(), this, false) && mayMove(selectedPiece, square)) {
                    g.setColor(new Color(130, 151, 105));
                    g.fillOval(square.getTopLeft().x + square.getRect().width / 2 - square.getRect().width / 10,
                            square.getTopLeft().y + square.getRect().height / 2 - square.getRect().width / 10,
                            square.getRect().width / 5, square.getRect().height / 5); // draw the square's piece if it exists
                }
            }
        }
    }
    public void drawSelectedSquare(Graphics g) {
        if (selectedPiece == null) return;

        for (Square[] squareRow : grid) {
            for (Square square : squareRow) {
                if (pointContained(selectedPiece.getPos(), square.getTopLeft(), square.getBottomRight())) {
                    if (square.getRow() == selectedPiece.getRow() && square.getColumn() == selectedPiece.getColumn()) {
                        g.setColor(new Color(82, 157, 204));
                        g.fillRect(square.getRect().x, square.getRect().y, square.getRect().width, square.getRect().height);
                        return;
                    }

                    g.setColor(((selectedPiece.canMove(square.getRow(), square.getColumn(), this, false) && mayMove(selectedPiece, square)) ? new Color(130, 151, 105) : new Color(204, 88, 82)));
                    g.fillRect(square.getRect().x, square.getRect().y, square.getRect().width, square.getRect().height);
                    return;
                }
            }
        }
    }
    public void resetBoard() {
        for (int row = 0; row < grid.length; row++) {
            for (int column = 0; column < grid[row].length; column++) {
                Point pos = new Point(gridTopLeft.x + (column * squareSize.width), gridTopLeft.y + (row * squareSize.height));

                switch (row) {
                    case 0:
                        switch (column) {
                            case 0:
                            case 7: // rooks
                                grid[row][column] = new Square(row, column, pos, squareSize, new Rook(false, row, column, pos));
                                break;
                            case 1:
                            case 6: // knights
                                grid[row][column] = new Square(row, column, pos, squareSize, new Knight(false, row, column, pos));
                                break;
                            case 2:
                            case 5: // bishop
                                grid[row][column] = new Square(row, column, pos, squareSize, new Bishop(false, row, column, pos));
                                break;
                            case 3: // queen
                                grid[row][column] = new Square(row, column, pos, squareSize, new Queen(false, row, column, pos));
                                break;
                            case 4: // queen
                                grid[row][column] = new Square(row, column, pos, squareSize, new King(false, row, column, pos));
                                break;
                        }
                        break;
                    case 1:
                        grid[row][column] = new Square(row, column, pos, squareSize, new Pawn(false, row, column, pos));
                        break;
                    case 6:
                        grid[row][column] = new Square(row, column, pos, squareSize, new Pawn(true, row, column, pos));
                        break;
                    case 7:
                        switch (column) {
                            case 0:
                            case 7: // rooks
                                grid[row][column] = new Square(row, column, pos, squareSize, new Rook(true, row, column, pos));
                                break;
                            case 1:
                            case 6: // knights
                                grid[row][column] = new Square(row, column, pos, squareSize, new Knight(true, row, column, pos));
                                break;
                            case 2:
                            case 5: // bishop
                                grid[row][column] = new Square(row, column, pos, squareSize, new Bishop(true, row, column, pos));
                                break;
                            case 3: // queen
                                grid[row][column] = new Square(row, column, pos, squareSize, new Queen(true, row, column, pos));
                                break;
                            case 4: // king
                                grid[row][column] = new Square(row, column, pos, squareSize, new King(true, row, column, pos));
                                break;
                        }
                        break;
                    default:
                        grid[row][column] = new Square(row, column, pos, squareSize, null);
                }

            }
        }
    }

    // "Update" Methods
    public void updatePawns() {
        for (Square[] squareRow : grid) {
            for (Square square : squareRow) {
                if (square.getPiece() instanceof Pawn) {
                    if(whiteTurn == square.getPiece().isWhite) {
                        ((Pawn) square.getPiece()).update();
                    }
                }
            }
        }
    }
    public void updateKings() {
        getKing(true).setCheck(isKingInCheck(getKing(true)));
        getKing(false).setCheck(isKingInCheck(getKing(false)));
    }

        // Logic Methods
    public void movePiece(Piece piece, Square toSquare, boolean permanent) {
        int oldRow = piece.row;
        int oldColumn = piece.column;

        if (permanent) piece.setTopLeft(toSquare.getTopLeft()); // move the selected piece to the square
        piece.setRow(toSquare.getRow());
        piece.setColumn(toSquare.getColumn());
        toSquare.setPiece(piece); // set the square's piece to the selected piece
        grid[oldRow][oldColumn].setPiece(null); // set the old square's piece to null
    }
    public boolean mayMove(Piece piece, Square toSquare) {
        int oldRow = piece.getRow();
        int oldColumn = piece.getColumn();

        boolean mayMove;
        Piece takenPiece = toSquare.getPiece();

        movePiece(piece, toSquare, false); // move piece to desired square
        mayMove = !isKingInCheck(getKing(whiteTurn));

        movePiece(piece, grid[oldRow][oldColumn], false); // move the piece back to original square
        toSquare.setPiece(takenPiece); // set the new square's piece back

        return mayMove;
    }

    public boolean isKingInCheck(King king) {
        int checkCount = 0;
        boolean isKingWhite = king.isWhite();

        for (Square[] squareRow : grid) {
            for (Square square : squareRow) {
                if (square.getPiece() != null) {
                    if (!(square.getPiece() instanceof King)) {
                        if (square.getPiece().isWhite() != isKingWhite && square.getPiece().canMove(king.getRow(), king.getColumn(), this, false)) {
                            checkCount++;
                        }
                    }
                }
            }
        }

        return checkCount > 0;
    }
    public int availableMoves(boolean isWhite) {
        int availableMoves = 0;

        for (Square[] squareRow : grid) {
            for (Square square : squareRow) { // for all squares on the board
                if (square.getPiece() != null && square.getPiece().isWhite() == isWhite) { // if there's a piece there and it's the right color

                    for (Square[] squareRowTwo : grid) {
                        for (Square squareTwo : squareRowTwo) { // for all squares on the board
                            if (square.getPiece().canMove(squareTwo.getRow(), squareTwo.getColumn(), this, false) && mayMove(square.getPiece(), squareTwo)) { // if the piece can move to the new square
                                    availableMoves++;
                            }
                        }

                    }
                }
            }
        }

        return availableMoves;
    }


    public void actionPerformed(ActionEvent ae) {
        updatePawns();
        updateKings();

        if (availableMoves(whiteTurn)==0) {
            if (getKing(whiteTurn).getCheck()) {
                System.out.println((whiteTurn ? "white" : "black") + " is in checkmate");
            } else {
                System.out.println((whiteTurn ? "white" : "black") + " is in stalemate");
            }
        }

        repaint();
    }
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // clear screen

        drawBoard(g);
        drawSelectedSquare(g);
        drawPieces(g);
        drawAvailableSquares(g);

        if (selectedPiece != null) g.drawImage(selectedPiece.getImage(), selectedPiece.getTopLeft().x, selectedPiece.getTopLeft().y, null);
    }
    public void start() {
        Timer t = new Timer(1000/Game.FRAME_RATE, this);
        t.start();
    }

    // Mouse Interaction Methods
    public boolean pointContained(Point point, Point topLeft, Point bottomRight) {
        return point.x >= topLeft.x && point.x <= bottomRight.x && point.y >= topLeft.y && point.y <= bottomRight.y;
    }
    public boolean mouseContained(MouseEvent me, Point topLeft, Point bottomRight) {
        return me.getX() >= topLeft.x && me.getX() <= bottomRight.x && me.getY() >= topLeft.y && me.getY() <= bottomRight.y;
    }

        // MouseListener Methods
    public void mouseClicked(MouseEvent me) {}
    public void mousePressed(MouseEvent me) {
        if (selectedPiece == null) { // if a piece isn't selected already
            for (Square[] squareRow : grid) {
                for (Square square : squareRow) {
                    if (square.getPiece() != null) {

                        if (mouseContained(me, square.getTopLeft(), square.getBottomRight()) && (square.getPiece().isWhite == whiteTurn)) { // if the mouse is in a square and it's white's turn
                            selectedPiece = square.getPiece(); // set the selected piece to the piece in the square
                        }
                    }
                }
            }
        }
    }
    public void mouseReleased(MouseEvent me) {
        if (selectedPiece == null) return; // if a piece isn't selected, return

        for (Square[] squareRow : grid) {
            for (Square square : squareRow) {
                if (pointContained(selectedPiece.getPos(), square.getTopLeft(), square.getBottomRight())) { // if the selected piece's position is in the square when released
                    if (selectedPiece.canMove(square.getRow(), square.getColumn(), this, true) && mayMove(selectedPiece, square)) { // if the piece can move to that location
                        movePiece(selectedPiece, square, true);
                        if (selectedPiece.isFirstMove()) selectedPiece.setFirstMove(false);
                        turnCount++;
                        whiteTurn = (turnCount%2==0);
                    } else {
                        selectedPiece.setTopLeft(grid[selectedPiece.getRow()][selectedPiece.getColumn()].getTopLeft()); // move the selected piece back if it can't move there
                    }
                    selectedPiece = null; // no selected piece now
                    return; // exit loop as the selected piece is now null
                }
            }
        }
    }
    public void mouseEntered(MouseEvent me) {}
    public void mouseExited(MouseEvent me) {}

        // MouseMotionListener Methods
    public void mouseDragged(MouseEvent me) {
        if (selectedPiece == null) return; // if a piece isn't selected return

        if (mouseContained(me, gridTopLeft, gridBottomRight)) selectedPiece.setPos(new Point(me.getX(), me.getY())); // move the selected piece to the mouse's location
    }
    public void mouseMoved(MouseEvent me) {}

    public Square[][] getGrid() { return grid; }
    public King getKing(boolean isWhite) {
        for (Square[] squareRow : grid) {
            for (Square square : squareRow) {
                if (square.getPiece() != null) {
                    if (square.getPiece() instanceof King && square.getPiece().isWhite == isWhite) return ((King) square.getPiece());
                }
            }
        }

        return null;
    }
}
