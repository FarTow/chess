package panels;

import entities.Board;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

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

        moveDisplayModel = new DefaultTableModel(readableMoveData(), headers);
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

    public String convertToChessNotation(Point lastMove) {
        String chessNotation = "";

        chessNotation += (char) ((char) 97+lastMove.x);
        chessNotation += lastMove.y;

        return chessNotation;
    }

    public void actionPerformed(ActionEvent ae) {
        repaint();

        if (whiteTurn != board.getWhiteTurn()) {
            if (board.getWhiteTurn()) {
                allMoveData.get(moveCount-1)[2] = convertToChessNotation(board.getLastMove());

                moveCount++;
                allMoveData.add(new Object[] {moveCount, "", ""});
            } else {
                allMoveData.get(moveCount-1)[1] = convertToChessNotation(board.getLastMove());
            }

            moveDisplayModel.setDataVector(readableMoveData(), headers);
            whiteTurn = board.getWhiteTurn();
        }

    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

}
