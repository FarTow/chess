package panels;

import entities.Pawn;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;

public class MoveHistory extends GameComponent implements ActionListener {
    private final Board board;
    private final Object[] headers = new Object[] {"Turn", "White", "Black"};

    private final DefaultTableModel moveDisplayModel;
    private final ArrayList<Object[]> allMoveData;

    private boolean whiteTurn;
    private int moveCount;
    private int pieceCount;

    public MoveHistory(Point initialTopLeft, Dimension initialSize, Board board) {
        super(initialTopLeft);

        setLayout(new BorderLayout(0, 0));

        this.board = board;
        whiteTurn = true;
        moveCount = 1;
        pieceCount = 32;
        allMoveData = new ArrayList<>();

        allMoveData.add(new Object[] {moveCount, "", ""});

        moveDisplayModel = new DefaultTableModel(readableMoveData(), headers) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable moveDisplay = new JTable(moveDisplayModel);
        moveDisplay.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        moveDisplay.getTableHeader().setReorderingAllowed(false);
        moveDisplay.getTableHeader().setResizingAllowed(false);
        add(new JScrollPane(moveDisplay));

        Main.forceSize(initialSize, this);

        getComponent(0).setLocation(topLeft);
    }

    public void resize(int ... properties) {
        getComponent(0).setLocation(topLeft);
    }

    public Object[][] readableMoveData() {
        Object[][] readableMoveData = new Object[allMoveData.size()][allMoveData.get(0).length];

        for (int i=0; i<allMoveData.size(); i++) {
            Object[] moveDataRow = new Object[readableMoveData[0].length];
            System.arraycopy(allMoveData.get(i), 0, moveDataRow, 0, allMoveData.get(i).length);
            readableMoveData[i] = moveDataRow;
        }

        return readableMoveData;
    }
    public String lastMove(boolean pieceTaken) {
        if ((whiteTurn ? board.getWhitePlayer().getKing() : board.getBlackPlayer().getKing()).getCastled() == 1) return "O-O";
        if ((whiteTurn ? board.getWhitePlayer().getKing() : board.getBlackPlayer().getKing()).getCastled() == 2) return "O-O-O";

        StringBuilder chessNotation = new StringBuilder();
        int oldRank = (4 + (4 - board.getOldSquare().x));
        char oldFile = (char) ((char) 97 + board.getOldSquare().y);
        int newRank = (4 + (4 - board.getNewSquare().x));
        char newFile = (char) ((char) 97 + board.getNewSquare().y);

        chessNotation.append(board.getLastPiece().getSymbol()); // symbol of the piece that moved

        if (pieceTaken) {
            if (board.getLastPiece() instanceof Pawn) chessNotation.append(oldFile); // include file name if the piece is a pawn
            chessNotation.append('×'); // captured symbol
        }

        if (board.isMoveAmbiguous()) { // self explanatory
            if (board.isColumnAmbiguous()) {
                chessNotation.append(oldRank);
            } else {
                chessNotation.append(oldFile);
            }
        }

        chessNotation.append(newFile); // file
        chessNotation.append(newRank); // rank

        if (board.getWhiteTurn() ? board.getWhitePlayer().isKingInCheck() : board.getBlackPlayer().isKingInCheck()) {
            if (board.availableMoves(board.getWhiteTurn()) == 0) { // if it's checkmate
                chessNotation.append('#');
            } else { // if the king is in check
                chessNotation.append('+');
            }
        } else {
            if (board.availableMoves(board.getWhiteTurn()) == 0) { // if it's stalemate
                chessNotation.append('$');
            }
        }

        return chessNotation.toString();
    }
    public void updateAllMoveData() {
        if (whiteTurn != board.getWhiteTurn()) { // essentially "if a move took place"
            int newPieceCount = board.getWhitePlayer().getPieces().size() + board.getBlackPlayer().getPieces().size();
            boolean pieceTaken = pieceCount == newPieceCount+1;

            if (board.getWhiteTurn()) { // if black was the one to move (as it's white's turn now)
                allMoveData.get(moveCount-1)[2] = lastMove(pieceTaken); // update black's move

                moveCount++; // go to next move
                allMoveData.add(new Object[] {moveCount, "", ""}); // new row of data
            } else { // if white was the one to move
                allMoveData.get(moveCount-1)[1] = lastMove(pieceTaken); // update white's move
            }

            pieceCount = newPieceCount;
            moveDisplayModel.setDataVector(readableMoveData(), headers); // reset data vector to accommodate for new data
            whiteTurn = board.getWhiteTurn();
        }
    }

    public void actionPerformed(ActionEvent ae) {
        updateAllMoveData();
        repaint();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

}
