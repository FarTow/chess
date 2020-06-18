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

    public void resize() {
        moveHistory.setPreferredSize(new Dimension(getWidth()/3, 480));
        getComponent(0).setPreferredSize(new Dimension(0, getHeight()/10));
        getComponent(1).setPreferredSize(new Dimension(0, getHeight()/10));
        getComponent(2).setPreferredSize(new Dimension(getWidth()/3, 0));
    }

    public void actionPerformed(ActionEvent ae) {
        board.actionPerformed(ae);
        moveHistory.actionPerformed(ae);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        resize();
    }

    public void start() {
        Dimension squareSize = new Dimension(60, 60);
        Point gridTopLeft = new Point(20,0);

        setLayout(new BorderLayout());

        board = new Board(gridTopLeft, squareSize);
        moveHistory = new MoveHistory(board);

        moveHistory.setPreferredSize(new Dimension(getWidth()/3, 480));

        add(Box.createVerticalStrut(getHeight()/10), BorderLayout.NORTH);
        add(Box.createVerticalStrut(getHeight()/10), BorderLayout.SOUTH);
        add(Box.createHorizontalStrut(getWidth()/3), BorderLayout.WEST);
        add(board, BorderLayout.CENTER);
        add(moveHistory, BorderLayout.EAST);
        addMouseListener(board);
        addMouseMotionListener(board);

        setBackground(new Color(194, 194, 194));

        Timer timer = new Timer(1000/Game.FRAME_RATE, this);
        timer.start();
    }

}
