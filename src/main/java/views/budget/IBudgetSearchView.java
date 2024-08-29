package views.budget;

import views.IToggleableView;

import javax.swing.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public interface IBudgetSearchView extends IToggleableView {
    String getSearchText();
    void setStringTableValueAt(int row, int col, String value);
    void clearTable();
    String getSelectedBudgetName();
    ArrayList<String> getMultipleSelectedBudgetNames();
    int getSelectedTableRow();
    JTable getBudgetResultTable();
    void deselectAllRows();
    String getStringValueAt(int row, int col);
    int getIntValueAt(int row, int col);
    double getDoubleValueAt(int row, int col);
}
