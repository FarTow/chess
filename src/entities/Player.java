package entities;

import panels.Board;

import java.util.ArrayList;

public class Player {
    private final ArrayList<Piece> pieces;

    public Player(Board board) {
        pieces = new ArrayList<>();
    }
}
