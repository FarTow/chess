package panels;

import entities.Piece;
import entities.Player;

import java.awt.*;
import java.awt.event.ActionEvent;

import java.util.ArrayList;

public class TakenPieces extends GameComponent {
    private final Board board;
    private final Player player;

    private boolean whiteTurn;

    public TakenPieces(Point initialTopLeft, Player player, Board board) {
        super(initialTopLeft);

        this.board = board;
        this.player = player;
        whiteTurn = true;
    }

    public void resize(int ... properties) {
        int newImageSize = properties[0];

        for (Piece piece : player.getDeadPieces()) {
            piece.scaleImage(newImageSize);
        }
    }

    public void actionPerformed(ActionEvent ae) {
        repaint();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (player.getDeadPieces().size() == 0) return;

        for (int i=0; i<player.getDeadPieces().size(); i++) {
            g.drawImage(player.getDeadPieces().get(i).getImage(),
                    i*player.getDeadPieces().get(i).getImage().getWidth(null),
                    player.isWhite() ? 0 : getHeight()-player.getDeadPieces().get(i).getImage().getHeight(null), null);
        }
    }
}
