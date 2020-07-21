package panels;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class StartMenu extends JPanel { // Weird lag?
    private JButton startButton;
    private JToggleButton timeSettingsButton;
    private JPanel timeSettingsPanel;

    private int startMinutes, startSeconds, timeIncrement;
    private int oldHeight;

    public StartMenu() {
        startMinutes = startSeconds = timeIncrement = 0;
        oldHeight = 10000;

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) { // review time
                super.componentResized(e);

                if (getComponentCount() == 0) return;

                resize();

                updateUI();
            }
        });
    }

    protected void resize() {
        Main.forceSize(new Dimension(getWidth() / 5, getHeight() / 20), startButton, timeSettingsButton);
        startButton.setFont(Main.MULISH_LIGHT.deriveFont(Math.min((float) getHeight() / 30, (float) getWidth() / 55)));
        timeSettingsButton.setFont(Main.MULISH_LIGHT.deriveFont(Math.min((float) getHeight() / 30, (float) getWidth() / 55)));

        if (oldHeight != getHeight()) {
            boolean shrunk = getHeight() - oldHeight < 0;

            Main.forceSize(new Dimension(getWidth() / 5, timeSettingsPanel.getHeight() + (shrunk ? -1 : 1)), timeSettingsPanel);
        } else {
            Main.forceSize(new Dimension(getWidth() / 5, timeSettingsPanel.getHeight()), timeSettingsPanel);
        }

        oldHeight = getHeight();
    }

    protected void initUI(Main main) {
        setBackground(Main.BACKGROUND_COLOR);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Create start button
        startButton = new JButton("Start");
        startButton.addActionListener(ae -> {
            ((CardLayout) main.getContentPane().getLayout()).show(main.getContentPane(), Main.GAME_LABEL);
            main.getGame().start(startMinutes, startSeconds, timeIncrement);
        });

        startButton.setFont(Main.MULISH_LIGHT.deriveFont(Math.min((float) getHeight()/30, (float) getWidth()/55)));
        startButton.setFocusable(false);
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create time setting panel
        timeSettingsPanel = new JPanel();
        Main.forceSize(new Dimension(getWidth()/5, 1), timeSettingsPanel);
        timeSettingsPanel.setFocusable(false);
        timeSettingsPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        timeSettingsPanel.updateUI();

        // Create time setting button
        timeSettingsButton = new JToggleButton("Time Settings");
        timeSettingsButton.addItemListener(itemEvent -> { // HARD CARRIED BY TANVIR LIKE FOR REAL THIS TIME
            int timeSettingsPanelMaxHeight = main.getHeight()/10;
            int width = main.getWidth()/5;

            if (itemEvent.getStateChange() == ItemEvent.SELECTED) { // increase size of panel
                new Thread(() -> {
                    while (timeSettingsPanel.getHeight() < timeSettingsPanelMaxHeight) {
                        Main.forceSize(new Dimension(width, timeSettingsPanel.getHeight() + timeSettingsPanel.getHeight()/10 + 1), timeSettingsPanel);
                        timeSettingsPanel.updateUI();

                        try {
                            Thread.sleep(5);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            } else if (itemEvent.getStateChange() == ItemEvent.DESELECTED) { // decrease size of panel
                new Thread(() -> {
                    while (timeSettingsPanel.getHeight() > 0) {
                        Main.forceSize(new Dimension(width, timeSettingsPanel.getHeight()*9/10), timeSettingsPanel);
                        timeSettingsPanel.updateUI();

                        try {
                            Thread.sleep(5);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        timeSettingsButton.setFont(Main.MULISH_LIGHT.deriveFont(Math.min((float) getHeight()/30, (float) getWidth()/55)));
        timeSettingsButton.setFocusable(false);
        timeSettingsButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        Main.forceSize(new Dimension(main.getWidth()/5, main.getHeight()/15), startButton, timeSettingsButton);

        // Add buttons
        add(Box.createRigidArea(new Dimension(main.getWidth(), main.getHeight()/3)));
        add(startButton);
        add(Box.createRigidArea(new Dimension(main.getWidth(), main.getHeight()/20)));
        add(timeSettingsButton);
        add(timeSettingsPanel);

    }

}
