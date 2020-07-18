package panels;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class Game extends JPanel implements ActionListener {
    public static final int FRAME_RATE = 60;

    private Board board;
    private MoveHistory moveHistory;
    private PlayerInfoBox whiteInfoBox;
    private PlayerInfoBox blackInfoBox;

    public Game() {
        setBackground(Main.BACKGROUND_COLOR);
        setLayout(new GridBagLayout());

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) { // review time
            super.componentResized(e);
            if (getComponentCount() == 0) return;

            board.resize(Math.min((getWidth()/24 - 1), 60));
            moveHistory.resize();
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
        whiteInfoBox.actionPerformed(ae);
        blackInfoBox.actionPerformed(ae);
    }

    public void start() {
        GridBagConstraints c = new GridBagConstraints();

        // Create all the components to be shown in Game
        board = new Board();
        moveHistory = new MoveHistory(board);
        whiteInfoBox = new PlayerInfoBox(board.getWhitePlayer());
        blackInfoBox = new PlayerInfoBox(board.getBlackPlayer());

        Main.forceSize(new Dimension(getWidth()*9/20, getHeight()/2), board);
        Main.forceSize(new Dimension(getWidth()/4, getHeight()/2), moveHistory, whiteInfoBox, blackInfoBox);

        Main.forceSize(new Dimension(getWidth()/40, getHeight()), whiteInfoBox.getLabelPanel(), blackInfoBox.getLabelPanel());

        // Add components to grid

        // Add whiteInfoBox
        Main.setGridBagLayoutConstraints(
                c, new Insets(20, 20, 20, 20), GridBagConstraints.VERTICAL,
                0, 0, 1, 2, 0.25, 0.5, GridBagConstraints.LAST_LINE_END);
        add(whiteInfoBox, c);

        // Add blackInfoBox
        Main.setGridBagLayoutConstraints(
                c, new Insets(20, 20, 20, 20), GridBagConstraints.VERTICAL,
                0, 2, 1, 2, 0.25, 0.5, GridBagConstraints.FIRST_LINE_END);
        add(blackInfoBox, c);

        // Add board
        Main.setGridBagLayoutConstraints(
                c, new Insets(0, 0, 0, 0), GridBagConstraints.BOTH,
                1, 0, 1, 4, 0.5, 1.0, GridBagConstraints.CENTER);
        add(board, c);

        // Add moveHistory
        Main.setGridBagLayoutConstraints(
                c, new Insets(20, 10, 20, 20), GridBagConstraints.VERTICAL,
                2, 0, 1, 2, 0.25, 0.5, GridBagConstraints.FIRST_LINE_START);
        add(moveHistory, c);

        Timer timer = new Timer(1000/Game.FRAME_RATE, this);
        timer.start();
    }
}
