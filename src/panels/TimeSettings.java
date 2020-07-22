package panels;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TimeSettings extends JPanel implements ActionListener {
    private StartMenu startMenu;

    private JLabel minuteLabel, secondLabel, incrementLabel;
    private JTextField minuteInput, secondInput, incrementInput;

    private final Dimension inputSize, labelSize;

    public TimeSettings(StartMenu startMenu) {
        this.startMenu = startMenu;

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

        minuteLabel.setFont(Main.MULISH_LIGHT.deriveFont(fontHeight));
        secondLabel.setFont(Main.MULISH_LIGHT.deriveFont(fontHeight));
        incrementLabel.setFont(Main.MULISH_LIGHT.deriveFont(fontHeight));
    }

    public void resize() {
        setSizes();
        setFonts();

        Main.forceSize(labelSize, minuteLabel, secondLabel, incrementLabel);
        Main.forceSize(inputSize, minuteInput, secondInput, incrementInput);

        updateUI();
    }

    protected void initUI() {
        // Initial settings
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        setFocusable(false);
        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

        // Create text fields
        minuteInput = new JTextField();
        secondInput = new JTextField();
        incrementInput = new JTextField();

        // Set alignment
        minuteInput.setAlignmentX(Component.RIGHT_ALIGNMENT);
        secondInput.setAlignmentX(Component.RIGHT_ALIGNMENT);
        incrementInput.setAlignmentX(Component.RIGHT_ALIGNMENT);

        // Add keyListener to inputs to only accept integers

        /*
        minuteInput.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                super.keyPressed(ke);

                minuteInput.setEditable(Character.isDigit(ke.getKeyChar()));
            }
        });
        secondInput.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                super.keyPressed(ke);

                secondInput.setEditable(Character.isDigit(ke.getKeyChar()));
            }
        });
        incrementInput.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                super.keyPressed(ke);

                incrementInput.setEditable(Character.isDigit(ke.getKeyChar()));
            }
        });
         */

        // Add this as the actionListener
        minuteInput.addActionListener(this);
        secondInput.addActionListener(this);
        incrementInput.addActionListener(this);

        // Create labels
        minuteLabel = new JLabel("Starting Minutes: ");
        secondLabel = new JLabel("Starting Seconds: ");
        incrementLabel = new JLabel("Increment: ");

        minuteLabel.setFont(Main.MULISH_LIGHT.deriveFont((float) labelSize.height));
        secondLabel.setFont(Main.MULISH_LIGHT.deriveFont((float) labelSize.height));
        incrementLabel.setFont(Main.MULISH_LIGHT.deriveFont((float) labelSize.height));

        // Add all components to panel
        Main.setGridBagLayoutConstraints(
                c, new Insets(1, 1, 1, 1), GridBagConstraints.HORIZONTAL,
                0, 0, 1, 1, 0.5, 0.5, GridBagConstraints.CENTER
        );
        add(minuteLabel);

        Main.setGridBagLayoutConstraints(
                c, new Insets(1, 1, 1, 1), GridBagConstraints.HORIZONTAL,
                1, 0, 1, 1, 0.5, 0.5, GridBagConstraints.CENTER
        );
        add(minuteInput, c);

        Main.setGridBagLayoutConstraints(
                c, new Insets(1, 1, 1, 1), GridBagConstraints.HORIZONTAL,
                0, 1, 1, 1, 0.5, 0.5, GridBagConstraints.CENTER
        );
        add(secondLabel, c);

        Main.setGridBagLayoutConstraints(
                c, new Insets(1, 1, 1, 1), GridBagConstraints.HORIZONTAL,
                1, 1, 1, 1, 0.5, 0.5, GridBagConstraints.CENTER
        );
        add(secondInput, c);

        Main.setGridBagLayoutConstraints(
                c, new Insets(1, 1, 1, 1), GridBagConstraints.HORIZONTAL,
                0, 2, 1, 1, 0.5, 0.5, GridBagConstraints.CENTER
        );
        add(incrementLabel, c);

        Main.setGridBagLayoutConstraints(
                c, new Insets(1, 1, 1, 1), GridBagConstraints.HORIZONTAL,
                1, 2, 1, 1, 0.5, 0.5, GridBagConstraints.CENTER
        );
        add(incrementInput, c);

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

        //startMenu.setStartMinutes(Integer.parseInt(minuteInput.getText()));
        //startMenu.setStartSeconds(Integer.parseInt(secondInput.getText()));
        //startMenu.setIncrement(Integer.parseInt(incrementInput.getText()));

        repaint();
    }

}
