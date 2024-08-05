package views.products;

import views.IToggleableView;

import javax.swing.*;

public interface IProductCreateView extends IToggleableView {
    String getProductName();
    String getProductDescription();
    String getProductCategory();
    double getProductPrice();
    void showCategoryOptions(JPanel categoryOptions);
    String getProductSubCategory();
    //void addSubCategory(String subCategory);
    //void clearSubCategories();
}
