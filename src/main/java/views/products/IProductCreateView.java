package views.products;

import views.IToggleableView;
import views.products.modular.IModularCategoryView;

import javax.swing.*;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface IProductCreateView extends IToggleableView {
    String getProductName();
    String getProductDescription();
    String getProductCategory();
    double getProductPrice();
    void setCategorias(List<String> categorias);
    JPanel getContainerPanel();
    void comboBoxListenerSet(ItemListener listener);
    JPanel getModularView();
    //JPanel getCorrespondingModularView(String category);
    IModularCategoryView getCorrespondingModularView(String category);
    //Map<String, JPanel> getCategoryPanelsMap();
    Map<String, IModularCategoryView> getCategoryPanelsMap();
    void showSelectedView(String category);
    void componentsListenerSet(ItemListener listener);
}
