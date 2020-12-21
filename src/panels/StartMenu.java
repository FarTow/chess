package panels;

import entities.Player;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class StartMenu extends JPanel { // Weird lag?
    private final Dimension buttonSize, timeSettingsPanelMaxSize;
    private float buttonFontHeight;

    private JButton startButton;
    private JToggleButton timeSettingsButton;
    private final TimeSettings timeSettings;

    private final int[] timeProperties;
    private int resizeCount; // absolutely hate this.

    public StartMenu() {
        timeProperties = new int[] {-1, -1, -1};

        buttonSize = new Dimension(0, 0);
        timeSettingsPanelMaxSize = new Dimension(0, 0);

        timeSettings = new TimeSettings(this);

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) { // review time
                super.componentResized(e);

                if (getComponentCount() == 0) return;

                resize();
                updateUI();

                resizeCount++;
            }
        });
    }

    protected void setSizes() {
        buttonSize.width = getWidth()/5;
        buttonSize.height = getHeight()/20;

        timeSettingsPanelMaxSize.width = getWidth()/5;
        timeSettingsPanelMaxSize.height = getHeight()/6;

        buttonFontHeight = Math.min((float) getHeight() / 30, (float) getWidth() / 55);
    }

    protected void resize() {
        setSizes();

        startButton.setFont(Main.MULISH_LIGHT.deriveFont(buttonFontHeight));
        timeSettingsButton.setFont(Main.MULISH_LIGHT.deriveFont(buttonFontHeight));

        Main.forceSize(buttonSize, startButton, timeSettingsButton);
        if (resizeCount >= 2 && timeSettingsButton.isSelected()) {
            Main.forceSize(timeSettingsPanelMaxSize, timeSettings);
        }
    }

    public void checkTimeProperties() {
        boolean minutesNull = timeProperties[Player.MINUTES_INDEX] == -1;
        boolean secondsNull = timeProperties[Player.SECONDS_INDEX] == -1;
        boolean incrementNull = timeProperties[Player.INCREMENT_INDEX] == -1;

        if (!minutesNull && !secondsNull && !incrementNull) return;
        if (minutesNull && secondsNull && incrementNull) return;

        if (incrementNull) { // increment is null and either minutes, seconds or both aren't null
            timeProperties[Player.INCREMENT_INDEX] = 0;

            if (minutesNull) timeProperties[Player.MINUTES_INDEX] = 0;
            if (secondsNull) timeProperties[Player.SECONDS_INDEX] = 0;
        } else { // increment isn't null and either minutes, seconds, or both are null
            if (minutesNull && secondsNull) {
                timeProperties[Player.INCREMENT_INDEX] = -1;
            } else if (minutesNull) {
                timeProperties[Player.MINUTES_INDEX] = 0;
            } else {
                timeProperties[Player.SECONDS_INDEX] = 0;
            }
        }
    }

    protected void initUI(Main main) {
        setBackground(Main.BACKGROUND_COLOR);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Create start button
        startButton = new JButton("Start");
        startButton.addActionListener(ae -> {
            ((CardLayout) main.getContentPane().getLayout()).show(main.getContentPane(), Main.GAME_LABEL);
            main.getGame().start(timeProperties);
        });

        startButton.setFocusable(false);
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Initialize time setting panel
        Main.forceSize(new Dimension(main.getWidth()/5, 0), timeSettings);
        timeSettings.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create time setting button
        timeSettingsButton = new JToggleButton("Time Settings");
        timeSettingsButton.addItemListener(itemEvent -> { // HARD CARRIED BY TANVIR LIKE FOR REAL THIS TIME
            if (itemEvent.getStateChange() == ItemEvent.SELECTED) { // increase size of panel
                new Thread(() -> {
                    while (timeSettings.getHeight() < timeSettingsPanelMaxSize.height) {
                        Main.forceSize(
                                new Dimension(
                                        timeSettingsPanelMaxSize.width,
                                        timeSettings.getHeight() + timeSettings.getHeight()/10 + 1),
                                timeSettings
                        );
                        timeSettings.updateUI();

                        try {
                            Thread.sleep(5);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            } else if (itemEvent.getStateChange() == ItemEvent.DESELECTED) { // decrease size of panel
                new Thread(() -> {
                    while (timeSettings.getHeight() > 0) {
                        Main.forceSize(new Dimension(timeSettingsPanelMaxSize.width, timeSettings.getHeight()*9/10), timeSettings);
                        timeSettings.updateUI();

                        try {
                            Thread.sleep(5);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        timeSettingsButton.setFocusable(false);
        timeSettingsButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add components
        add(Box.createRigidArea(new Dimension(main.getWidth(), main.getHeight()/3)));
        add(startButton);
        add(Box.createRigidArea(new Dimension(main.getWidth(), main.getHeight()/20)));
        add(timeSettingsButton);
        add(timeSettings);
    }

    public void setTimeProperty(int index, int value) { timeProperties[index] = value; }

    public int getTimeProperty(int index) { return timeProperties[index]; }
}
