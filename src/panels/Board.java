package panels;

import entities.*;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;

public class Board extends JPanel implements ActionListener, MouseListener, MouseMotionListener {
    // Board Properties
    private final Point topLeft;
    private final Square[][] grid;
    private final Point bottomRight;
    private int squareLength;

    // Players
    private final Player whitePlayer;
    private final Player blackPlayer;
    private Player currentPlayer;

    // "Game" Properties
    private Piece selectedPiece;
    private int turnCount;
    private boolean whiteTurn;

    // Win Conditions
    private boolean check, checkmate, stalemate; // objective game ends
    private boolean threeFoldRepetition, fiftyMoveRule; // claimable draws

    // MoveHistory Trackers
    private Piece lastPiece;
    private Point oldSquare, newSquare;
    private boolean ambiguousMove, ambiguousColumn;
    private int castleState;

    public Board(Point initialTopLeft) {
        setBackground(new Color(194, 194, 194));

        topLeft = initialTopLeft;
        squareLength = 60;
        bottomRight = new Point(10+(squareLength*8), topLeft.y+(squareLength*8));

        grid = new Square[8][8];

        for (int row=0; row<grid.length; row++) {
            for (int column=0; column<grid[row].length; column++) {
                Point pos = new Point(topLeft.x+(column*squareLength), topLeft.y+(row*squareLength));

                grid[row][column] = new Square(row, column, pos, squareLength, null);
            }
        }

        whitePlayer = new Player(true, this);
        blackPlayer = new Player (false, this);

        whitePlayer.setEnemyPlayer(blackPlayer);
        blackPlayer.setEnemyPlayer(whitePlayer);

        currentPlayer = whitePlayer;

        turnCount = 0;
        whiteTurn = true;
        resetBoard();

        addMouseListener(this);
        addMouseMotionListener(this);

        currentPlayer.updatePieces();
    }

    public void resetBoard() {
        whitePlayer.defaultResetPieces();
        blackPlayer.defaultResetPieces();

        for (Square[] squareRow : grid) {
            for (Square square : squareRow) {
                for (Piece piece : whitePlayer.getPieces()) {
                    if (piece.getRow() == square.getRow() && piece.getColumn() == square.getColumn()) square.setPiece(piece);
                }

                for (Piece piece : blackPlayer.getPieces()) {
                    if (piece.getRow() == square.getRow() && piece.getColumn() == square.getColumn()) square.setPiece(piece);
                }
            }
        }
    }
    public void resize(int newSquareSize) {
        this.squareLength = newSquareSize;
        topLeft.y = getHeight()/2 - squareLength*4;
        bottomRight.x = topLeft.x+squareLength*8;
        bottomRight.y = topLeft.y+squareLength*8;

        whitePlayer.scalePieceImages(newSquareSize);
        blackPlayer.scalePieceImages(newSquareSize);

        for (int row = 0; row < grid.length; row++) {
            for (int column = 0; column < grid[row].length; column++) {
                Square square = grid[row][column];
                Point pos = new Point(topLeft.x + column*squareLength, topLeft.y + row*squareLength);

                square.setRect(new Rectangle(pos.x, pos.y, squareLength, squareLength));

                if (square.getPiece() != null) square.getPiece().setTopLeft(pos);
            }
        }
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
    }
    public void drawIndicators(Graphics g) {
        Font indicatorsFont = new Font("Helvetica", Font.PLAIN, 18);
        g.setColor(Color.black);

        for (int row=1; row<=grid.length; row++) {
            String rank = Integer.toString(row);
            Dimension stringSize = new Dimension(g.getFontMetrics(indicatorsFont).stringWidth(rank),
                    g.getFontMetrics(indicatorsFont).getHeight());
            g.drawString(rank, topLeft.x-stringSize.width, (bottomRight.y-squareLength/2+stringSize.height/4)-(squareLength * (row-1)));
        }

        for (int column=0; column<grid[0].length; column++) {
            String file = String.valueOf((char) ((char) 97+column));
            Dimension stringSize = new Dimension(g.getFontMetrics(indicatorsFont).stringWidth(file),
                    g.getFontMetrics(indicatorsFont).getHeight());
            g.drawString(file, topLeft.x/2+(squareLength/2+stringSize.width/4)+(squareLength * column), bottomRight.y+stringSize.height/2);
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

                    g.setColor(((selectedPiece.canMove(square)) ? new Color(130, 151, 105) : new Color(204, 88, 82)));
                    g.fillRect(square.getRect().x, square.getRect().y, square.getRect().width, square.getRect().height);
                    return;
                }
            }
        }
    }
    public void drawPieces(Graphics g) {
        for (Piece piece : whitePlayer.getPieces()) {
            g.drawImage(piece.getImage(), piece.getTopLeft().x, piece.getTopLeft().y, null);
        }
        for (Piece piece : blackPlayer.getPieces()) {
            g.drawImage(piece.getImage(), piece.getTopLeft().x, piece.getTopLeft().y, null);
        }
    }
    public void drawAvailableSquares(Graphics g) {
        if (selectedPiece == null) return;

        for (Square[] squareRow : grid) {
            for (Square square : squareRow) {
                if (selectedPiece.canMove(square)) {
                    g.setColor(new Color(130, 151, 105));
                    g.fillOval(square.getTopLeft().x + square.getRect().width / 2 - square.getRect().width / 10,
                            square.getTopLeft().y + square.getRect().height / 2 - square.getRect().width / 10,
                            square.getRect().width / 5, square.getRect().height / 5); // draw the square's piece if it exists
                }
            }
        }
    }

    // Logic Methods
    public void movePiece(Piece piece, Square toSquare, boolean permanent) {
        int oldRow = piece.getRow();
        int oldColumn = piece.getColumn();

        if (permanent) piece.setTopLeft(toSquare.getTopLeft()); // move the selected piece to the square
        piece.setSquare(toSquare);
        toSquare.setPiece(piece); // set the square's piece to the selected piece
        grid[oldRow][oldColumn].setPiece(null); // set the old square's piece to null
    }
    public int availableMoves() {
        int availableMoves = 0;

        for (Piece piece : currentPlayer.getPieces()) {
            for (Square[] squareRow : grid) {
                for (Square square : squareRow) {
                    if (piece.canMove(square)) {
                        availableMoves++;
                    }
                }
            }
        }

        return availableMoves;
    }

    public void updateWinCondition() {
        if (check) {
            check = false;
            return;
        }
        if (checkmate || stalemate) return;

        if (getCurrentPlayer().isKingInCheck()) {
            if (availableMoves() == 0) { // if it's checkmate
                checkmate = true;
            } else { // if the king is in check
                check = true;
            }
        } else {
            if (availableMoves() == 0) { // if it's stalemate
                stalemate = true;
            }
        }
    }
    public void updateAmbiguousMove(Square toSquare) {
        if (selectedPiece == null) return;

        if (!(selectedPiece instanceof Pawn)) {
            for (Piece piece : currentPlayer.getPieces()) {
                if (selectedPiece != piece && piece.getClass().equals(selectedPiece.getClass())) {
                    if (piece.canMove(toSquare)) {
                        ambiguousMove = true;

                        if (selectedPiece.getColumn() == piece.getColumn()) ambiguousColumn = true;
                    }
                }
            }
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // clear screen

        drawBoard(g);
        drawIndicators(g);
        drawSelectedSquare(g);
        drawPieces(g);
        drawAvailableSquares(g);

        if (selectedPiece != null) g.drawImage(selectedPiece.getImage(), selectedPiece.getTopLeft().x, selectedPiece.getTopLeft().y, null);
    }
    public void actionPerformed(ActionEvent ae) {
        repaint();
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
        if (selectedPiece != null) return; // if a piece isn't selected already

        if (checkmate || stalemate) return; // determine a better way to end the gamer soon

        for (Piece piece : currentPlayer.getPieces()) {
            if (piece.getSquare() != null) {
                if (mouseContained(me, piece.getTopLeft(), piece.getSquare().getBottomRight())) {
                    selectedPiece = piece;
                }
            }
        }

    }
    public void mouseReleased(MouseEvent me) {
        if (selectedPiece == null) return; // if a piece isn't selected, return

        if (castleState != 0) castleState = 0; // not a fan of having to check this every time the mouse is released

        for (Square[] squareRow : grid) {
            for (Square square : squareRow) {
                if (pointContained(selectedPiece.getPos(), square.getTopLeft(), square.getBottomRight())) { // if the selected piece's position is in the square when released
                    if (selectedPiece.canMove(square)) { // if the piece can move to that location
                        // MoveHistory Interaction
                        ambiguousMove = ambiguousColumn = false;
                        lastPiece = selectedPiece;
                        Piece takenPiece = square.getPiece();
                        oldSquare = new Point(selectedPiece.getRow(), selectedPiece.getColumn());
                        newSquare = new Point(square.getRow(), square.getColumn());
                        updateAmbiguousMove(square);

                        // Physical "Moving" of Pieces
                        (selectedPiece.isWhite() ? blackPlayer : whitePlayer).removePiece(takenPiece, true);
                        movePiece(selectedPiece, square, true);
                        if (selectedPiece.isFirstMove()) selectedPiece.setFirstMove(false);

                        // Special Pieces (pieces that require special checks)
                        if (selectedPiece instanceof King) { // if the piece was a king
                            int columnDiff = oldSquare.y - newSquare.y;

                            if (Math.abs(columnDiff) == 2) {
                                currentPlayer.physicallyCastle(columnDiff < 0 ? 1 : 2); // if it castled, tell the player it castled
                                castleState = columnDiff < 0 ? 1 : 2; // needs work probably
                            }
                        } else if (selectedPiece instanceof Pawn) {
                            if (Math.abs(oldSquare.x - newSquare.x) == 2) { // set piece to be en-passant capturable
                                ((Pawn) selectedPiece).setEnPassantCapturable(true);
                            } else if (selectedPiece.isWhite() ? newSquare.x == 0 : newSquare.x == 7) { // if the pawn is on the opposite side
                                ((Pawn) selectedPiece).setPromotable(true);

                                System.out.println("This pawn is promotable");
                            }
                        }

                        // Update Board / Players
                        turnCount++;
                        whiteTurn = turnCount%2==0;

                        currentPlayer = whiteTurn ? whitePlayer : blackPlayer;
                        currentPlayer.updatePieces(); // PIECES UPDATED AT THE START OF THEIR TURN

                        updateWinCondition();
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

        if (mouseContained(me, topLeft, bottomRight)) selectedPiece.setPos(new Point(me.getX(), me.getY())); // move the selected piece to the mouse's location
    }
    public void mouseMoved(MouseEvent me) {}

    public Square[][] getGrid() { return grid; } // "Grid" Getters
    public Point getOldSquare() { return oldSquare; }
    public Point getNewSquare() { return newSquare; }
    public Player getWhitePlayer() { return whitePlayer; } // Player Getters
    public Player getBlackPlayer() { return blackPlayer; }
    public Player getCurrentPlayer() { return currentPlayer; }
    public boolean isCheck() { return check; } // Win Condition Getters
    public boolean isCheckmate() { return checkmate; }
    public boolean isStalemate() { return stalemate; }
    public boolean getWhiteTurn() { return whiteTurn; } // Miscellaneous Getters
    public int getCastleState() { return castleState; }
    public Piece getLastPiece() { return lastPiece; }
    public boolean isMoveAmbiguous() { return ambiguousMove; }
    public boolean isColumnAmbiguous() { return ambiguousColumn; }

}
