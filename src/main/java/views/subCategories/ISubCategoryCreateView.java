package views.subCategories;

import views.IToggleableView;
import javax.swing.*;
import java.util.List;

public interface ISubCategoryCreateView extends IToggleableView {
    String getSubCategoryName();
    String getCategoryAssociated();
    void setCategorias(List<String> categorias);
    JComboBox<String> getComboBoxCategorias();
    JButton getCreateButton();

}
