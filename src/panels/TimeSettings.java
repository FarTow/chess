package panels;

import entities.Player;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TimeSettings extends JPanel implements ActionListener {
    private final StartMenu startMenu;

    private final JLabel[] inputLabels;
    private final JTextField[] inputFields;
    private JButton submitInputButton;

    private final Dimension inputFieldSize, inputLabelSize, submitInputButtonSize;
    private float fontHeight;

    public TimeSettings(StartMenu startMenu) {
        this.startMenu = startMenu;
        inputLabels = new JLabel[3];
        inputFields = new JTextField[3];

        inputLabelSize = new Dimension(0, 0);
        inputFieldSize = new Dimension(0, 0);
        submitInputButtonSize = new Dimension(0, 0);

        initUI();
    }

    protected void setSizes() {
        inputLabelSize.width = getWidth()*4/5;
        inputLabelSize.height = getHeight()/4;

        inputFieldSize.width = getWidth()/5;
        inputFieldSize.height = getHeight()/4;

        submitInputButtonSize.width = getWidth()/2;
        submitInputButtonSize.height = getHeight()/4;
    }
    protected void setFonts() { // can probably be replaced with for loop
        fontHeight = Math.min(inputLabelSize.height*2/3f, inputLabelSize.width/12f);

        for (JLabel label : inputLabels) {
            label.setFont(Main.MULISH_LIGHT.deriveFont(fontHeight));
        }

        for (JTextField textField : inputFields) {
            textField.setFont(Main.MULISH_LIGHT.deriveFont(fontHeight));
        }

        submitInputButton.setFont(Main.MULISH_LIGHT.deriveFont(fontHeight));
    }
    protected void updateLabels() {
        for (int i=0; i<inputLabels.length; i++) {
            int currentTimeProperty = startMenu.getTimeProperty(i);
            StringBuilder label = new StringBuilder();

            switch (i) {
                case Player.MINUTES_INDEX:
                    label.append("Starting Minutes");
                    break;
                case Player.SECONDS_INDEX:
                    label.append("Starting Seconds");
                    break;
                case Player.INCREMENT_INDEX:
                    label.append("Increment");
                    break;
            }
            label.append(": ");

            inputLabels[i].setText(label.toString() + (currentTimeProperty != -1 ? currentTimeProperty : "-"));
        }
    }

    public void resize() {
        setSizes();
        setFonts();

        for (JLabel inputLabel : inputLabels) {
            Main.forceSize(inputLabelSize, inputLabel);
        }
        for (JTextField inputField : inputFields) {
            Main.forceSize(inputFieldSize, inputField);
        }
        Main.forceSize(submitInputButtonSize, submitInputButton);

        updateUI();
    }

    protected void boundKeyInputs(JTextField textField) {
        textField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                super.keyPressed(ke);

                textField.setEditable(Character.isDigit(ke.getKeyChar()) || ke.getKeyChar() == KeyEvent.VK_BACK_SPACE);
            }
        });
    }

    protected void initUI() {
        // Initial settings
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        setFocusable(false);
        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

        // Create and initialize text fields
        for (int i=0; i<inputFields.length; i++) {
            inputFields[i] = new JTextField();
            inputFields[i].setAlignmentX(Component.RIGHT_ALIGNMENT);
            boundKeyInputs(inputFields[i]);
            inputFields[i].addActionListener(this);
        }

        // Create and initialize labels
        for (int i=0; i<inputLabels.length; i++) {
            switch(i) {
                case 0:
                    inputLabels[i] = new JLabel("Starting Minutes:");
                    break;
                case 1:
                    inputLabels[i] = new JLabel("Starting Seconds:");
                    break;
                case 2:
                    inputLabels[i] = new JLabel("Increment:");
                    break;
            }

            inputLabels[i].setFont(Main.MULISH_LIGHT.deriveFont(fontHeight));
        }

        // Create submit button
        submitInputButton = new JButton("Set Time");
        submitInputButton.addActionListener(ae -> {
            for (int i=0; i<inputFields.length; i++) {
                try {
                    startMenu.setTimeProperty(i, Integer.parseInt(inputFields[i].getText()));
                } catch(NumberFormatException e) {
                    if (inputFields[i].getText().equals("")) startMenu.setTimeProperty(i, -1); // force empty fields to be a sort of temporary "null"
                }
            }
        });
        submitInputButton.setFocusable(false);

        // Add all components to panel
        Main.setGridBagLayoutConstraints(
                c, new Insets(1, 1, 1, 1), GridBagConstraints.HORIZONTAL,
                0, 0, 1, 1, 0.5, 0.5, GridBagConstraints.CENTER
        );
        add(inputLabels[0], c);

        Main.setGridBagLayoutConstraints(
                c, new Insets(1, 1, 1, 1), GridBagConstraints.HORIZONTAL,
                2, 0, 1, 1, 0.5, 0.5, GridBagConstraints.CENTER
        );
        add(inputFields[0], c);

        Main.setGridBagLayoutConstraints(
                c, new Insets(1, 1, 1, 1), GridBagConstraints.HORIZONTAL,
                0, 1, 1, 1, 0.5, 0.5, GridBagConstraints.CENTER
        );
        add(inputLabels[1], c);

        Main.setGridBagLayoutConstraints(
                c, new Insets(1, 1, 1, 1), GridBagConstraints.HORIZONTAL,
                2, 1, 1, 1, 0.5, 0.5, GridBagConstraints.CENTER
        );
        add(inputFields[1], c);

        Main.setGridBagLayoutConstraints(
                c, new Insets(1, 1, 1, 1), GridBagConstraints.HORIZONTAL,
                0, 2, 1, 1, 0.5, 0.5, GridBagConstraints.CENTER
        );
        add(inputLabels[2], c);

        Main.setGridBagLayoutConstraints(
                c, new Insets(1, 1, 1, 1), GridBagConstraints.HORIZONTAL,
                2, 2, 1, 1, 0.5, 0.5, GridBagConstraints.CENTER
        );
        add(inputFields[2], c);

        Main.setGridBagLayoutConstraints(
                c, new Insets(5, 0, 1, 0), GridBagConstraints.BOTH,
                0, 3, 1, 1, 0.5, 0.5, GridBagConstraints.CENTER
        );
        add(submitInputButton, c);

        // Start timer after initializing UI
        Timer timer = new Timer(1000/Game.FRAME_RATE, this);
        timer.start();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
    public void actionPerformed(ActionEvent ae) {
        resize(); // make efficient
        setFonts();

        updateLabels();
        repaint();
    }

}
