package views.products.modular;

import org.javatuples.Triplet;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
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

    ArrayList<String> getRelevantInformation();

	void loadComboBoxValues();
    void loadTextFieldsValues();

    ArrayList<String> getExhaustiveInformation();

    List<Triplet<String, String, Double>> getModularPrices();

    void unlockTextFields();

    void blockTextFields();

    void setPriceTextFields();
}
