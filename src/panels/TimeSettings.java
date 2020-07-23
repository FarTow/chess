package panels;

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

    private final Dimension inputSize, labelSize;

    public TimeSettings(StartMenu startMenu) {
        this.startMenu = startMenu;
        inputLabels = new JLabel[3];
        inputFields = new JTextField[3];

        labelSize = new Dimension(0, 0);
        inputSize = new Dimension(0, 0);

        initUI();
    }

    protected void setSizes() {
        labelSize.width = getWidth()*4/5;
        labelSize.height = getHeight()/4;

        inputSize.width = getWidth()/5;
        inputSize.height = getHeight()/4;
    }
    protected void setFonts() { // can probably be replaced with for loop
        float fontHeight = labelSize.height*2/3f;

        for (JLabel label : inputLabels) {
            label.setFont(Main.MULISH_LIGHT.deriveFont(fontHeight));
        }
    }

    public void resize() {
        setSizes();
        setFonts();

        for (JLabel label : inputLabels) {
            Main.forceSize(labelSize, label);
        }

        for (JTextField textField : inputFields) {
            Main.forceSize(inputSize, textField);
        }

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
                    inputLabels[i] = new JLabel("Starting Minutes: ");
                    break;
                case 1:
                    inputLabels[i] = new JLabel("Starting Seconds: ");
                    break;
                case 2:
                    inputLabels[i] = new JLabel("Increments: ");
                    break;
            }

            inputLabels[i].setFont(Main.MULISH_LIGHT.deriveFont((float) labelSize.height));
        }

        // Add all components to panel
        Main.setGridBagLayoutConstraints(
                c, new Insets(1, 1, 1, 1), GridBagConstraints.HORIZONTAL,
                0, 0, 1, 1, 0.5, 0.5, GridBagConstraints.CENTER
        );
        add(inputLabels[0], c);

        Main.setGridBagLayoutConstraints(
                c, new Insets(1, 1, 1, 1), GridBagConstraints.HORIZONTAL,
                1, 0, 1, 1, 0.5, 0.5, GridBagConstraints.CENTER
        );
        add(inputFields[0], c);

        Main.setGridBagLayoutConstraints(
                c, new Insets(1, 1, 1, 1), GridBagConstraints.HORIZONTAL,
                0, 1, 1, 1, 0.5, 0.5, GridBagConstraints.CENTER
        );
        add(inputLabels[1], c);

        Main.setGridBagLayoutConstraints(
                c, new Insets(1, 1, 1, 1), GridBagConstraints.HORIZONTAL,
                1, 1, 1, 1, 0.5, 0.5, GridBagConstraints.CENTER
        );
        add(inputFields[1], c);

        Main.setGridBagLayoutConstraints(
                c, new Insets(1, 1, 1, 1), GridBagConstraints.HORIZONTAL,
                0, 2, 1, 1, 0.5, 0.5, GridBagConstraints.CENTER
        );
        add(inputLabels[2], c);

        Main.setGridBagLayoutConstraints(
                c, new Insets(1, 1, 1, 1), GridBagConstraints.HORIZONTAL,
                1, 2, 1, 1, 0.5, 0.5, GridBagConstraints.CENTER
        );
        add(inputFields[2], c);

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

        for (int i=0; i<inputFields.length; i++) {
            try {
                startMenu.setTimeProperty(i, Integer.parseInt(inputFields[i].getText()));
            } catch(NumberFormatException e) {
                if (inputFields[i].getText().equals("")) startMenu.setTimeProperty(i, 0);
            }
        }

        repaint();
    }

}
