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
        moveHistory.setPreferredSize(new Dimension(getWidth()/3, getHeight()*4/5));
        moveHistory.setMinimumSize(new Dimension(getWidth()/3, getHeight()*4/5));
        moveHistory.setMaximumSize(new Dimension(getWidth()/3, getHeight()*4/5));
        board.setPreferredSize(new Dimension(getWidth()/3, getHeight()*4/5));
        board.setMinimumSize(new Dimension(getWidth()/3, getHeight()*4/5));
        board.setMaximumSize(new Dimension(getWidth()/3, getHeight()*4/5));

        getComponent(2).setPreferredSize(new Dimension(getWidth()/3, getHeight()*4/5));
        getComponent(2).setMinimumSize(new Dimension(getWidth()/3, getHeight()*4/5));
        getComponent(2).setMaximumSize(new Dimension(getWidth()/3, getHeight()*4/5));

        getComponent(0).setSize(new Dimension(getWidth(), getHeight()/10));
        getComponent(1).setSize(new Dimension(getWidth(), getHeight()/10));
        //getComponent(2).setSize(new Dimension(getWidth()/3, getHeight()*4/5));
        moveHistory.setSize(new Dimension(getWidth()/3, getHeight()*4/5));
        board.setSize(new Dimension(getWidth()/3, getHeight()*4/5));

        board.setxMargin(getWidth()/100);
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
        setLayout(new BorderLayout(getWidth()/1000, 0));

        board = new Board(getWidth()/100);
        moveHistory = new MoveHistory(board);

        moveHistory.setPreferredSize(new Dimension(getWidth()/3, getHeight()*4/5));
        board.setPreferredSize(new Dimension(getWidth()/3, getHeight()*4/5));

        add(Box.createRigidArea(new Dimension(getWidth(), getHeight()/10)), BorderLayout.NORTH);
        add(Box.createRigidArea(new Dimension(getWidth(), getHeight()/10)), BorderLayout.SOUTH);
        add(Box.createRigidArea(new Dimension(getWidth()/3, getHeight())), BorderLayout.WEST);
        add(moveHistory, BorderLayout.EAST);
        add(board, BorderLayout.CENTER);

        Timer timer = new Timer(1000/Game.FRAME_RATE, this);
        timer.start();
    }

}
