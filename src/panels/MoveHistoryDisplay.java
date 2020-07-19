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

public class MoveHistoryDisplay extends JPanel implements ActionListener {
    private final Board board;
    private final Object[] headers = new Object[] {"Turn", "White", "Black"};

    private JTable moveHistoryTable;
    private DefaultTableModel moveHistoryModel;
    private DefaultTableCellRenderer moveHistoryCellRenderer;
    private final ArrayList<Object[]> allMoveData;
    private boolean initialResize;

    private boolean whiteTurn;
    private int moveCount;
    private int pieceCount;

    public MoveHistoryDisplay(Board board) {
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

    public void initUI() {
        // Init MoveDisplay Data
        allMoveData.add(new Object[] {moveCount, "", ""});

        // Init MoveDisplay Model
        moveHistoryModel = new DefaultTableModel(readableMoveData(), headers) {
            public boolean isCellEditable(int row, int column) { return false; }
        };

        // Init MoveDisplay Cell Renderer
        moveHistoryCellRenderer = new DefaultTableCellRenderer();
        moveHistoryCellRenderer.setHorizontalAlignment(SwingConstants.CENTER); // might be possible to conserve piece symbols...

        // Init MoveDisplay Table
        moveHistoryTable = new JTable(moveHistoryModel);

        // Functional Changes

        // Initialize Table Header
        moveHistoryTable.getTableHeader().setReorderingAllowed(false); // table unable to be reordered
        moveHistoryTable.getTableHeader().setResizingAllowed(false); // table unable to resize columns
        moveHistoryTable.getTableHeader().setBackground(Color.black); // set header background

        // Initialize Misc. Properties of Table
        //moveHistoryTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); // resize the table columns
        moveHistoryTable.setRowHeight(20); // set cell size

        centerTable();
    }

    // Visual Update
    public void centerTable() {
        for (int i=0; i<moveHistoryTable.getTableHeader().getColumnModel().getColumnCount(); i++) { // center table headers
            moveHistoryTable.getTableHeader().getColumnModel().getColumn(i).setCellRenderer(moveHistoryCellRenderer);
        }
        for (int i=0; i<moveHistoryTable.getColumnModel().getColumnCount(); i++) { // center rest of table values
            moveHistoryTable.getColumnModel().getColumn(i).setCellRenderer(moveHistoryCellRenderer);
        }
    }
    public void resize() {
        moveHistoryTable.getTableHeader().setFont(Main.MULISH_LIGHT.deriveFont(Math.min((float) getWidth()/15, (float) getHeight()/20))); // set header font
        moveHistoryTable.setFont(Main.MULISH_LIGHT.deriveFont(Math.min((float) getWidth()/25, (float) getHeight()/35)));
        moveHistoryTable.setRowHeight(Math.min(getWidth()/10, getHeight()/18)); // set cell size
    }

    // Data Update
    protected String createNotation(boolean pieceTaken) {
        if (board.getLastPieceMoved() instanceof King) { // castling
            if (board.getCastlingStatus() == 1) return "O-O";
            if (board.getCastlingStatus() == 2) return "O-O-O";
        }

        // Initialize chessNotation string and other used variables
        StringBuilder chessNotation = new StringBuilder();
        int oldRank = (4 + (4 - board.getOldSquareCords().x));
        char oldFile = (char) ((char) 97 + board.getOldSquareCords().y);
        int newRank = (4 + (4 - board.getNewSquareCords().x));
        char newFile = (char) ((char) 97 + board.getNewSquareCords().y);
        chessNotation.append(board.getLastPieceMoved().getNotation()); // symbol of the piece that moved

        // Append "taken" notation if a piece was taken
        if (pieceTaken) {
            if (board.getLastPieceMoved() instanceof Pawn) chessNotation.append(oldFile); // include file name if the piece is a pawn
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

        if (board.getLastPieceMoved() instanceof Pawn) {
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
    protected Object[][] readableMoveData() {
        Object[][] readableMoveData = new Object[allMoveData.size()][allMoveData.get(0).length];

        for (int i=0; i<allMoveData.size(); i++) {
            Object[] moveDataRow = new Object[readableMoveData[0].length];
            System.arraycopy(allMoveData.get(i), 0, moveDataRow, 0, allMoveData.get(i).length);
            readableMoveData[i] = moveDataRow;
        }

        return readableMoveData;
    }
    protected void updateAllMoveData() {
        if (whiteTurn == board.getWhiteTurn()) return; // essentially "if a move took place"

        int newPieceCount = board.getWhitePlayer().getPieces().size() + board.getBlackPlayer().getPieces().size();
        boolean pieceTaken = pieceCount == newPieceCount+1;

        if (moveCount == 1) {
            if (board.getWhiteTurn()) { // if black was the one to move (as it's white's turn now)
                allMoveData.get(moveCount-1)[2] = createNotation(pieceTaken); // update black's move
                moveCount++; // go to next move
            } else { // if white was the one to move
                allMoveData.get(moveCount-1)[1] = createNotation(pieceTaken); // update white's move
            }
        } else {
            if (board.getWhiteTurn()) { // if black was the one to move (as it's white's turn now)
                allMoveData.get(moveCount-1)[2] = createNotation(pieceTaken); // update black's move
                moveCount++; // go to next move
            } else { // if white was the one to move
                allMoveData.add(new Object[]{moveCount, "", ""}); // new row of data

                allMoveData.get(moveCount-1)[1] = createNotation(pieceTaken); // update white's move
            }
        }

        pieceCount = newPieceCount;
        moveHistoryModel.setDataVector(readableMoveData(), headers); // reset data vector to accommodate for new data
        centerTable();
        whiteTurn = board.getWhiteTurn();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
    public void actionPerformed(ActionEvent ae) {
        if (!initialResize) { // please god tell me there's a way
            resize();
            initialResize = true;
        }

        updateAllMoveData();
        repaint();
    }

}