package views.client.list;

import views.IToggleableView;

import javax.swing.*;

public interface IClientListView extends IToggleableView {
    void setStringTableValueAt(int row, int col, String value);
    void setDoubleTableValueAt(int row, int col, double value);
    void setIntTableValueAt(int row, int col, int value);
    void clearView();
    void setClientTableModel();
    int getSelectedTableRow();
    void deselectAllRows();
    JFrame getJFrame();
}
