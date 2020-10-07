package entities;

import panels.Board;

import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

public class Player {
    public enum PlayerState {
        NORMAL,
        CHECK,
        CHECKMATE,
        STALEMATE,
        TIMEOUT
    }

    public static final int MINUTES_INDEX = 0;
    public static final int SECONDS_INDEX = 1;
    public static final int INCREMENT_INDEX = 2;

    private final int PAWN_INDEX = 0;
    private final int KNIGHT_INDEX = 1;
    private final int BISHOP_INDEX = 2;
    private final int ROOK_INDEX = 3;
    private final int QUEEN_INDEX = 4;

    private final Board board;

    // Personal Properties ("Human")
    private final boolean isWhite;
    private ArrayList<Piece> pieces;
    private final int[] pieceCount;
    private ArrayList<Square> allMoves;

    private boolean firstTurn;
    private boolean runTimer;
    private int[] timeProperties;

    // Interactive Properties (Win Conditions)
    private PlayerState playerState;

    // Other
    private Player enemyPlayer;

    public Player(boolean isWhite, Board board) {
        this.isWhite = isWhite;
        this.board = board;

        timeProperties = board.getTimeProperties().clone();
        firstTurn = true;
        pieceCount = new int[5];
        allMoves = new ArrayList<>();
        runTimer = false;
        playerState = PlayerState.NORMAL;

        if (board.isTimedGame()) {
            ActionListener timerCountDown = ae -> {
                if (runTimer) {
                    if (timeProperties[MINUTES_INDEX] == 0 && timeProperties[SECONDS_INDEX] == 0) {
                        playerState = PlayerState.TIMEOUT;
                        return;
                    }
                    if (timeProperties[SECONDS_INDEX] == 0) {
                        timeProperties[MINUTES_INDEX]--;
                        timeProperties[SECONDS_INDEX] = 59;
                    } else {
                        timeProperties[SECONDS_INDEX]--;
                    }
                }
            };

            Timer timer = new Timer(1000, timerCountDown);
            timer.start();
        }

        defaultReset();
        updatePieceCount();
    }

    // Piece Arrangement
    public void defaultReset() {
        playerState = PlayerState.NORMAL;
        timeProperties = board.getTimeProperties().clone();
        firstTurn = true;
        runTimer = false;

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

        updatePieceCount();
    }

    // Special Case Updates
    public void enPassantUpdate() { // credits to Tanvir for this logic
        for (Piece piece : pieces) { // player checks if OWN PAWN was captured through en passant
            if (piece instanceof Pawn) {
                if (piece.getRow() != (isWhite ? 4 : 3)) continue;
                int behindRowDirection = isWhite ? 1 : -1;

                if (piece.getRow() + behindRowDirection < 0 || piece.getRow() + behindRowDirection > 7) continue;

                Square possiblePawnSquare =
                        board.getGrid()[piece.getRow() + behindRowDirection][piece.getCol()]; // square
                // behind pawn
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
        Arrays.fill(pieceCount, 0);

        for (Piece piece : pieces) {
            switch(piece.getNotation()) {
                case (char) 0:
                    pieceCount[PAWN_INDEX]++;
                    break;
                case 'N':
                    pieceCount[KNIGHT_INDEX]++;
                    break;
                case 'B':
                    pieceCount[BISHOP_INDEX]++;
                    break;
                case 'R':
                    pieceCount[ROOK_INDEX]++;
                    break;
                case 'Q':
                    pieceCount[QUEEN_INDEX]++;
                    break;
            }
        }

    }
    public void update() {
        playerState = PlayerState.NORMAL;

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
        updatePieceCount();

        if (isKingInCheck()) {
            if (allMoves.size() == 0) { // if it's checkmate
                playerState = PlayerState.CHECKMATE;
            } else { // if the king is in check
                playerState = PlayerState.CHECK;
            }
        } else {
            if (allMoves.size() == 0) { // if it's stalemate
                playerState = PlayerState.STALEMATE;
            }
        }
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
        int oldCol = piece.getCol();

        board.movePiece(piece, toSquare, false); // move piece to desired square
        mayMove = !isKingInCheck();
        board.movePiece(piece, board.getGrid()[oldRow][oldCol], false); // move the piece back to original
        // square
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
        if (timeProperties[SECONDS_INDEX] >= 60) {
            timeProperties[MINUTES_INDEX]++;
            timeProperties[SECONDS_INDEX] -= 60;
        }
    }
    public void scalePieceImages(int newSize) {
        for (Piece piece : pieces) {
            piece.scaleImage(newSize);
        }
    }

    public void setTimeProperty(int index, int value) { timeProperties[index] = value; }
    public void setFirstTurn(boolean firstTurn) { this.firstTurn = firstTurn; }
    public void setEnemyPlayer(Player enemyPlayer) { this.enemyPlayer = enemyPlayer; }
    public void shouldRunTimer(boolean runTimer) { this.runTimer = runTimer; }

    public int getTimeProperty(int index) { return timeProperties[index]; }
    protected King getKing() {
        for (Piece piece : pieces) {
            if (piece instanceof King) return (King) piece;
        }

        return null;
    }
    public boolean getFirstTurn() { return firstTurn; }
    public ArrayList<Piece> getPieces() { return pieces; }
    public int[] getPieceCount() { return pieceCount; }
    public Player getEnemyPlayer() { return enemyPlayer; }
    public boolean isKingInCheck() {
        int checkCount = 0;

        for (Piece piece : enemyPlayer.getPieces() ) {
            piece.update(board);

            if (piece.mayMove(getKing().getSquare())) { checkCount++; }
        }

        return checkCount > 0;
    } // probably combine this method and the latter
    public boolean isWhite() { return isWhite; }
    public PlayerState getState() { return playerState; }

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
