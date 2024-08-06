package views.budget;

import views.IToggleableView;

public interface IBudgetSearchView extends IToggleableView {
    String getSearchText();
    void setStringTableValueAt(int row, int col, String value);
    void clearTable();
}
