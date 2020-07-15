package panels;

import javax.swing.*;

import java.awt.*;

public class Main extends JFrame {
    static final String START_MENU_LABEL = "START MENU";
    static final String GAME_LABEL = "GAME";

    private final CardLayout cards;
    private final Game game;

    public static void forceSize(Dimension dim, Component ... components) {
        for (Component component : components) {
            component.setPreferredSize(dim);
            component.setMinimumSize(dim);
            component.setMaximumSize(dim);
        }
    }

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
        setSize(800, 800);
        setMinimumSize(new Dimension(1000, 650));
        setLocationRelativeTo(null); // center
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException | ClassNotFoundException e) {
            e.printStackTrace();
        }

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
