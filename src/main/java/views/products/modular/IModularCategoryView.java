package views.products.modular;

import org.javatuples.Triplet;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface IModularCategoryView{

    JPanel getContainerPanel();
    void initListeners();

    Map<String,String> getComboBoxValues();

    Map<String,String> getTextFieldValues();

    ArrayList<String> getRadioValues();

	void loadComboBoxValues();

    List<Triplet<String, String, Double>> getModularPrices();

    void unlockTextFields();

    void blockTextFields();

    void setPriceTextFields();
}
