package panels;

import entities.Board;

import javax.swing.JPanel;

import java.awt.*;
import java.awt.event.*;

public class Game extends JPanel implements ActionListener {
    public static final int FRAME_RATE = 60;

    private Board board;

    public Game() { }

    public void actionPerformed(ActionEvent ae) {
        repaint();
    }
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // clear screen
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        setBackground(new Color(194, 194, 194));
    }
    public void start() {
        Dimension screenSize = new Dimension(getWidth(), getHeight());
        Dimension squareSize = new Dimension(60, 60);
        Point gridTopLeft = new Point((screenSize.width-(squareSize.width*8))/2, (screenSize.height-(squareSize.height*8))/2);

        setLayout(new BorderLayout());

        board = new Board(gridTopLeft, squareSize);
        add(board, BorderLayout.CENTER);
        addMouseListener(board);
        addMouseMotionListener(board);

        board.start();
    }

}
