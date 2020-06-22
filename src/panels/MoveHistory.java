package panels;

import entities.Board;
import entities.Pawn;
import entities.Piece;
import entities.Square;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;

public class MoveHistory extends JPanel implements ActionListener {
    private final Board board;
    private final Object[] headers = new Object[] {"Turn", "White", "Black"};

    private final DefaultTableModel moveDisplayModel;
    private final ArrayList<Object[]> allMoveData;

    private boolean whiteTurn;
    private int moveCount;

    public MoveHistory(Board board) {
        setBackground(new Color(194, 194, 194));
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

    public Object[][] readableMoveData() {
        Object[][] readableMoveData = new Object[allMoveData.size()][allMoveData.get(0).length];

        for (int i=0; i<allMoveData.size(); i++) {
            Object[] moveDataRow = new Object[readableMoveData[0].length];
            System.arraycopy(allMoveData.get(i), 0, moveDataRow, 0, allMoveData.get(i).length);
            readableMoveData[i] = moveDataRow;
        }

        return readableMoveData;
    }

    public String lastMove(Point oldSquare, Point newSquare, Piece piece, boolean tookPiece) {
        String chessNotation = "";
        chessNotation += piece.getSymbol();

        if (tookPiece) {
            if (piece instanceof Pawn) chessNotation += (char) ((char) 97 + oldSquare.y);
            chessNotation += "×";
        }

        if (board.isMoveAmbigious()) chessNotation += "...";

        chessNotation += (char) ((char) 97 + newSquare.y); // file
        chessNotation += 4 + (4 - newSquare.x); // rank

        return chessNotation;
    }

    public void updateAllMoveData() {
        if (whiteTurn != board.getWhiteTurn()) {
            if (board.getWhiteTurn()) {
                allMoveData.get(moveCount-1)[2] = lastMove(board.getOldSquare(), board.getNewSquare(), board.getLastPiece(), board.getTookPiece());

                moveCount++;
                allMoveData.add(new Object[] {moveCount, "", ""});
            } else {
                allMoveData.get(moveCount-1)[1] = lastMove(board.getOldSquare(),board.getNewSquare(), board.getLastPiece(), board.getTookPiece());
            }

            moveDisplayModel.setDataVector(readableMoveData(), headers);
            whiteTurn = board.getWhiteTurn();
        }
    }

    public void actionPerformed(ActionEvent ae) {
        repaint();
        updateAllMoveData();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

}
