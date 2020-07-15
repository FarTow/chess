package panels;

import javax.swing.*;

import java.awt.*;

public class StartMenu extends JPanel {
    public void initUI(Main main) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Create start button
        JButton startButton = new JButton("Start");
        startButton.addActionListener(ae -> {
            ((CardLayout) main.getContentPane().getLayout()).show(main.getContentPane(), Main.GAME_LABEL);
            main.getGame().start();
        });

        // Add buttons
        add(Box.createRigidArea(new Dimension(main.getWidth(), main.getHeight()/3)));
        add(startButton);
        add(Box.createRigidArea(new Dimension(main.getWidth(), main.getHeight()/20)));
        add(new JButton("Set Time Mode"));

        for (int i=0; i<getComponentCount(); i++) {
            if (!(getComponent(i) instanceof JButton)) {
                continue;
            }

            Main.forceSize(new Dimension(main.getWidth()/6, main.getHeight()/20), getComponent(i));
            getComponent(i).setFont(new Font("Serif", Font.PLAIN, 20));

            ((JButton) getComponent(i)).setAlignmentX(Component.CENTER_ALIGNMENT);
            getComponent(i).setFocusable(false);
        }

    }

}
