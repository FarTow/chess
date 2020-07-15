package panels;

import javax.swing.*;

import java.awt.*;

public class StartMenu extends JPanel {
    public StartMenu(Main main) {
        initUI(main);
    }

    private void initUI(Main main) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JButton startButton = new JButton("Start");
        startButton.addActionListener(ae -> {
            ((CardLayout) main.getContentPane().getLayout()).show(main.getContentPane(), Main.GAME_LABEL);
            main.getGame().start();
        });

        startButton.setFocusable(false);

        add(startButton);
        add(new JButton("haggo"));

        setAlignmentY(Component.CENTER_ALIGNMENT);
    }

}
