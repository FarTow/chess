package entities;

import panels.Board;

import java.util.ArrayList;

public class Player {
    private final Board board;
    private ArrayList<Piece> pieces;
    private final boolean isWhite;

    public Player(boolean isWhite, Board board) {
        this.board = board;
        this.isWhite = isWhite;
        resetPieces();
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
        pieces.remove(piece);
    }

    public ArrayList<Piece> getPieces() { return pieces; }
    public King getKing() {
        for (Piece piece : pieces) {
            if (piece instanceof King) return (King) piece;
        }

        return null;
    }
}
