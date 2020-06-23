package panels;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class Game extends JPanel implements ActionListener {
    public static final int FRAME_RATE = 60;

    private JPanel centerDisplay;
    private JPanel eastDisplay;

    private Board board;
    private MoveHistory moveHistory;
    private TakenPieces whiteTakenPieces;
    private TakenPieces blackTakenPieces;

    public Game() {
        setBackground(new Color(194, 194, 194));
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) { // review time
            super.componentResized(e);

            if (getComponentCount() == 0) return;

            Main.forceSize(new Dimension(getWidth(), getHeight()/10), getComponent(0), getComponent(1));
            Main.forceSize(new Dimension(getWidth()/3, getHeight()*4/5), getComponent(2), centerDisplay, eastDisplay);
            board.resize(Math.min((getWidth()/24 - 1), 60));
            moveHistory.resize();
            whiteTakenPieces.resize(Math.min((getWidth()/24 - 1), 60));
            blackTakenPieces.resize(Math.min((getWidth()/24 - 1), 60));

            updateUI();
            }
        });
    }

    public void actionPerformed(ActionEvent ae) {
        board.actionPerformed(ae);
        moveHistory.actionPerformed(ae);
        whiteTakenPieces.actionPerformed(ae);
        blackTakenPieces.actionPerformed(ae);
    }
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    public void start() {
        setLayout(new BorderLayout(getWidth()/500, 0));

        centerDisplay = new JPanel(new BorderLayout());
        eastDisplay = new JPanel(new BorderLayout());

        board = new Board(new Point(10,getHeight()*2/5 - 240));
        moveHistory = new MoveHistory(new Point(0, getHeight()*2/5), new Dimension(getWidth()/3, getHeight()*2/5), board);
        whiteTakenPieces = new TakenPieces(new Point(0, getHeight()/5), true, board);
        blackTakenPieces = new TakenPieces(new Point(0, getHeight()*4/5), false, board);

        centerDisplay.add(board, BorderLayout.CENTER);
        eastDisplay.add(moveHistory, BorderLayout.CENTER);
        eastDisplay.add(blackTakenPieces, BorderLayout.NORTH);
        eastDisplay.add(whiteTakenPieces, BorderLayout.SOUTH);

        Main.forceSize(new Dimension(getWidth()/3, getHeight()*4/5), centerDisplay, eastDisplay);

        add(Box.createRigidArea(new Dimension(getWidth(), getHeight()/10)), BorderLayout.NORTH);
        add(Box.createRigidArea(new Dimension(getWidth(), getHeight()/10)), BorderLayout.SOUTH);
        add(Box.createRigidArea(new Dimension(getWidth()/3, getHeight())), BorderLayout.WEST);
        add(centerDisplay, BorderLayout.CENTER);
        add(eastDisplay, BorderLayout.EAST);

        Timer timer = new Timer(1000/Game.FRAME_RATE, this);
        timer.start();
    }

}
