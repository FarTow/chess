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
    private TakenPieces blackTakenPieces;
    private TakenPieces whiteTakenPieces;

    public Game() {
        setBackground(Main.BACKGROUND_COLOR);
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) { // review time
            super.componentResized(e);
            resize();
            updateUI();
            }
        });
    }

    public void resize() {
        if (getComponentCount() == 0) return;
        Main.forceSize(new Dimension(getWidth(), getHeight()/10), getComponent(0), getComponent(1));
        Main.forceSize(new Dimension(getWidth()/3, getHeight()*4/5), getComponent(2), centerDisplay, eastDisplay);
        board.resize(Math.min((getWidth()/24 - 1), 60));
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
        setLayout(new BorderLayout(getWidth()/500, 0));

        // Initialize displays to be used by border layout
        centerDisplay = new JPanel();
        eastDisplay = new JPanel();
        Main.forceSize(new Dimension(getWidth()/3, getHeight()*4/5), centerDisplay, eastDisplay); // set size
        centerDisplay.setLayout(new BoxLayout(centerDisplay, BoxLayout.Y_AXIS)); // layout
        eastDisplay.setLayout(new BoxLayout(eastDisplay, BoxLayout.Y_AXIS));
        centerDisplay.setOpaque(false); // background color
        eastDisplay.setOpaque(false);

        // Create specific panels that will be arranged around the Game panel
        board = new Board(new Point(10,getHeight()*2/5 - 240));
        moveHistory = new MoveHistory(new Dimension(getWidth()/3, getHeight()*2/5), board);
        blackTakenPieces = new TakenPieces(board.getBlackPlayer());
        whiteTakenPieces = new TakenPieces(board.getWhitePlayer());
        Main.forceSize(new Dimension(getWidth()/3, getHeight()/5), blackTakenPieces, whiteTakenPieces);

        // Add components to displays
        centerDisplay.add(board);
        eastDisplay.add(blackTakenPieces);
        eastDisplay.add(moveHistory);
        eastDisplay.add(whiteTakenPieces);

        // Add all displays to Game
        add(Box.createRigidArea(new Dimension(getWidth(), getHeight()/10)), BorderLayout.NORTH);
        add(Box.createRigidArea(new Dimension(getWidth(), getHeight()/10)), BorderLayout.SOUTH);
        add(Box.createRigidArea(new Dimension(getWidth()/3, getHeight())), BorderLayout.WEST);
        add(centerDisplay, BorderLayout.CENTER);
        add(eastDisplay, BorderLayout.EAST);

        Timer timer = new Timer(1000/Game.FRAME_RATE, this);
        timer.start();
    }

}
