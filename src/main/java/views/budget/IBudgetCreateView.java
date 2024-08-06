package views.budget;

import views.IToggleableView;

import java.util.List;

public interface IBudgetCreateView extends IToggleableView {
    String getBudgetClientName();
    String getBudgetDate();
    String getBudgetClientType();
    int getBudgetNumber();
    void clearTable();
    void setStringTableValueAt(int row, int col, String value);
}
