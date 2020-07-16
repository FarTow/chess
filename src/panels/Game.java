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

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) { // review time
            super.componentResized(e);
            if (getComponentCount() == 0) return;

            board.resize(Math.min((getWidth()/24 - 1), 60));
            updateUI();
            }
        });
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

        // Create and initialize any necessary holders
        JPanel whiteInfoBox = new JPanel();
        JPanel blackInfoBox = new JPanel();

        whiteInfoBox.setLayout(new BoxLayout(whiteInfoBox, BoxLayout.Y_AXIS));
        blackInfoBox.setLayout(new BoxLayout(blackInfoBox, BoxLayout.Y_AXIS));

        whiteInfoBox.add(whiteTimer);
        whiteInfoBox.add(whiteTakenPieces);

        blackInfoBox.add(blackTimer);
        blackInfoBox.add(blackTakenPieces);

        //Main.forceSize(new Dimension(getWidth()*3/5, getHeight()*2/3), board);
        //Main.forceSize(new Dimension(getWidth()/5, getHeight()/4), moveHistory);

        // Add components to grid

        // Add whiteInfoBox
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 2;
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.anchor = GridBagConstraints.CENTER;
        add(whiteInfoBox, c);

        // Add blackInfoBox
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        c.gridheight = 2;
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.anchor = GridBagConstraints.PAGE_END;
        add(blackTakenPieces, c);

        // Add board
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 3;
        c.gridheight = 4;
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.anchor = GridBagConstraints.CENTER;
        add(board, c);

        // Add whiteTimer
        c.gridx = 1;
        c.gridy = 5;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.anchor = GridBagConstraints.PAGE_START;
        add(whiteTimer, c);

        // Add moveHistory
        c.gridx = 3;
        c.gridy = 2;
        c.gridwidth = 2;
        c.gridheight = 2;
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.anchor = GridBagConstraints.CENTER;
        add(moveHistory, c);

        // Add whiteTakenPieces
        c.gridx = 3;
        c.gridy = 4;
        c.gridwidth = 2;
        c.gridheight = 2;
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.anchor = GridBagConstraints.PAGE_START;
        add(whiteTakenPieces, c);

        Timer timer = new Timer(1000/Game.FRAME_RATE, this);
        timer.start();
    }

    private void setGridBagLayoutConstraints(GridBagConstraints c, int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty, int anchor) {
        c.gridx = gridx;
        c.gridy = gridy;
        c.gridwidth = gridwidth;
        c.gridheight = gridheight;
        c.weightx = weightx;
        c.weighty = weighty;
        c.anchor = anchor;
    }

}
