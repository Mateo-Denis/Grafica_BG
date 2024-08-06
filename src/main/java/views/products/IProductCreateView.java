package views.products;

import views.IToggleableView;

import javax.swing.*;
import java.util.List;

public interface IProductCreateView extends IToggleableView {
    String getProductName();
    String getProductDescription();
    String getProductCategory();
    double getProductPrice();
    void showCategoryOptions(JPanel categoryOptions);
    String getProductSubCategory();
    //void addSubCategory(String subCategory);
    //void clearSubCategories();


    public void setCategorias(List<String> categorias);
    public JComboBox<String> getComboBoxCategorias();
}
