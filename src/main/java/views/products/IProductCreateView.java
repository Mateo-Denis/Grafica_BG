package views.products;

import views.IToggleableView;

public interface IProductCreateView extends IToggleableView {
    String getProductName();
    String getProductDescription();
    String getProductCategory();
    String getProductSubCategory();
    double getProductPrice();
    void updateSubCategoryComboBox(String category);

    void addSubCategory(String subCategory);

    void clearSubCategories();
}
