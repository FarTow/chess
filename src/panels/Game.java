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
        Main.forceSize(new Dimension(getWidth(), getHeight()/10), getComponent(0), getComponent(1));
        Main.forceSize(new Dimension(getWidth()/3, getHeight()*4/5), moveHistory, board, getComponent(2));
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

        board = new Board();
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
