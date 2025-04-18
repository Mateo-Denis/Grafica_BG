package views.products;

import views.IToggleableView;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public interface IProductSearchView extends IToggleableView {
    String getNameSearchText();
    void setStringTableValueAt(int row, int col, String value);
    void setDoubleTableValueAt(int row, int col, double value);
    void setIntTableValueAt(int row, int col, int value);
    void clearView();
    String getSelectedProductName();
    ArrayList<String> getMultipleSelectedProductNames();
    int getSelectedTableRow();
    void deselectAllRows();
    JTable getProductResultTable();
    void setCategoriesComboBox(List<String> categorias);
    JComboBox getCategoriesComboBox();
}
