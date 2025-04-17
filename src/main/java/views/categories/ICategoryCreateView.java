package views.categories;

import views.IToggleableView;

import javax.swing.*;

public interface ICategoryCreateView extends IToggleableView {
    JTextField getCategoryNameField();
    JButton getAddFieldButton();
    JButton getCreateCategoryButton();
    JComboBox<String> getFieldTypeComboBox();
    JPanel getPreviewPanel();
    void addField(JComponent field);
    void clearView();
}
