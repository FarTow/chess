package panels;

import entities.*;

import javax.imageio.ImageIO;
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

    public static Image[] greyPieceImages;
    public static Image[] whitePieceImages;
    public static Image[] blackPieceImages;

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
        greyPieceImages = new Image[6];
        whitePieceImages = new Image[6];
        blackPieceImages = new Image[6];

        initFonts();
        initPieceImages(greyPieceImages, "grey");
        initPieceImages(whitePieceImages, "white");
        initPieceImages(blackPieceImages, "black");

        cards = new CardLayout();
        startMenu = new StartMenu();
        game = new Game();

        setLayout(cards);
        add(startMenu, START_MENU_LABEL);
        add(game, GAME_LABEL);
    }

    private void initFonts() {
        try {
            MULISH_LIGHT = Font.createFont(Font.TRUETYPE_FONT, new File("res/fonts/static/Mulish-Light.ttf"));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(MULISH_LIGHT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void initPieceImages(Image[] pieceImages, String color) {
        String piecePathStart = "res/pieces/";
        String piecePathEnd = ".png";

        for (int i=0; i<pieceImages.length; i++) {
            String pieceName;

            switch (i) {
                case 0:
                    pieceName = "bishop";
                    break;
                case 1:
                    pieceName = "king";
                    break;
                case 2:
                    pieceName = "knight";
                    break;
                case 3:
                    pieceName = "queen";
                    break;
                case 4:
                    pieceName = "pawn";
                    break;
                case 5:
                    pieceName = "rook";
                    break;
                default:
                    pieceName = "";
                    break;
            }

            try {
                pieceImages[i] = ImageIO.read(new File(piecePathStart+color+"-"+pieceName+piecePathEnd));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
