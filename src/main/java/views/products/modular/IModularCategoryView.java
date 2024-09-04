package views.products.modular;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Map;

public interface IModularCategoryView{
    JPanel getContainerPanel();
    //ArrayList<String> getModularAttributes();
    void initListeners();
    Map<String,String> getComboBoxValues();
    Map<String,String> getTextFieldValues();
    ArrayList<String> getRadioValues();
    Map<String,String> getModularAttributes();

    double getPrice();
}
