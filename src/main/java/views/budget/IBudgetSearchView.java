package views.budget;

import views.IToggleableView;

import javax.swing.*;
import java.util.ArrayList;

public interface IBudgetSearchView extends IToggleableView {
    String getSearchText();
    void setStringTableValueAt(int row, int col, String value);
    void clearTable();
    String getSelectedBudgetName();
    int getSelectedBudgetNumber();
    ArrayList<String> getMultipleSelectedBudgetNames();
    ArrayList<Integer> getMultipleSelectedBudgetNumbers();
    int getSelectedTableRow();
    JTable getBudgetResultTable();
    void deselectAllRows();
    String getStringValueAt(int row, int col);
    int getIntValueAt(int row, int col);
    double getDoubleValueAt(int row, int col);
    JButton getModifyButton();
}
