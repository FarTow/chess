package panels;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class Game extends JPanel implements ActionListener {
    public static final int FRAME_RATE = 60;

    private Board board;
    private MoveHistory moveHistory;
    private TakenPieces blackTakenPieces;
    private TakenPieces whiteTakenPieces;
    private TimeDisplay blackTimer;
    private TimeDisplay whiteTimer;

    public Game() {
        setBackground(Main.BACKGROUND_COLOR);

        /*
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) { // review time
            super.componentResized(e);
            updateUI();
            }
        });

         */
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
    public void actionPerformed(ActionEvent ae) {
        board.actionPerformed(ae);
        moveHistory.actionPerformed(ae);
        whiteTakenPieces.actionPerformed(ae);
        blackTakenPieces.actionPerformed(ae);
    }

    public void start() {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        // Create all the components to be shown in Game
        board = new Board();
        moveHistory = new MoveHistory(board);
        blackTakenPieces = new TakenPieces(board.getBlackPlayer());
        whiteTakenPieces = new TakenPieces(board.getWhitePlayer());
        blackTimer = new TimeDisplay(board.getBlackPlayer());
        whiteTimer = new TimeDisplay(board.getWhitePlayer());

        Main.forceSize(new Dimension(getWidth()*2/3, getHeight()*3/5), board);
        Main.forceSize(new Dimension(getWidth()/3, getHeight()*3/5), moveHistory);

        // Add components to grid

        // Add blackTimer
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.PAGE_END;
        c.weightx = 0.33;
        c.weighty = 0.2;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        add(blackTimer, c);

        // Add board
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 0.67;
        c.weighty = 0.6;
        c.gridy = 1;
        c.gridwidth = 2;
        add(board, c);

        // Add whiteTimer
        c.anchor = GridBagConstraints.PAGE_START;
        c.weightx = 0.33;
        c.weighty = 0.2;
        c.gridy = 2;
        c.gridwidth = 1;
        add(whiteTimer, c);

        // Add blackTakenPieces
        c.anchor = GridBagConstraints.PAGE_END;
        c.weightx = 0.33;
        c.weighty = 0.2;
        c.gridx = 2;
        c.gridy = 0;
        c.gridwidth = 1;
        add(blackTakenPieces, c);

        // Add moveHistory
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 0.33;
        c.weighty = 0.6;
        c.gridy = 1;
        add(moveHistory, c);

        // Add whiteTakenPieces
        c.anchor = GridBagConstraints.PAGE_START;
        c.weightx = 0.33;
        c.weighty = 0.2;
        c.gridy = 2;
        add(whiteTakenPieces);

        Timer timer = new Timer(1000/Game.FRAME_RATE, this);
        timer.start();
    }

}
