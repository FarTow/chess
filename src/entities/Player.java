package entities;

import panels.Board;

import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Player {
    private final Board board;

    // Personal Properties ("Human")
    private final boolean isWhite;
    private ArrayList<Piece> pieces;
    private final int[] pieceCount; // 0 = Pawn, 1 = Knight, 2 = Bishop, 3 = Rook, 4 = Queen
    private ArrayList<Square> allMoves;

    private boolean firstTurn; // Time
    private boolean runTimer;
    private int minutesLeft, secondsLeft, timeIncrement;

    // Interactive Properties (Win Conditions)
    private boolean inCheck, inCheckmate, inStalemate, timeOut;

    // Other
    private Player enemyPlayer;

    public Player(boolean isWhite, Board board) {
        this.isWhite = isWhite;
        this.board = board;

        firstTurn = true;
        pieceCount = new int[5];
        allMoves = new ArrayList<>();
        timeOut = false;

        if (board.getStartMinutes() <= 0 && board.getStartSeconds() <= 0) {
            minutesLeft = secondsLeft = timeIncrement = -1;
        } else {
            minutesLeft = board.getStartMinutes();
            secondsLeft = board.getStartSeconds();
            timeIncrement = board.getTimeIncrement();

            ActionListener timerCountDown = ae -> {
                if (!runTimer) return;

                if (minutesLeft == 0 && secondsLeft == 0) {
                    timeOut = true;
                    return;
                }
                if (secondsLeft == 0) {
                    minutesLeft--;
                    secondsLeft = 59;
                } else {
                    secondsLeft--;
                }
            };

            Timer timer = new Timer(1000, timerCountDown);
            timer.start();
        }

        defaultResetPieces();
        updatePieceCount();
    }

    // Piece Arrangement
    public void defaultResetPieces() {
        pieces = new ArrayList<>();
        Square[][] grid = board.getGrid();

        int startRow = isWhite ? 7 : 0;

        for (int i=0; i<=7; i++) {
            Square currentSquare = grid[startRow][i];

            switch(i) {
                case 0:
                case 7:
                    pieces.add(new Rook(isWhite, currentSquare));
                    break;
                case 1:
                case 6:
                    pieces.add(new Knight(isWhite, currentSquare));
                    break;
                case 2:
                case 5:
                    pieces.add(new Bishop(isWhite, currentSquare));
                    break;
                case 3:
                    pieces.add(new Queen(isWhite, currentSquare));
                    break;
                case 4:
                    pieces.add(new King(isWhite, currentSquare));
                    break;
            }
        }

        for (int i=0; i<=7; i++) {
            Square currentSquare = grid[startRow + (isWhite ? -1 : 1)][i];
            pieces.add(new Pawn(isWhite, currentSquare));
        }
    }
    public void randomResetPieces() {
        pieces = new ArrayList<>();
        Square[][] grid = board.getGrid();

        int startRow = isWhite ? 7 : 0;

        for (int i=0; i<=7; i++) {
            Square currentSquare = grid[startRow][i];

            if (i == 4) {
                pieces.add(new King(isWhite, currentSquare));
            } else {
                double randomNumber = Math.random();

                if (randomNumber <= .2) {
                    pieces.add(new Pawn(isWhite, currentSquare));
                } else if (randomNumber <= .4) {
                    pieces.add(new Rook(isWhite, currentSquare));
                } else if (randomNumber <= .6) {
                    pieces.add(new Knight(isWhite, currentSquare));
                } else if (randomNumber <= .8) {
                    pieces.add(new Bishop(isWhite, currentSquare));
                } else {
                    pieces.add(new Queen(isWhite, currentSquare));
                }
            }
        }

        for (int i=0; i<=7; i++) {
            Square currentSquare = grid[startRow + (isWhite ? -1 : 1)][i];
            pieces.add(new Pawn(isWhite, currentSquare));
        }
    }

    // Special Case Updates
    public void enPassantUpdate() { // credits to Tanvir for this logic
        for (Piece piece : pieces) { // player checks if OWN PAWN was captured through en passant
            if (piece instanceof Pawn) {
                if (piece.getRow() != (isWhite ? 4 : 3)) continue;
                int behindRowDirection = isWhite ? 1 : -1;

                if (piece.getRow() + behindRowDirection < 0 || piece.getRow() + behindRowDirection > 7) continue;

                Square possiblePawnSquare = board.getGrid()[piece.getRow() + behindRowDirection][piece.getColumn()]; // square behind pawn
                Piece possiblePawn = possiblePawnSquare.getPiece();

                if (possiblePawn instanceof Pawn) {
                    if (isWhite != possiblePawn.isWhite()) {
                        piece.getSquare().setPiece(null);
                        piece.setSquare(null);
                        pieces.remove(piece);
                        break;
                    }
                }
            }
        }

        if (isWhite == board.getWhiteTurn()) {
            for (Piece piece : pieces) {
                if (piece instanceof Pawn) {
                    if (((Pawn) piece).isEnPassantCapturable()) ((Pawn) piece).setEnPassantCapturable(false);
                }
            }
        }
    }
    public void physicallyCastle(int castleSide) {
        switch(castleSide) { // 0 -> not castled, 1 -> castled king's side, 2 -> castled queen's side
            case 1:
                board.movePiece(board.getGrid()[getKing().getRow()][7].getPiece(), board.getGrid()[getKing().getRow()][5], true);
                break;
            case 2:
                board.movePiece(board.getGrid()[getKing().getRow()][0].getPiece(), board.getGrid()[getKing().getRow()][3], true);
                break;
            default:
                break;
        }
    }

    // Personal Updates
    protected void updateAllMoves() {
        allMoves = new ArrayList<>();

        for (Piece piece : pieces) {
            allMoves.addAll(piece.getMoveableSquares());
        }
    }
    public void updatePieceCount() {
        int pawnCount, knightCount, bishopCount, rookCount, queenCount;
        pawnCount = knightCount = bishopCount = rookCount = queenCount = 0;

        for (Piece piece : pieces) {
            switch(piece.getNotation()) {
                case (char) 0:
                    pawnCount += 1;
                    break;
                case 'N':
                    knightCount += 1;
                    break;
                case 'B':
                    bishopCount += 1;
                    break;
                case 'R':
                    rookCount += 1;
                    break;
                case 'Q':
                    queenCount += 1;
                    break;
            }
        }

        for (int i=0; i<pieceCount.length; i++) {
            switch(i) {
                case 0:
                    pieceCount[i] = pawnCount;
                    break;
                case 1:
                    pieceCount[i] = knightCount;
                    break;
                case 2:
                    pieceCount[i] = bishopCount;
                    break;
                case 3:
                    pieceCount[i] = rookCount;
                    break;
                case 4:
                    pieceCount[i] = queenCount;
                    break;
            }
        }
    }
    public void update() {
        inCheck = false;

        // Update pawns
        enPassantUpdate();

        // Update all pieces to where they may move
        for (Piece piece : pieces) {
            piece.update(board);

            ArrayList<Square> mayMoveSquares = new ArrayList<>();
            for (Square square : piece.getMoveableSquares()) {
                if (mayMove(piece, square)) mayMoveSquares.add(square);
            }

            piece.setMoveableSquares(mayMoveSquares);
        }

        getKing().castleCheck(board);
        updateAllMoves();

        if (isKingInCheck()) {
            if (allMoves.size() == 0) { // if it's checkmate
                inCheckmate = true;
            } else { // if the king is in check
                inCheck = true;
            }
        } else {
            if (allMoves.size() == 0) { // if it's stalemate
                inStalemate = true;
            }
        }

        updatePieceCount();
    }

    // "Changers"
    protected boolean mayMove(Piece piece, Square toSquare) {
        Piece takenPiece = toSquare.getPiece();

        if (toSquare == piece.getSquare()) return false;

        if (takenPiece != null) { // check if they're the same color
            if (piece.isWhite() == toSquare.getPiece().isWhite()) return false;

            enemyPlayer.removePiece(takenPiece);
        }

        boolean mayMove;
        int oldRow = piece.getRow();
        int oldColumn = piece.getColumn();

        board.movePiece(piece, toSquare, false); // move piece to desired square
        mayMove = !isKingInCheck();
        board.movePiece(piece, board.getGrid()[oldRow][oldColumn], false); // move the piece back to original square
        toSquare.setPiece(takenPiece); // set the new square's piece back
        if (takenPiece != null) enemyPlayer.addPiece(takenPiece);

        return mayMove;
    }
    public void addPiece(Piece piece) { pieces.add(piece); }
    public void removePiece(Piece piece) {
        if (piece == null) return;

        pieces.remove(piece);
    }

    // Misc
    public void formatTime() {
        if (secondsLeft >= 60) {
            minutesLeft++;
            secondsLeft -= 60;
        }
    }
    public void scalePieceImages(int newSize) {
        for (Piece piece : pieces) {
            piece.scaleImage(newSize);
        }
    }

    public void setFirstTurn(boolean firstTurn) { this.firstTurn = firstTurn; }
    public void setEnemyPlayer(Player enemyPlayer) { this.enemyPlayer = enemyPlayer; }
    public void setSecondsLeft(int secondsLeft) { this.secondsLeft = secondsLeft; }
    public void shouldRunTimer(boolean runTimer) { this.runTimer = runTimer; }

    protected King getKing() {
        for (Piece piece : pieces) {
            if (piece instanceof King) return (King) piece;
        }

        return null;
    }
    public boolean getFirstTurn() { return firstTurn; }
    public ArrayList<Piece> getPieces() { return pieces; }
    public int[] getPieceCount() { return pieceCount; }
    public int getMinutesLeft() { return minutesLeft; }
    public int getSecondsLeft() { return secondsLeft; }
    public int getTimeIncrement() { return timeIncrement; }
    public Player getEnemyPlayer() { return enemyPlayer; }
    public boolean isKingInCheck() {
        int checkCount = 0;

        for (Piece piece : enemyPlayer.getPieces() ) {
            piece.update(board);

            if (piece.canMove(getKing().getSquare())) { checkCount++; }
        }

        return checkCount > 0;
    } // probably combine this method and the latter
    public boolean isInCheck() { return inCheck; }
    public boolean isInCheckmate() { return inCheckmate; }
    public boolean isInStalemate() { return inStalemate; }
    public boolean isInTimeout() { return timeOut; }
    public boolean isWhite() { return isWhite; }

    public String toString() {
        StringBuilder returnString = new StringBuilder();
        returnString.append(isWhite ? "White: " : "Black: ");

        for (Piece piece : pieces) {
            returnString.append(piece.getNotation());
            returnString.append(" ");
        }

        return returnString.toString();
    }
}
