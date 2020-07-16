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

    private JTable moveHistoryTable;
    private DefaultTableModel moveHistoryModel;
    private DefaultTableCellRenderer moveHistoryCellRenderer;
    private final ArrayList<Object[]> allMoveData;

    private boolean whiteTurn;
    private int moveCount;
    private int pieceCount;

    public MoveHistory(Board board) {
        setOpaque(false);
        setLayout(new BorderLayout(0, 0));

        this.board = board;
        whiteTurn = true;
        moveCount = 1;
        pieceCount = 32;
        allMoveData = new ArrayList<>();

        initUI();
        add(new JScrollPane(moveHistoryTable));
    }

    public void initUI() { // CLEAN THIS UP
        // Init MoveDisplay Data
        allMoveData.add(new Object[] {moveCount, "", ""});

        // Init MoveDisplay Model
        moveHistoryModel = new DefaultTableModel(readableMoveData(), headers) {
            public boolean isCellEditable(int row, int column) { return false; }
        };

        // Init MoveDisplay Cell Renderer
        moveHistoryCellRenderer = new DefaultTableCellRenderer();
        moveHistoryCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        // Init MoveDisplay Table
        moveHistoryTable = new JTable(moveHistoryModel);

            // Functional Changes
        moveHistoryTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); // resize the table columns
        moveHistoryTable.getTableHeader().setReorderingAllowed(false); // table unable to be reordered
        moveHistoryTable.getTableHeader().setResizingAllowed(false); // table unable to resize columns
        for (int i=0; i<moveHistoryTable.getTableHeader().getColumnModel().getColumnCount(); i++) { // center table headers
            moveHistoryTable.getTableHeader().getColumnModel().getColumn(i).setCellRenderer(moveHistoryCellRenderer);
        }
        for (int i=0; i<moveHistoryTable.getColumnModel().getColumnCount(); i++) { // center rest of table values
            moveHistoryTable.getColumnModel().getColumn(i).setCellRenderer(moveHistoryCellRenderer);
        }
        moveHistoryTable.getTableHeader().setBackground(Color.black); // set header background
        moveHistoryTable.getTableHeader().setFont(Main.MULISH_LIGHT.deriveFont(20.0f)); // set header font
        moveHistoryTable.setFont(Main.MULISH_LIGHT.deriveFont(10.0f));
        moveHistoryTable.setRowHeight(20); // set cell size
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
            if (board.getCastlingStatus() == 1) return "O-O";
            if (board.getCastlingStatus() == 2) return "O-O-O";
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
        if (board.getCurrentPlayer().isInCheck()) {
            chessNotation.append('+');
        } else if (board.getCurrentPlayer().isInCheckmate()) {
            chessNotation.append('#');
        } else if (board.getCurrentPlayer().isInStalemate()) {
            chessNotation.append('$');
        }

        return chessNotation.toString();
    }
    public void updateAllMoveData() {
        if (whiteTurn != board.getWhiteTurn()) { // essentially "if a move took place"
            int newPieceCount = board.getWhitePlayer().getPieces().size() + board.getBlackPlayer().getPieces().size();
            boolean pieceTaken = pieceCount == newPieceCount+1;

            if (moveCount == 1) {
                if (board.getWhiteTurn()) { // if black was the one to move (as it's white's turn now)
                    allMoveData.get(moveCount-1)[2] = lastMove(pieceTaken); // update black's move
                    moveCount++; // go to next move
                } else { // if white was the one to move
                    allMoveData.get(moveCount-1)[1] = lastMove(pieceTaken); // update white's move
                }
            } else {
                if (board.getWhiteTurn()) { // if black was the one to move (as it's white's turn now)
                    allMoveData.get(moveCount-1)[2] = lastMove(pieceTaken); // update black's move
                    moveCount++; // go to next move
                } else { // if white was the one to move
                    allMoveData.add(new Object[]{moveCount, "", ""}); // new row of data

                    allMoveData.get(moveCount-1)[1] = lastMove(pieceTaken); // update white's move
                }
            }

            pieceCount = newPieceCount;
            moveHistoryModel.setDataVector(readableMoveData(), headers); // reset data vector to accommodate for new data
            for (int i=0; i<moveHistoryTable.getTableHeader().getColumnModel().getColumnCount(); i++) {
                moveHistoryTable.getTableHeader().getColumnModel().getColumn(i).setCellRenderer(moveHistoryCellRenderer);
            }
            for (int i=0; i<moveHistoryTable.getColumnModel().getColumnCount(); i++) {
                moveHistoryTable.getColumnModel().getColumn(i).setCellRenderer(moveHistoryCellRenderer);
            }
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
