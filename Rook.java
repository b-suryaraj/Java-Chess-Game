public class Rook extends Piece {
    public Rook(boolean isWhite) {
        super(isWhite,isWhite ? "images/white_rook.png" : "images/black_rook.png");
    }

    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol, Piece[][] board) {
        if (fromRow == toRow || fromCol == toCol) {
            int rowDirection = Integer.compare(toRow, fromRow);
            int colDirection = Integer.compare(toCol, fromCol);
            int checkRow = fromRow + rowDirection, checkCol = fromCol + colDirection;

            while (checkRow != toRow || checkCol != toCol) {
                if (board[checkRow][checkCol] != null) return false;
                checkRow += rowDirection;
                checkCol += colDirection;
            }

            return board[toRow][toCol] == null || board[toRow][toCol].isWhite() != isWhite();
        }
        return false;    
    }

}
