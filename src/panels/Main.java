package panels;

import entities.*;

import javax.swing.*;

import java.awt.*;
import java.io.File;

public class Main extends JFrame {
    public static final Color BACKGROUND_COLOR = new Color(212, 202, 186);
    public static Font MULISH_LIGHT;

    public static int BISHOP_IMAGE_INDEX = 0;
    public static int KING_IMAGE_INDEX = 1;
    public static int KNIGHT_IMAGE_INDEX = 2;
    public static int QUEEN_IMAGE_INDEX = 3;
    public static int PAWN_IMAGE_INDEX = 4;
    public static int ROOK_IMAGE_INDEX = 5;

    public static final Object[] whitePieceIcons = new Object[] {
            new ImageIcon(new Bishop(true).getImage()),
            new ImageIcon(new King(true).getImage()),
            new ImageIcon(new Knight(true).getImage()),
            new ImageIcon(new Queen(true).getImage()),
            new ImageIcon(new Pawn(true).getImage()),
            new ImageIcon(new Rook(true).getImage())
    };
    public static final Object[] blackPieceIcons = new Object[] {
            new ImageIcon(new Bishop(false).getImage()),
            new ImageIcon(new King(false).getImage()),
            new ImageIcon(new Knight(false).getImage()),
            new ImageIcon(new Queen(false).getImage()),
            new ImageIcon(new Pawn(false).getImage()),
            new ImageIcon(new Rook(false).getImage())
    };

    public static final String START_MENU_LABEL = "START MENU";
    public static final String GAME_LABEL = "GAME";

    private final CardLayout cards;
    private final StartMenu startMenu;
    private final Game game;

    public static void forceSize(Dimension dim, Component ... components) {
        for (Component component : components) {
            component.setPreferredSize(dim);
            component.setMinimumSize(dim);
            component.setMaximumSize(dim);
        }
    }

    public static void setGridBagLayoutConstraints(GridBagConstraints c, Insets insets, int fill, int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty, int anchor) {
        c.insets = insets;
        c.fill = fill;
        c.gridx = gridx;
        c.gridy = gridy;
        c.gridwidth = gridwidth;
        c.gridheight = gridheight;
        c.weightx = weightx;
        c.weighty = weighty;
        c.anchor = anchor;
    }


    Main() {
        try {
            MULISH_LIGHT = Font.createFont(Font.TRUETYPE_FONT, new File("res/fonts/static/Mulish-Light.ttf"));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(MULISH_LIGHT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        cards = new CardLayout();
        startMenu = new StartMenu();
        game = new Game();

        setLayout(cards);
        add(startMenu, START_MENU_LABEL);
        add(game, GAME_LABEL);
    }

    private void initUI() {
        setTitle("Chess");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800, 650));
        setLocationRelativeTo(null); // center
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        startMenu.initUI(this);

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
