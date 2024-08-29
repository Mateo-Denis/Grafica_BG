package views.budget.list;

import views.IToggleableView;

import javax.swing.*;

public interface IBudgetListView extends IToggleableView {
    void setStringTableValueAt(int row, int col, String value);
    void setDoubleTableValueAt(int row, int col, double value);
    void setIntTableValueAt(int row, int col, int value);
    void clearView();
    void setBudgetTableModel();
    int getSelectedTableRow();
    void deselectAllRows();
    JFrame getJFrame();
}
