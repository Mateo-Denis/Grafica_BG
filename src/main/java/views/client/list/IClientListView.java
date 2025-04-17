package views.client.list;

import views.IToggleableView;

public interface IClientListView extends IToggleableView {
    void setStringTableValueAt(int row, int col, String value);
    void setIntTableValueAt(int row, int col, int value);
    void clearView();
    void setClientTableModel();
}
