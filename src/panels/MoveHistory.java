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

    public MoveHistory(Point initialTopLeft, Board board) {
        super(initialTopLeft);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.board = board;
        whiteTurn = true;
        moveCount = 1;
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
        add(new JScrollPane(moveDisplay));
    }

    public void resize() {

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
    public String lastMove() {
        String chessNotation = "";
        int oldRank = 4 + (4 - board.getOldSquare().x);
        char oldFile = (char) ((char) 97 + board.getOldSquare().y);
        int newRank = 4 + (4 - board.getNewSquare().x);
        char newFile = (char) ((char) 97 + board.getNewSquare().y);

        chessNotation += board.getLastPiece().getSymbol(); // symbol of the piece that moved

        if (board.getTakenPiece()) { // if a piece is captured
            if (board.getLastPiece() instanceof Pawn) chessNotation += oldFile; // include file name if the piece is a pawn
            chessNotation += "×"; // captured symbol
        }

        if (board.isMoveAmbiguous()) { // self explanatory
            if (board.isColumnAmbiguous()) {
                chessNotation += oldRank;
            } else {
                chessNotation += oldFile;
            }
        }

        chessNotation += newFile; // file
        chessNotation += newRank; // rank

        if (board.isKingInCheck(board.getKing(board.getWhiteTurn()))) { // if the king is in check
            if (board.availableMoves(board.getWhiteTurn()) == 0) { // if it's checkmate
                chessNotation += "#";
            } else { // if the king is in check
                chessNotation += "+";
            }
        } else {
            if (board.availableMoves(board.getWhiteTurn()) == 0) { // if it's stalemate
                chessNotation += "$";
            }
        }

        return chessNotation;
    }
    public void updateAllMoveData() {
        if (whiteTurn != board.getWhiteTurn()) { // essentially "if a move took place"
            if (board.getWhiteTurn()) { // if black was the one to move (as it's white's turn now)
                allMoveData.get(moveCount-1)[2] = lastMove(); // update black's move

                moveCount++; // go to next move
                allMoveData.add(new Object[] {moveCount, "", ""}); // new row of data
            } else { // if white was the one to move
                allMoveData.get(moveCount-1)[1] = lastMove(); // update white's move
            }

            moveDisplayModel.setDataVector(readableMoveData(), headers); // reset data vector to accommodate for new data
            whiteTurn = board.getWhiteTurn();
        }
    }

    public void actionPerformed(ActionEvent ae) {
        updateAllMoveData();
        super.actionPerformed(ae);
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

}
