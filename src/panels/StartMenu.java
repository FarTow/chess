package panels;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class StartMenu extends JPanel {
    public StartMenu() {
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) { // review time
                super.componentResized(e);

                if (getComponentCount() == 0) return;

                for (int i=0; i<getComponentCount(); i++) {
                    switch(i) {
                        case 0: // top rigid body
                            Main.forceSize(new Dimension(getWidth(), getHeight() / 3), getComponent(i));
                            break;
                        case 2: // middle rigid body
                            Main.forceSize(new Dimension(getWidth(), getHeight() / 20), getComponent(i));
                            break;
                        default: // buttons
                            getComponent(i).setFont(getComponent(i).getFont().deriveFont((float) Math.min(getHeight()/30, getWidth()/50)));
                            Main.forceSize(new Dimension(getWidth()/6, getHeight()/20), getComponent(i));
                            break;
                    }
                }

                updateUI();
            }
        });
    }

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
            getComponent(i).setFont(new Font("Serif", Font.PLAIN, Math.min(getHeight()/30, getWidth()/50)));

            ((JButton) getComponent(i)).setAlignmentX(Component.CENTER_ALIGNMENT);
            getComponent(i).setFocusable(false);
        }

    }

}
