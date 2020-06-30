package entities;

import panels.Board;

import java.util.ArrayList;

public class Player {
    private final Board board;
    private final boolean isWhite;
    private ArrayList<Piece> pieces;
    private final ArrayList<Piece> deadPieces;

    public Player(boolean isWhite, Board board) {
        this.isWhite = isWhite;
        this.board = board;
        resetPieces();
        deadPieces = new ArrayList<>();
    }

    public void resetPieces() {
        pieces = new ArrayList<>();

        int startRow = isWhite ? 7 : 0;

        for (int i=0; i<=7; i++) {
            switch(i) {
                case 0:
                case 7:
                    pieces.add(new Rook(isWhite, board.getGrid()[startRow][i]));
                    break;
                case 1:
                case 6:
                    pieces.add(new Knight(isWhite, board.getGrid()[startRow][i]));
                    break;
                case 2:
                case 5:
                    pieces.add(new Bishop(isWhite, board.getGrid()[startRow][i]));
                    break;
                case 3:
                    pieces.add(new Queen(isWhite, board.getGrid()[startRow][i]));
                    break;
                case 4:
                    pieces.add(new King(isWhite, board.getGrid()[startRow][i]));
                    break;
            }
        }

        for (int i=0; i<=7; i++) {
            pieces.add(new Pawn(isWhite, board.getGrid()[startRow + (isWhite ? -1 : 1)][i]));
        }
    }
    public void updatePieces() {
        for (Piece piece : pieces) piece.update(board);

        getKing().setCheck(isKingInCheck());
    }

    public boolean isKingInCheck() {
        int checkCount = 0;

        for (Piece piece : isWhite ? board.getBlackPlayer().getPieces() : board.getWhitePlayer().getPieces() ) {
            if (piece.canMove(getKing().getSquare())) checkCount++;
        }

        return checkCount > 0;
    }

    public void removePiece(Piece piece) {
        deadPieces.add(piece);
        pieces.remove(piece);
    }

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
            returnString.append(", ");
        }

        return returnString.toString();
    }
}
