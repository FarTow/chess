package panels;

import entities.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Board extends JPanel implements ActionListener, MouseListener, MouseMotionListener { // Weird lag
    // Board Properties
    private final Square[][] grid;
    private final Point topLeft;
    private final Point bottomRight;
    private int squareLength;
    private boolean initialCenter;

    private int startMinutes, startSeconds, timeIncrement;

    // Players
    private final Player whitePlayer;
    private final Player blackPlayer;
    private Player currentPlayer;

    // "Game" Properties
    private Piece selectedPiece;
    private int turnCount;
    private boolean whiteTurn;

    // External Trackers
    private Piece lastPieceMoved;
    private Point oldSquareCords, newSquareCords;
    private boolean ambiguousMove, ambiguousColumn;
    private int castlingStatus;
    private char pawnPromotionStatus;

    public Board(int startMinutes, int startSeconds, int timeIncrement) {
        setOpaque(false);

        // Properties
        this.startMinutes = startMinutes;
        this.startSeconds = startSeconds;
        this.timeIncrement = timeIncrement;

        grid = new Square[8][8];
        topLeft = new Point(0, 0);
        bottomRight = new Point(squareLength*8, squareLength*8);
        squareLength = 60;
        pawnPromotionStatus = ' ';
        turnCount = 0;
        whiteTurn = true;

        for (int row=0; row<grid.length; row++) { // initialize empty grid
            for (int column=0; column<grid[row].length; column++) {
                Point pos = new Point(topLeft.x + column * squareLength, topLeft.y + row * squareLength);

                grid[row][column] = new Square(row, column, pos, squareLength, null);
            }
        }

        whitePlayer = new Player(true, this);
        blackPlayer = new Player (false, this);

        resetBoard();
        updatePiecePositions();
        updatePieceImages();

        whitePlayer.setEnemyPlayer(blackPlayer);
        blackPlayer.setEnemyPlayer(whitePlayer);

        currentPlayer = whitePlayer;
        currentPlayer.update();

        addMouseListener(this);
        addMouseMotionListener(this);
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

        updatePositions();
        updatePieceImages();
        updateSquarePositions();
        updatePiecePositions();
    }

    protected void updatePositions() {
        topLeft.x = getWidth() / 2 - squareLength * 4;
        topLeft.y = getHeight() / 2 - squareLength * 4;
        bottomRight.x = topLeft.x + squareLength * 8;
        bottomRight.y = topLeft.y + squareLength * 8;
    }
    protected void updateSquarePositions() {
        for (int row = 0; row < grid.length; row++) {
            for (int column = 0; column < grid[row].length; column++) {
                Square square = grid[row][column];
                Point pos = new Point(topLeft.x + column * squareLength, topLeft.y + row * squareLength);

                square.setRect(new Rectangle(pos.x, pos.y, squareLength, squareLength));
            }
        }
    }
    protected void updatePiecePositions() {
        for (Square[] squareRow : grid) {
            for (Square square : squareRow) {
                if (square.getPiece() != null) square.getPiece().setTopLeft(square.getTopLeft());
            }
        }
    }
    protected void updatePieceImages() {
        whitePlayer.scalePieceImages(squareLength);
        blackPlayer.scalePieceImages(squareLength);
    }

    // Graphics
    protected void drawBoard(Graphics g) {
        for (Square[] squareRow : grid) {
            for (Square square : squareRow) {
                g.setColor(square.getRow()%2 == square.getColumn()%2 ?
                        new Color(240, 199, 134) : new Color(181, 136, 99));
                g.fillRect(square.getRect().x, square.getRect().y, square.getRect().width, square.getRect().height);
            }
        }
    }
    protected void drawIndicators(Graphics g) {
        Font indicatorsFont = Main.MULISH_LIGHT.deriveFont((float) getWidth()/60);
        g.setFont(indicatorsFont);
        g.setColor(Color.black);

        int offset = 5;

        for (int row=1; row<=grid.length; row++) { // draw files
            String rank = Integer.toString(row);
            Dimension stringSize = new Dimension(
                    g.getFontMetrics(indicatorsFont).stringWidth(rank),
                    g.getFontMetrics(indicatorsFont).getHeight());
            g.drawString(rank, topLeft.x-stringSize.width-offset, (bottomRight.y-squareLength/2+stringSize.height/4)-(squareLength * (row-1)));
        }

        for (int column=0; column<grid[0].length; column++) { // draw ranks
            String file = String.valueOf((char) ((char) 97+column));
            Dimension stringSize = new Dimension(
                    g.getFontMetrics(indicatorsFont).stringWidth(file),
                    g.getFontMetrics(indicatorsFont).getHeight());
            g.drawString(file, topLeft.x+(squareLength/2-stringSize.width/4)+(squareLength * column), bottomRight.y+stringSize.height/2+offset);
        }
    }
    protected void drawSelectedSquare(Graphics g) {
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
    protected void drawPieces(Graphics g) {
        for (Piece piece : whitePlayer.getPieces()) {
            g.drawImage(piece.getImage(), piece.getTopLeft().x, piece.getTopLeft().y, null);
        }
        for (Piece piece : blackPlayer.getPieces()) {
            g.drawImage(piece.getImage(), piece.getTopLeft().x, piece.getTopLeft().y, null);
        }
    }
    protected void drawAvailableSquares(Graphics g) {
        if (selectedPiece == null) return;

        for (Square[] squareRow : grid) {
            for (Square square : squareRow) {
                if (selectedPiece.canMove(square)) {
                    g.setColor(new Color(130, 151, 105));

                    if (square.getPiece() != null) { // if there's a capturable piece, fill the four corners
                        int divisor = 4;

                        for (int xDest=square.getTopLeft().x; xDest<square.getTopLeft().x+square.getRect().width/divisor; xDest++) { // topLeftCorner
                            for (int yDest=square.getTopLeft().y; yDest<square.getTopLeft().y+square.getRect().height/divisor; yDest++) {
                                g.drawLine(xDest, square.getTopLeft().y, square.getTopLeft().x, yDest);
                            }
                        }
                        for (int xDest=square.getBottomRight().x; xDest>square.getBottomRight().x-square.getRect().width/divisor; xDest--) { // topRightCorner
                            for (int yDest=square.getTopLeft().y; yDest<square.getTopLeft().y+square.getRect().height/divisor; yDest++) {
                                g.drawLine(xDest, square.getTopLeft().y, square.getBottomRight().x, yDest);
                            }
                        }
                        for (int xDest=square.getTopLeft().x; xDest<square.getTopLeft().x+square.getRect().width/divisor; xDest++) { // bottomLeftCorner
                            for (int yDest=square.getBottomRight().y; yDest>square.getBottomRight().y-square.getRect().height/divisor; yDest--) {
                                g.drawLine(xDest, square.getBottomRight().y, square.getTopLeft().x, yDest);
                            }
                        }
                        for (int xDest=square.getBottomRight().x; xDest>square.getBottomRight().x-square.getRect().width/divisor; xDest--) { // bottomRightCorner
                            for (int yDest=square.getBottomRight().y; yDest>square.getBottomRight().y-square.getRect().height/divisor; yDest--) {
                                g.drawLine(xDest, square.getBottomRight().y, square.getBottomRight().x, yDest);
                            }
                        }
                    } else { // if it's a free square
                        g.fillOval(square.getTopLeft().x + square.getRect().width / 2 - square.getRect().width / 10,
                                square.getTopLeft().y + square.getRect().height / 2 - square.getRect().width / 10,
                                square.getRect().width / 5, square.getRect().height / 5); // draw the square's piece if it exists
                    }
                }
            }
        }
    }

    // Logic Methods
    protected void updateAmbiguousMove(Square toSquare) {
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
    protected void updateMoveHistoryInteractors(Square toSquare) {
        if (selectedPiece == null) return;

        ambiguousMove = ambiguousColumn = false;
        lastPieceMoved = selectedPiece;
        oldSquareCords = new Point(selectedPiece.getRow(), selectedPiece.getColumn());
        newSquareCords = new Point(toSquare.getRow(), toSquare.getColumn());
        updateAmbiguousMove(toSquare);
    }

    protected int createPromotionPrompt(boolean isWhite) {
        Object[] promotionDialogImages = new Object[] {
                new ImageIcon(isWhite ? Main.whitePieceImages[Main.QUEEN_IMAGE_INDEX] : Main.blackPieceImages[Main.QUEEN_IMAGE_INDEX]),
                new ImageIcon(isWhite ? Main.whitePieceImages[Main.ROOK_IMAGE_INDEX] : Main.blackPieceImages[Main.ROOK_IMAGE_INDEX]),
                new ImageIcon(isWhite ? Main.whitePieceImages[Main.BISHOP_IMAGE_INDEX] : Main.blackPieceImages[Main.BISHOP_IMAGE_INDEX]),
                new ImageIcon(isWhite ? Main.whitePieceImages[Main.KNIGHT_IMAGE_INDEX] : Main.blackPieceImages[Main.KNIGHT_IMAGE_INDEX])
        };

        return JOptionPane.showOptionDialog(
                this, null, "Promote Piece",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                promotionDialogImages, JOptionPane.UNINITIALIZED_VALUE);
    }
    protected void promotePawn(Piece promotedPawn, Square newSquare, int newPiece) {
        int replacedPieceIndex = currentPlayer.getPieces().indexOf(promotedPawn);

        switch(newPiece) {
            case -1:
                promotePawn(promotedPawn, newSquare, createPromotionPrompt(promotedPawn.isWhite()));
                break;
            case 0:
                currentPlayer.getPieces().set(replacedPieceIndex, new Queen(currentPlayer.isWhite(), newSquare));
                break;
            case 1:
                currentPlayer.getPieces().set(replacedPieceIndex, new Rook(currentPlayer.isWhite(), newSquare));
                break;
            case 2:
                currentPlayer.getPieces().set(replacedPieceIndex, new Bishop(currentPlayer.isWhite(), newSquare));
                break;
            case 3:
                currentPlayer.getPieces().set(replacedPieceIndex, new Knight(currentPlayer.isWhite(), newSquare));
                break;
        }

        currentPlayer.getPieces().get(replacedPieceIndex).scaleImage(squareLength);
        currentPlayer.removePiece(promotedPawn);
        newSquare.setPiece(currentPlayer.getPieces().get(replacedPieceIndex));

        pawnPromotionStatus = currentPlayer.getPieces().get(replacedPieceIndex).getNotation();
        currentPlayer.updatePieceCount();
        currentPlayer.getEnemyPlayer().updatePieceCount();
    }
    protected void checkSpecialPieceMoved() {
        if (selectedPiece == null) return;

        if (selectedPiece instanceof King) { // if the piece was a king
            int columnDiff = oldSquareCords.y - newSquareCords.y;

            if (Math.abs(columnDiff) == 2) {
                currentPlayer.physicallyCastle(columnDiff < 0 ? 1 : 2); // if it castled, tell the player it castled
                castlingStatus = columnDiff < 0 ? 1 : 2; // needs work probably
            }
        } else if (selectedPiece instanceof Pawn) {
            if (Math.abs(oldSquareCords.x - newSquareCords.x) == 2) { // set piece to be en-passant capturable
                ((Pawn) selectedPiece).setEnPassantCapturable(true);
            } else if (selectedPiece.isWhite() ? newSquareCords.x == 0 : newSquareCords.x == 7) { // if the pawn is on the opposite side
                promotePawn(selectedPiece, selectedPiece.getSquare(), createPromotionPrompt(selectedPiece.isWhite()));
            }
        }
    }
    protected void swapPlayer() {
        if (currentPlayer.getFirstTurn()) {
            currentPlayer.setFirstTurn(false);
        } else {
            currentPlayer.shouldRunTimer(false);
            currentPlayer.setSecondsLeft(currentPlayer.getSecondsLeft()+timeIncrement);
        }

        currentPlayer = whiteTurn ? whitePlayer : blackPlayer;
        currentPlayer.update(); // PIECES UPDATED AT THE START OF THEIR TURN

        if (!currentPlayer.getFirstTurn()) {
            currentPlayer.shouldRunTimer(true);
        }
    }

    public void movePiece(Piece piece, Square toSquare, boolean permanent) {
        int oldRow = piece.getRow();
        int oldColumn = piece.getColumn();

        if (permanent) piece.setTopLeft(toSquare.getTopLeft()); // move the selected piece to the square
        piece.setSquare(toSquare);
        toSquare.setPiece(piece); // set the square's piece to the selected piece
        grid[oldRow][oldColumn].setPiece(null); // set the old square's piece to null
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // clear screen

        // Sharper text
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawBoard(g);
        drawIndicators(g);
        drawSelectedSquare(g);
        drawPieces(g);
        drawAvailableSquares(g);

        if (selectedPiece != null) g.drawImage(selectedPiece.getImage(), selectedPiece.getTopLeft().x, selectedPiece.getTopLeft().y, null);
    }
    public void actionPerformed(ActionEvent ae) {
        if (!initialCenter) { // please god tell me there's a way
            resize(squareLength);
            initialCenter = true;
        }

        whitePlayer.formatTime(); // ugh
        blackPlayer.formatTime();

        if (currentPlayer.isInTimeout()) System.out.println("player is in timeout");

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

        if (castlingStatus != 0) castlingStatus = 0; // not a fan of having to check this every time the mouse is released
        if (pawnPromotionStatus != ' ') pawnPromotionStatus = ' '; // yuck

        for (Square[] squareRow : grid) {
            for (Square square : squareRow) {
                if (pointContained(selectedPiece.getPos(), square.getTopLeft(), square.getBottomRight())) { // if the selected piece's position is in the square when released
                    if (selectedPiece.canMove(square)) { // if the piece can move to that location
                        updateMoveHistoryInteractors(square);

                        // Physical "Moving" of Pieces
                        (selectedPiece.isWhite() ? blackPlayer : whitePlayer).removePiece(square.getPiece());
                        movePiece(selectedPiece, square, true);
                        if (selectedPiece.isFirstMove()) selectedPiece.setFirstMove(false);

                        checkSpecialPieceMoved();

                        // Update Board / Players
                        turnCount++;
                        whiteTurn = turnCount%2==0;
                        swapPlayer();
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

    public int getStartMinutes() { return startMinutes; } // Time Getters
    public int getStartSeconds() { return startSeconds; }
    public int getTimeIncrement() { return timeIncrement; }
    public Square[][] getGrid() { return grid; } // "Grid" Getters
    public Point getOldSquareCords() { return oldSquareCords; }
    public Point getNewSquareCords() { return newSquareCords; }
    public Player getWhitePlayer() { return whitePlayer; } // Player Getters
    public Player getBlackPlayer() { return blackPlayer; }
    public Player getCurrentPlayer() { return currentPlayer; }
    public boolean getWhiteTurn() { return whiteTurn; } // Miscellaneous Getters
    public int getCastlingStatus() { return castlingStatus; }
    public char getPawnPromotionStatus() { return pawnPromotionStatus; }
    public Piece getLastPieceMoved() { return lastPieceMoved; }
    public boolean isMoveAmbiguous() { return ambiguousMove; }
    public boolean isColumnAmbiguous() { return ambiguousColumn; }

}
