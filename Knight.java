public class Knight extends Piece {
    public Knight(boolean isWhite) {
        super(isWhite, isWhite ? "images/white_knight.png" : "images/black_knight.png");
    }

    @Override
    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol, Piece[][] board) {
        int rowDiff = Math.abs(fromRow - toRow);
        int colDiff = Math.abs(fromCol - toCol);
        return (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);
    }

}
