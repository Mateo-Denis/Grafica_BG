package views.products.list;

import views.IToggleableView;

import javax.swing.*;

public interface IProductListView extends IToggleableView {
    void setStringTableValueAt(int row, int col, String value);
    void setDoubleTableValueAt(int row, int col, double value);
    void setIntTableValueAt(int row, int col, int value);
    void clearView();
    void setProductTableModel();
    int getSelectedTableRow();
    String getSelectedProductName();
    void deselectAllRows();
    JFrame getJFrame();
}
