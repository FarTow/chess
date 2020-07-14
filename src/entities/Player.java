package entities;

import panels.Board;

import java.util.ArrayList;

public class Player {
    private final Board board;
    private final boolean isWhite;
    private Player enemyPlayer;
    private ArrayList<Piece> pieces;
    private ArrayList<Piece> deadPieces;

    public Player(boolean isWhite, Board board) {
        this.isWhite = isWhite;
        this.board = board;
        defaultResetPieces();
        deadPieces = new ArrayList<>();
    }

    public void defaultResetPieces() {
        pieces = new ArrayList<>();
        deadPieces = new ArrayList<>();
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
        deadPieces = new ArrayList<>();
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

    public void enPassantUpdate() { // credits to Tanvir for this logic
        for (Piece piece : pieces) {
            if (piece instanceof Pawn) {
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
    public void phsyicallyCastle(int castleSide) {
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

    public void updatePieces() {
        System.out.println(((Pawn) pieces.get(14)).isPromotable());

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
    }

    public boolean mayMove(Piece piece, Square toSquare) {
        Piece takenPiece = toSquare.getPiece();

        if (toSquare == piece.getSquare()) return false;

        if (takenPiece != null) { // check if they're the same color
            if (piece.isWhite() == toSquare.getPiece().isWhite()) return false;

            enemyPlayer.removePiece(takenPiece, false);
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
    public boolean isKingInCheck() {
        int checkCount = 0;

        for (Piece piece : enemyPlayer.getPieces() ) {
            piece.update(board);

            if (piece.canMove(getKing().getSquare())) { checkCount++; }
        }

        return checkCount > 0;
    }

    public void scalePieceImages(int newSize) {
        for (Piece piece : pieces) {
            piece.scaleImage(newSize);
        }

        for (Piece deadPiece : deadPieces) {
            deadPiece.scaleImage(newSize);
        }
    }

    public void removePiece(Piece piece, boolean permanent) {
        if (piece == null) return;

        if (permanent) deadPieces.add(piece);
        pieces.remove(piece);
    }
    public void addPiece(Piece piece) { pieces.add(piece); }

    public void setEnemyPlayer(Player enemyPlayer) { this.enemyPlayer = enemyPlayer; }

    public ArrayList<Piece> getPieces() { return pieces; }
    public ArrayList<Piece> getDeadPieces() { return deadPieces; }
    public King getKing() {
        for (Piece piece : pieces) {
            if (piece instanceof King) return (King) piece;
        }

        return null;
    }
    public boolean isWhite() { return isWhite; }

    public String toString() {
        StringBuilder returnString = new StringBuilder();
        returnString.append(isWhite ? "White: " : "Black: ");

        for (Piece piece : pieces) {
            returnString.append(piece.getSymbol());
            returnString.append(" ");
        }

        return returnString.toString();
    }
}
