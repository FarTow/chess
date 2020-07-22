package panels;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TimeSettings extends JPanel implements ActionListener {
    private StartMenu startMenu;

    private JTextField minuteInput, secondInput, incrementInput;
    private final Dimension inputSize;

    public TimeSettings(StartMenu startMenu) {
        this.startMenu = startMenu;

        inputSize = new Dimension(0, 0);

        initUI();
    }

    public void resize() {
        inputSize.width = getWidth()/5;
        inputSize.height = getHeight()/4;
        Main.forceSize(inputSize, minuteInput, secondInput, incrementInput);

        updateUI();
    }

    protected void initUI() {
        // Initial settings
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
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

        // Add this as the actionListener
        minuteInput.addActionListener(this);
        secondInput.addActionListener(this);
        incrementInput.addActionListener(this);

        // Add text fields to panel
        add(minuteInput);
        add(secondInput);
        add(incrementInput);

        // Start timer after initializing UI
        Timer timer = new Timer(1000/Game.FRAME_RATE, this);
        timer.start();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    public void actionPerformed(ActionEvent ae) {
        resize();
        repaint();
    }
}
