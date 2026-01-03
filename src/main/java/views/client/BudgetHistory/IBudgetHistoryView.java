package views.client.BudgetHistory;

import views.IToggleableView;

import javax.swing.*;

public interface IBudgetHistoryView extends IToggleableView {
    void clearView();
    void clearTable();
    void setTableValueAt(int row, int col, String value);
    void setClientName(String clientName);
    JTable getBudgetHistoryTable();
}
