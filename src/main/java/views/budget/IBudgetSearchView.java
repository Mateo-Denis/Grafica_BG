package views.budget;

import PdfFormater.Row;
import views.IToggleableView;

import javax.swing.*;
import java.util.ArrayList;

public interface IBudgetSearchView extends IToggleableView {

    String getSelectedBudgetName();
    String getSelectedBudgetClientType();
    int getSelectedBudgetNumber();
    ArrayList<Row> getBudgetContent();
    ArrayList<String> getMultipleSelectedBudgetNames();
    ArrayList<Integer> getMultipleSelectedBudgetNumbers();
    int getSelectedTableRow();
    JTable getBudgetResultTable();
    void deselectAllRows();
    String getStringValueAt(int row, int col);
    int getIntValueAt(int row, int col);
    double getDoubleValueAt(int row, int col);
    JButton getModifyButton();
    void setStringTableValueAt(int row, int col, String value);
    String getSearchText();
    void clearTable();
}
