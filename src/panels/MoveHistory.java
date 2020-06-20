package panels;

import entities.Board;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;

public class MoveHistory extends JPanel implements ActionListener {
    Board board;

    JTable moveDisplay;
    ArrayList<Object[]> allMoveData;

    public MoveHistory(Board board) {
        setBackground(new Color(194, 194, 194));
        this.board = board;
        allMoveData = new ArrayList<>();

        Object[] headers = {"Turn", "White", "Black"};
        allMoveData.add(new Object[] {1, "", ""});

        moveDisplay = new JTable(readableDataForTable(), headers);
        moveDisplay.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        add(new JScrollPane(moveDisplay));
    }

    public void updateMoves() {

    }

    public Object[][] readableDataForTable() {
        Object[][] readableMoveData = new Object[allMoveData.size()][allMoveData.get(0).length];

        for (int i=0; i<allMoveData.size(); i++) {
            Object[] moveDataRow = new Object[readableMoveData[0].length];
            System.arraycopy(allMoveData.get(i), 0, moveDataRow, 0, allMoveData.get(i).length);
            readableMoveData[i] = moveDataRow;
        }

        return readableMoveData;
    }

    public void actionPerformed(ActionEvent ae) {
        repaint();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //g.setColor(Color.blue);
        //g.fillRect(0, 0, getWidth(), getHeight());
    }

}
