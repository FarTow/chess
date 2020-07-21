package panels;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TimeSettings extends JPanel implements ActionListener {
    private StartMenu startMenu;

    public TimeSettings(StartMenu startMenu) {
        this.startMenu = startMenu;

        initUI();
    }

    protected void initUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setFocusable(false);
        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

        JTextField timeSettingsOptions = new JTextField();
        timeSettingsOptions.addActionListener(this);

        add(timeSettingsOptions);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    public void actionPerformed(ActionEvent ae) {
        repaint();
    }
}
