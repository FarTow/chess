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

        whiteInfoBox.add(new JLabel("White Information", JLabel.CENTER)); // add components to holders
        whiteInfoBox.add(whiteTimer);
        whiteInfoBox.add(whiteTakenPieces);

        blackInfoBox.add(new JLabel("Black Information", JLabel.CENTER));
        blackInfoBox.add(blackTimer);
        blackInfoBox.add(blackTakenPieces);

        whiteInfoBox.getComponent(0).setFont(new Font("Serif", Font.PLAIN, 20)); // configure settings
        blackInfoBox.getComponent(0).setFont(new Font("Serif", Font.PLAIN, 20));


        // Forcing sizes
        Main.forceSize(new Dimension(getWidth()/2, getHeight()/2), board);
        Main.forceSize(new Dimension(getWidth()/4, getHeight()/2), moveHistory, whiteInfoBox, blackInfoBox);

        // Add components to grid

        // Add whiteInfoBox
        setGridBagLayoutConstraints(c, GridBagConstraints.VERTICAL, 0, 0, 1, 2, 0.25, 0.5, GridBagConstraints.LAST_LINE_END);
        add(whiteInfoBox, c);

        // Add blackInfoBox
        setGridBagLayoutConstraints(c, GridBagConstraints.VERTICAL, 0, 2, 1, 2, 0.25, 0.5, GridBagConstraints.FIRST_LINE_END);
        add(blackInfoBox, c);

        // Add board
        setGridBagLayoutConstraints(c, GridBagConstraints.BOTH, 1, 0, 1, 4, 0.5, 1.0, GridBagConstraints.CENTER);
        add(board, c);

        // Add moveHistory
        setGridBagLayoutConstraints(c, GridBagConstraints.VERTICAL, 2, 0, 1, 2, 0.25, 0.5, GridBagConstraints.FIRST_LINE_START);
        add(moveHistory, c);

        Timer timer = new Timer(1000/Game.FRAME_RATE, this);
        timer.start();
    }

    private void setGridBagLayoutConstraints(GridBagConstraints c, int fill, int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty, int anchor) {
        c.fill = fill;
        c.gridx = gridx;
        c.gridy = gridy;
        c.gridwidth = gridwidth;
        c.gridheight = gridheight;
        c.weightx = weightx;
        c.weighty = weighty;
        c.anchor = anchor;
    }

}
