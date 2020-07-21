package panels;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class StartMenu extends JPanel { // Weird lag?
    private int startMinutes, startSeconds, timeIncrement;

    public StartMenu() {
        startMinutes = startSeconds = timeIncrement = 0;

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) { // review time
                super.componentResized(e);

                if (getComponentCount() == 0) return;

                for (int i=0; i<getComponentCount(); i++) {
                    switch(i) {
                        case 0: // top rigid body
                            Main.forceSize(new Dimension(getWidth(), getHeight()/3), getComponent(i));
                            break;
                        case 2: // middle rigid body
                            Main.forceSize(new Dimension(getWidth(), getHeight()/20), getComponent(i));
                            break;
                        case 4:
                            Main.forceSize(new Dimension(getWidth()/6, getComponent(i).getHeight()*9/10));
                            break;
                        default: // buttons
                            getComponent(i).setFont(getComponent(i).getFont().deriveFont((float) Math.min(getHeight()/30, getWidth()/55)));
                            Main.forceSize(new Dimension(getWidth()/6, getHeight()/15), getComponent(i));
                            break;
                    }
                }

                updateUI();
            }
        });
    }

    protected void initUI(Main main) {
        setBackground(Main.BACKGROUND_COLOR);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Create start button
        JButton startButton = new JButton("Start");
        startButton.addActionListener(ae -> {
            ((CardLayout) main.getContentPane().getLayout()).show(main.getContentPane(), Main.GAME_LABEL);
            main.getGame().start(startMinutes, startSeconds, timeIncrement);
        });

        startButton.setFont(Main.MULISH_LIGHT.deriveFont(Math.min((float) getHeight()/30, (float) getWidth()/55)));
        startButton.setFocusable(false);
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create time setting panel
        JPanel timeSettingsPanel = new JPanel();
        Main.forceSize(new Dimension(getWidth()/6, 1), timeSettingsPanel);
        timeSettingsPanel.setFocusable(false);
        timeSettingsPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        timeSettingsPanel.updateUI();

        // Create time setting button
        JToggleButton timeSettingsButton = new JToggleButton("Time Settings");
        timeSettingsButton.addItemListener(itemEvent -> { // HARD CARRIED BY TANVIR LIKE FOR REAL THIS TIME
            int timeSettingsPanelMaxHeight = main.getHeight()/10;

            System.out.println(timeSettingsPanelMaxHeight);

            if (itemEvent.getStateChange() == ItemEvent.SELECTED) { // increase size of panel
                new Thread(() -> {
                    while (timeSettingsPanel.getHeight() < timeSettingsPanelMaxHeight) {
                        System.out.println("Start " + timeSettingsPanel.getWidth() + ", " + timeSettingsPanel.getHeight());
                        Main.forceSize(new Dimension(main.getWidth() / 6, timeSettingsPanel.getHeight() + timeSettingsPanel.getHeight()/10 + 1), timeSettingsPanel);
                        timeSettingsPanel.updateUI();

                        System.out.println("End " + timeSettingsPanel.getWidth() + ", " + timeSettingsPanel.getHeight());
                    }

                    try {
                        Thread.sleep(50);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            } else if (itemEvent.getStateChange() == ItemEvent.DESELECTED) { // decrease size of panel
                new Thread(() -> {
                    while (timeSettingsPanel.getHeight() > 0) {
                        Main.forceSize(new Dimension(main.getWidth()/6, timeSettingsPanel.getHeight()*9/10), timeSettingsPanel);
                        timeSettingsPanel.updateUI();
                    }

                    try {
                        Thread.sleep(50);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        });

        timeSettingsButton.setFont(Main.MULISH_LIGHT.deriveFont(Math.min((float) getHeight()/30, (float) getWidth()/55)));
        timeSettingsButton.setFocusable(false);
        timeSettingsButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        Main.forceSize(new Dimension(main.getWidth()/6, main.getHeight()/15), startButton, timeSettingsButton);

        // Add buttons
        add(Box.createRigidArea(new Dimension(main.getWidth(), main.getHeight()/3)));
        add(startButton);
        add(Box.createRigidArea(new Dimension(main.getWidth(), main.getHeight()/20)));
        add(timeSettingsButton);
        add(timeSettingsPanel);

    }

}
