package panels;

import javax.swing.JPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class GameComponent extends JPanel implements ActionListener {
    protected final Point topLeft;

    public GameComponent(Point initialTopLeft) {
        setBackground(new Color(194, 194, 194));
        topLeft = initialTopLeft;
    }

    public void resize() { }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
    public void actionPerformed(ActionEvent ae) {
        repaint();
    }

}
