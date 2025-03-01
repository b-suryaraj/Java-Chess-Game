public class Bishop extends Piece {
    public Bishop(boolean isWhite) {
        super(isWhite, isWhite ? "images/white_bishop.png" : "images/black_bishop.png");
    }

    @Override
    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol, Piece[][] board) {
        if (Math.abs(fromRow - toRow) == Math.abs(fromCol - toCol)) {
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
