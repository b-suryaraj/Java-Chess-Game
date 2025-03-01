public class Queen extends Piece {
    public Queen(boolean isWhite) {
        super(isWhite, isWhite ? "images/white_queen.png" : "images/black_queen.png");
    }

    @Override
    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol, Piece[][] board) {
        return new Rook(isWhite()).isValidMove(fromRow, fromCol, toRow, toCol, board) ||
               new Bishop(isWhite()).isValidMove(fromRow, fromCol, toRow, toCol, board);
    }
}
