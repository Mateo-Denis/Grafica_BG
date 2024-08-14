package views.products;

import views.IToggleableView;

import java.util.ArrayList;

public interface IProductSearchView extends IToggleableView {
    String getNameSearchText();
    void setStringTableValueAt(int row, int col, String value);
    void setDoubleTableValueAt(int row, int col, double value);
    void setIntTableValueAt(int row, int col, int value);
    void clearView();
    String getSelectedProductName();
    //ArrayList<String> getVisibleProductNames();
    int getSelectedTableRow();
    void deselectAllRows();
}
