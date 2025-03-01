public class King extends Piece {
    public King(boolean isWhite) {
        super(isWhite, isWhite ? "images/white_king.png" : "images/black_king.png");
    }

    @Override
    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol, Piece[][] board) {
        int rowDiff = Math.abs(fromRow - toRow);
        int colDiff = Math.abs(fromCol - toCol);
        return rowDiff <= 1 && colDiff <= 1 && (board[toRow][toCol] == null || board[toRow][toCol].isWhite() != isWhite());
    }
}
