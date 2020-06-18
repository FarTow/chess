package panels;

import entities.Board;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class Game extends JPanel implements ActionListener {
    public static final int FRAME_RATE = 60;

    private Board board;
    private MoveHistory moveHistory;

    public Game() {
        setBackground(new Color(194, 194, 194));
    }

    public void actionPerformed(ActionEvent ae) {
        board.actionPerformed(ae);
        moveHistory.actionPerformed(ae);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    public void start() {
        Dimension squareSize = new Dimension(60, 60);
        setLayout(new BorderLayout(getWidth()/200, 0));

        board = new Board(getWidth()/100);
        moveHistory = new MoveHistory(board);

        moveHistory.setPreferredSize(new Dimension(getWidth()/3, 480));

        add(Box.createVerticalStrut(getHeight()/10), BorderLayout.NORTH);
        add(Box.createVerticalStrut(getHeight()/10), BorderLayout.SOUTH);
        add(Box.createHorizontalStrut(getWidth()/3), BorderLayout.WEST);
        add(moveHistory, BorderLayout.EAST);
        add(board, BorderLayout.CENTER);

        Timer timer = new Timer(1000/Game.FRAME_RATE, this);
        timer.start();
    }

}
