package p02.pres;

import javax.swing.table.AbstractTableModel;
import p02.game.Board;

public class BoardModel extends AbstractTableModel {
    private Board gameBoard;

    public BoardModel(Board gameBoard) {
        this.gameBoard = gameBoard;
    }

    public int getRowCount() {
        return gameBoard.getRows();
    }

    public int getColumnCount() {
        return gameBoard.getColumns();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return gameBoard.getCell(columnIndex, rowIndex);
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public void fireTableDataChanged() {
        super.fireTableDataChanged();
    }

    public void fireTableCellUpdated(int row, int column) {
        super.fireTableCellUpdated(row, column);
    }

    public String getColumnName(int column) {
        return "";
    }
}
