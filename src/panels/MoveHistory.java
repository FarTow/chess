package panels;

import entities.Board;

import javax.swing.JPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Graphics;
import java.awt.Color;

public class MoveHistory extends JPanel implements ActionListener {
    Board board;

    public MoveHistory(Board board) {
        setBackground(new Color(194, 194, 194));

        this.board = board;
    }

    public void drawUI(Graphics g) {

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.blue);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
    public void actionPerformed(ActionEvent ae) {
        repaint();
    }
}
