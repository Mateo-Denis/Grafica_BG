package views.client.list;

import javax.swing.*;

public interface IClientListView {
    void setStringTableValueAt(int row, int col, String value);
    void setDoubleTableValueAt(int row, int col, double value);
    void setIntTableValueAt(int row, int col, int value);
    void clearView();
    void setClientTableModel();
    int getSelectedTableRow();
    String getSelectedClientName();
    void deselectAllRows();
    JFrame getJFrame();
}
