package panels;

import entities.Board;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class Game extends JPanel implements ActionListener {
    public static final int FRAME_RATE = 60;

    private Board board;

    public Game() {
        setBackground(new Color(194, 194, 194));
    }

    public void actionPerformed(ActionEvent ae) {
        board.actionPerformed(ae);
    }

    public void start() {
        Dimension screenSize = new Dimension(getWidth(), getHeight());
        Dimension squareSize = new Dimension(60, 60);
        Point gridTopLeft = new Point((screenSize.width-(squareSize.width*8))/2, (screenSize.height-(squareSize.height*8))/2);

        setLayout(new BorderLayout());

        board = new Board(gridTopLeft, squareSize);
        MoveHistory moveHistory = new MoveHistory(board);

        add(board, BorderLayout.CENTER);
        add(moveHistory, BorderLayout.EAST);
        addMouseListener(board);
        addMouseMotionListener(board);

        Timer timer = new Timer(1000/Game.FRAME_RATE, this);
        timer.start();
    }

}
