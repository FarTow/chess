package panels;

import javax.swing.JPanel;
import javax.swing.JButton;

import java.awt.CardLayout;

public class StartMenu extends JPanel {
    private JButton startButton;

    public StartMenu(Main main) {
        startButton = new JButton("Start");
        startButton.addActionListener(ae -> {
            ((CardLayout) main.getContentPane().getLayout()).show(main.getContentPane(), Main.GAME_LABEL);
            main.getGame().start();
        });
        startButton.setFocusable(false);

        add(startButton);
    }

}
