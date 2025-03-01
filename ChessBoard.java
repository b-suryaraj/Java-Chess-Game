import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ChessBoard extends JFrame {
    private final JButton[][] squares = new JButton[8][8];
    private Piece[][] board = new Piece[8][8];
    private Piece selectedPiece = null;
    private int selectedRow = -1, selectedCol = -1;
    private boolean isWhiteTurn = true; // White moves first

    private final Stack<GameState> undoStack = new Stack<>();
    private final Stack<GameState> redoStack = new Stack<>();

    public ChessBoard() {
        setTitle("Chess Game");
        setSize(600, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel boardPanel = new JPanel(new GridLayout(8, 8));
        JPanel buttonPanel = new JPanel();

        JButton undoButton = new JButton("Undo");
        JButton redoButton = new JButton("Redo");

        undoButton.addActionListener(e -> undoMove());
        redoButton.addActionListener(e -> redoMove());

        buttonPanel.add(undoButton);
        buttonPanel.add(redoButton);

        initializeBoard(boardPanel);
        add(boardPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void initializeBoard(JPanel boardPanel) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col] = new JButton();
                squares[row][col].setFont(new Font("Arial", Font.BOLD, 20));
                squares[row][col].setFocusPainted(false);
                squares[row][col].setOpaque(true);
                squares[row][col].setBackground((row + col) % 2 == 0 ? Color.WHITE : Color.GRAY);

                final int r = row, c = col;
                squares[row][col].addActionListener(e -> handleClick(r, c));

                boardPanel.add(squares[row][col]);
            }
        }
        setupPieces();
    }

    private void setupPieces() {
        board = new Piece[8][8];

        board[0][0] = board[0][7] = new Rook(false);
        board[7][0] = board[7][7] = new Rook(true);
        board[0][1] = board[0][6] = new Knight(false);
        board[7][1] = board[7][6] = new Knight(true);
        board[0][2] = board[0][5] = new Bishop(false);
        board[7][2] = board[7][5] = new Bishop(true);
        board[0][3] = new Queen(false);
        board[7][3] = new Queen(true);
        board[0][4] = new King(false);
        board[7][4] = new King(true);

        for (int i = 0; i < 8; i++) {
            board[1][i] = new Pawn(false);
            board[6][i] = new Pawn(true);
        }

        updateBoard();
    }

    private void handleClick(int row, int col) {
        if (selectedPiece == null) {
            if (board[row][col] != null && board[row][col].isWhite() == isWhiteTurn) {
                selectedPiece = board[row][col];
                selectedRow = row;
                selectedCol = col;
                highlightLegalMoves(selectedRow, selectedCol);
            }
        } else {
            if (selectedPiece.isValidMove(selectedRow, selectedCol, row, col, board)) {
                saveState(); // Save state before making a move

                if (board[row][col] instanceof King) {
                    JOptionPane.showMessageDialog(this, (isWhiteTurn ? "White" : "Black") + " Wins!");
                    restartGame();
                    return;
                }

                board[row][col] = selectedPiece;
                board[selectedRow][selectedCol] = null;
                isWhiteTurn = !isWhiteTurn;

                if (isKingInCheck(isWhiteTurn)) {
                    JOptionPane.showMessageDialog(this, (isWhiteTurn ? "White" : "Black") + " is in Check!");
                }

                if (isCheckmate(isWhiteTurn)) {
                    JOptionPane.showMessageDialog(this, (isWhiteTurn ? "Black" : "White") + " Wins!");
                    restartGame();
                }

                selectedPiece = null;
                updateBoard();
            } else {
                selectedPiece = null;
                updateBoard();
            }
        }
    }

    private void highlightLegalMoves(int row, int col) {
        updateBoard(); // Reset board to default colors

        List<int[]> legalMoves = new ArrayList<>();

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (board[row][col].isValidMove(row, col, r, c, board)) {
                    legalMoves.add(new int[]{r, c});
                }
            }
        }

        for (int[] move : legalMoves) {
            int r = move[0], c = move[1];
            if (board[r][c] != null) {
                squares[r][c].setBackground(Color.RED); // Capture moves in red
            } else {
                squares[r][c].setBackground(Color.GREEN); // Normal moves in green
            }
        }
    }

    private void saveState() {
        Piece[][] boardCopy = new Piece[8][8];
        for (int i = 0; i < 8; i++) {
            System.arraycopy(board[i], 0, boardCopy[i], 0, 8);
        }
        undoStack.push(new GameState(boardCopy, isWhiteTurn));
        redoStack.clear(); // Clear redo stack on new move
    }

    private void undoMove() {
        if (!undoStack.isEmpty()) {
            redoStack.push(new GameState(board, isWhiteTurn));
            GameState prevState = undoStack.pop();
            board = prevState.getBoard();
            isWhiteTurn = prevState.isWhiteTurn();
            updateBoard();
        }
    }

    private void redoMove() {
        if (!redoStack.isEmpty()) {
            undoStack.push(new GameState(board, isWhiteTurn));
            GameState nextState = redoStack.pop();
            board = nextState.getBoard();
            isWhiteTurn = nextState.isWhiteTurn();
            updateBoard();
        }
    }

    private boolean isKingInCheck(boolean whiteTurn) {
        int kingRow = -1, kingCol = -1;

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (board[r][c] instanceof King && board[r][c].isWhite() == whiteTurn) {
                    kingRow = r;
                    kingCol = c;
                    break;
                }
            }
        }

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (board[r][c] != null && board[r][c].isWhite() != whiteTurn) {
                    if (board[r][c].isValidMove(r, c, kingRow, kingCol, board)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isCheckmate(boolean whiteTurn) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board[row][col] != null && board[row][col].isWhite() == whiteTurn) {
                    for (int r = 0; r < 8; r++) {
                        for (int c = 0; c < 8; c++) {
                            if (board[row][col].isValidMove(row, col, r, c, board)) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    private void restartGame() {
        setupPieces();
        isWhiteTurn = true;
        undoStack.clear();
        redoStack.clear();
    }

    private void updateBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col].setBackground((row + col) % 2 == 0 ? Color.WHITE : Color.GRAY);
                Piece piece = board[row][col];
                squares[row][col].setIcon(piece != null ? piece.getImage() : null);
            }
        }
    }

    public static void main(String[] args) {
        new ChessBoard();
    }
}

class GameState {
    private final Piece[][] board;
    private final boolean isWhiteTurn;

    public GameState(Piece[][] board, boolean isWhiteTurn) {
        this.board = new Piece[8][8];
        for (int i = 0; i < 8; i++) {
            System.arraycopy(board[i], 0, this.board[i], 0, 8);
        }
        this.isWhiteTurn = isWhiteTurn;
    }

    public Piece[][] getBoard() {
        return board;
    }

    public boolean isWhiteTurn() {
        return isWhiteTurn;
    }
}
