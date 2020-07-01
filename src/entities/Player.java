package entities;

import panels.Board;

import java.util.ArrayList;

public class Player {
    private final Board board;
    private final boolean isWhite;
    private ArrayList<Piece> pieces;
    private ArrayList<Piece> deadPieces;

    public Player(boolean isWhite, Board board) {
        this.isWhite = isWhite;
        this.board = board;
        resetPieces();
        deadPieces = new ArrayList<>();
    }

    public void resetPieces() {
        pieces = new ArrayList<>();
        deadPieces = new ArrayList<>();

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
        getKing().setCheck(isKingInCheck());

        for (Piece piece : pieces) {
            piece.update(board);

            ArrayList<Square> mayMoveSquares = new ArrayList<>();

            for (Square square : piece.getMoveableSquares()) {
                if (mayMove(piece, square)) mayMoveSquares.add(square);
            }

            piece.setMoveableSquares(mayMoveSquares);
        }

        System.out.println(pieces.get(0).moveableSquares);
    }

    public boolean mayMove(Piece piece, Square toSquare) {
        Piece takenPiece = toSquare.getPiece();

        if (toSquare.getPiece() != null) { // check if they're the same color
            if (piece.isWhite() == toSquare.getPiece().isWhite()) return false;
        }

        boolean mayMove;
        int oldRow = piece.getRow();
        int oldColumn = piece.getColumn();

        board.movePiece(piece, toSquare, false); // move piece to desired square
        //(piece.isWhite() ? whitePlayer : blackPlayer).updatePieces();
        mayMove = !isKingInCheck();
        board.movePiece(piece, board.getGrid()[oldRow][oldColumn], false); // move the piece back to original square
        toSquare.setPiece(takenPiece); // set the new square's piece back

        return mayMove;
    }
    public boolean isKingInCheck() {
        int checkCount = 0;

        for (Piece piece : (isWhite ? board.getBlackPlayer() : board.getWhitePlayer()).getPieces() ) {
            if (piece.canMove(getKing().getSquare())) checkCount++;
        }

        if (checkCount > 0) {
            System.out.println((isWhite ? "White " : "Black ") + "is in check.");
            System.out.println(checkCount);
        }

        return checkCount > 0;
    }

    public void removePiece(Piece piece) {
        pieces.remove(piece);
        deadPieces.add(piece);
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
            returnString.append(" ");
        }

        return returnString.toString();
    }
}
