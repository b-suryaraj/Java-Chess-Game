public class Pawn extends Piece {
    public Pawn(boolean isWhite) {
        super(isWhite, isWhite ? "images/white_pawn.png" : "images/black_pawn.png");
    }

    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol, Piece[][] board) {
        int direction = isWhite ? -1 : 1;
        if (fromCol == toCol && board[toRow][toCol] == null) {
            if (toRow == fromRow + direction) {
                return true;
            }
            // First move: Can move two steps
            if ((isWhite() && fromRow == 6) || (!isWhite() && fromRow == 1)) {
                return toRow == fromRow + 2 * direction && board[fromRow + direction][toCol] == null;
            }
        }
        
        if (Math.abs(fromCol - toCol) == 1 && toRow == fromRow + direction) {
            return board[toRow][toCol] != null && board[toRow][toCol].isWhite() != isWhite();
        }

        return false;
    
    }

}
