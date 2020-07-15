package panels;

import entities.King;
import entities.Pawn;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;

public class MoveHistory extends JPanel implements ActionListener {
    private final Board board;
    private final Object[] headers = new Object[] {"Turn", "White", "Black"};

    private final JTable moveDisplayTable;
    private final DefaultTableModel moveDisplayModel;
    private final DefaultTableCellRenderer moveDisplayCellRenderer;
    private final ArrayList<Object[]> allMoveData;

    private boolean whiteTurn;
    private int moveCount;
    private int pieceCount;

    public MoveHistory(Dimension initialSize, Board board) {
        setBackground(new Color(194, 194, 194));
        setLayout(new BorderLayout(0, 0));

        this.board = board;
        whiteTurn = true;
        moveCount = 1;
        pieceCount = 32;
        allMoveData = new ArrayList<>();

        allMoveData.add(new Object[] {moveCount, "", ""});

        moveDisplayModel = new DefaultTableModel(readableMoveData(), headers) {
            public boolean isCellEditable(int row, int column) { return false; }
        };

        moveDisplayCellRenderer = new DefaultTableCellRenderer();
        moveDisplayCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        moveDisplayTable = new JTable(moveDisplayModel);
        moveDisplayTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); // functional changes
        moveDisplayTable.getTableHeader().setReorderingAllowed(false);
        moveDisplayTable.getTableHeader().setResizingAllowed(false);

        moveDisplayTable.setDefaultRenderer(String.class, moveDisplayCellRenderer); // visual changes
        moveDisplayTable.getTableHeader().setBackground(Color.black);
        moveDisplayTable.getTableHeader().setFont(new Font("Serif", Font.PLAIN, 16));
        moveDisplayTable.setFont(new Font("Serif", Font.PLAIN, 12));
        moveDisplayTable.setRowHeight(25);
        add(new JScrollPane(moveDisplayTable));

        Main.forceSize(initialSize, this);
    }

    public Object[][] readableMoveData() {
        Object[][] readableMoveData = new Object[allMoveData.size()][allMoveData.get(0).length];

        for (int i=0; i<allMoveData.size(); i++) {
            Object[] moveDataRow = new Object[readableMoveData[0].length];
            System.arraycopy(allMoveData.get(i), 0, moveDataRow, 0, allMoveData.get(i).length);
            readableMoveData[i] = moveDataRow;
        }

        return readableMoveData;
    }
    public String lastMove(boolean pieceTaken) {
        if (board.getLastPiece() instanceof King) { // castling
            if (board.getCastleState() == 1) return "O-O";
            if (board.getCastleState() == 2) return "O-O-O";
        }

        // Initialize chessNotation string and other used variables
        StringBuilder chessNotation = new StringBuilder();
        int oldRank = (4 + (4 - board.getOldSquareCords().x));
        char oldFile = (char) ((char) 97 + board.getOldSquareCords().y);
        int newRank = (4 + (4 - board.getNewSquareCords().x));
        char newFile = (char) ((char) 97 + board.getNewSquareCords().y);
        chessNotation.append(board.getLastPiece().getSymbol()); // symbol of the piece that moved

        // Append "taken" notation if a piece was taken
        if (pieceTaken) {
            if (board.getLastPiece() instanceof Pawn) chessNotation.append(oldFile); // include file name if the piece is a pawn
            chessNotation.append('×'); // captured symbol
        }

        // Check for ambiguity and adjust string accordingly
        if (board.isMoveAmbiguous()) {
            if (board.isColumnAmbiguous()) {
                chessNotation.append(oldRank);
            } else {
                chessNotation.append(oldFile);
            }
        }

        // Notate new file and rank
        chessNotation.append(newFile); // file
        chessNotation.append(newRank); // rank

        if (board.getLastPiece() instanceof Pawn) {
            if (board.getPawnPromotionStatus() != ' ') {
                chessNotation.append('=');
                chessNotation.append(board.getPawnPromotionStatus());
            }
        }

        // Check for win conditions
        if (board.isCheck()) {
            chessNotation.append('+');
        } else if (board.isCheckmate()) {
            chessNotation.append('#');
        } else if (board.isStalemate()) {
            chessNotation.append('$');
        }

        return chessNotation.toString();
    }
    public void updateAllMoveData() {
        if (whiteTurn != board.getWhiteTurn()) { // essentially "if a move took place"
            int newPieceCount = board.getWhitePlayer().getPieces().size() + board.getBlackPlayer().getPieces().size();
            boolean pieceTaken = pieceCount == newPieceCount+1;

            if (board.getWhiteTurn()) { // if black was the one to move (as it's white's turn now)
                allMoveData.get(moveCount-1)[2] = lastMove(pieceTaken); // update black's move

                moveCount++; // go to next move
                allMoveData.add(new Object[] {moveCount, "", ""}); // new row of data
            } else { // if white was the one to move
                allMoveData.get(moveCount-1)[1] = lastMove(pieceTaken); // update white's move
            }

            pieceCount = newPieceCount;
            moveDisplayModel.setDataVector(readableMoveData(), headers); // reset data vector to accommodate for new data
            whiteTurn = board.getWhiteTurn();
        }
    }

    public void actionPerformed(ActionEvent ae) {
        updateAllMoveData();
        repaint();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

}
