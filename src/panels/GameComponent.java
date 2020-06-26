package panels;

import javax.swing.JPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class GameComponent extends JPanel implements ActionListener {
    protected final Point topLeft;

    // HEAVILY REFACTOR THIS
    public GameComponent(Point initialTopLeft) { // respective to own JPanel
        setBackground(new Color(194, 194, 194));
        topLeft = initialTopLeft;
    }

    public abstract void resize(int ... properties);

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
    public void actionPerformed(ActionEvent ae) {
        repaint();
    }

}
