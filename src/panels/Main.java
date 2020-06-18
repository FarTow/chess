package panels;

import javax.swing.JFrame;

import java.awt.*;

public class Main extends JFrame {
    static final String START_MENU_LABEL = "START MENU";
    static final String GAME_LABEL = "GAME";

    private final CardLayout cards;
    private final Game game;

    Main() {
        cards = new CardLayout();
        StartMenu startMenu = new StartMenu(this);
        game = new Game();

        setLayout(cards);
        add(startMenu, START_MENU_LABEL);
        add(game, GAME_LABEL);
    }

    private void initUI() {
        setTitle("Chess");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setMinimumSize(new Dimension(600, 600));
        setLocationRelativeTo(null); // center
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        cards.show(getContentPane(), START_MENU_LABEL);
    }
    void createAndShowGUI() {
        initUI();
        setVisible(true);
    }

    public Game getGame() {
        return game;
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.createAndShowGUI();
    }

}
