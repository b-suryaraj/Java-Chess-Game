import javax.swing.*;
import java.awt.*;

public abstract class Piece {
    protected boolean isWhite;
    private ImageIcon image;

    public Piece(boolean isWhite, String imagePath) {
        this.isWhite = isWhite;
        this.image = new ImageIcon(new ImageIcon(imagePath).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
    }

    public boolean isWhite() {
        return isWhite;
    }

    public ImageIcon getImage() {
        return image;
    }

    public abstract boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol, Piece[][] board);
}
