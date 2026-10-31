package p02.pres;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import p02.game.Board;

public class GameBoardCellRenderer extends JLabel implements TableCellRenderer {

    private int playerWidth = 120;
    private int playerHeight = 120;

    public GameBoardCellRenderer() {
        setOpaque(false);
        setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        Image drawImage = null;
        Image originalImage = null;
        if (value instanceof Integer) {
            int cellType = (Integer) value;
            switch (cellType) {
                case Board.playerCell:
                    originalImage = Board.characterImage;
                    break;
                case Board.playerBox:
                    originalImage = Board.playerBoxImage;
                    break;
                case Board.turtleCell:
                    originalImage = Board.turtleImage;
                    break;
                case Board.fishCell:
                    originalImage = Board.fishImage;
                    break;
                case Board.turtleHunt:
                    originalImage = Board.turtleHuntImage;
                    break;
            }
        }

        if (originalImage != null) {
            if (value instanceof Integer &&
                (((Integer)value == Board.playerCell) || ((Integer)value == Board.playerBox))) {
                drawImage = originalImage.getScaledInstance(playerWidth, playerHeight, Image.SCALE_SMOOTH);
            } else if (originalImage != null) {
                int otherElementSize = Math.min(table.getRowHeight(row) - 4, table.getColumnModel().getColumn(column).getWidth() - 4);
                if (otherElementSize < 10) otherElementSize = 10;
                drawImage = originalImage.getScaledInstance(otherElementSize, otherElementSize, Image.SCALE_SMOOTH);
            }

            if (drawImage != null) {
                setIcon(new ImageIcon(drawImage));
            } else {
                setIcon(null);
            }
        } else {
            setIcon(null);
            setText("");
        }
        return this;
    }
}
