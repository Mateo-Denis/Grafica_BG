package views.products;

import views.IToggleableView;

public interface IProductSearchView extends IToggleableView {
    String getNameSearchText();
    void setStringTableValueAt(int row, int col, String value);
    void setDoubleTableValueAt(int row, int col, double value);


}
