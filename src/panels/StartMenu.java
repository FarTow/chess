package panels;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class StartMenu extends JPanel { // Weird lag?
    private final int INITIAL_RESIZE_COUNT = 2;
    private Dimension buttonSize;

    private JButton startButton;
    private JToggleButton timeSettingsButton;
    private JPanel timeSettingsPanel;

    private int startMinutes, startSeconds, timeIncrement;
    private int initialResize; // absolutely hate this.

    public StartMenu() {
        startMinutes = startSeconds = timeIncrement = 0;

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) { // review time
                super.componentResized(e);

                if (getComponentCount() == 0) return;

                resize();
                updateUI();

                initialResize++;
            }
        });
    }

    protected void resize() {
        buttonSize = new Dimension(getWidth()/5, getHeight()/20);

        Main.forceSize(buttonSize, startButton, timeSettingsButton);
        if (initialResize >= INITIAL_RESIZE_COUNT && timeSettingsButton.isSelected()) {
            Main.forceSize(new Dimension(getWidth()/5, getHeight()/10), timeSettingsPanel);
        }
        startButton.setFont(Main.MULISH_LIGHT.deriveFont(Math.min((float) getHeight() / 30, (float) getWidth() / 55)));
        timeSettingsButton.setFont(Main.MULISH_LIGHT.deriveFont(Math.min((float) getHeight() / 30, (float) getWidth() / 55)));
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
        Main.forceSize(new Dimension(getWidth()/5, 0), timeSettingsPanel);
        timeSettingsPanel.setFocusable(false);
        timeSettingsPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
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

        Main.forceSize(buttonSize, startButton, timeSettingsButton);

        // Add buttons
        add(Box.createRigidArea(new Dimension(main.getWidth(), main.getHeight()/3)));
        add(startButton);
        add(Box.createRigidArea(new Dimension(main.getWidth(), main.getHeight()/20)));
        add(timeSettingsButton);
        add(timeSettingsPanel);

    }

}
