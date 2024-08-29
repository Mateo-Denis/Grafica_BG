package presenters.categories;

import presenters.StandardPresenter;
import views.products.modular.IModularCategoryView;
import views.products.IProductCreateView;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ModularCategoriesPresenter extends StandardPresenter {
    private IModularCategoryView view;
    private IProductCreateView productCreateView;

    public ModularCategoriesPresenter(IProductCreateView productCreateView) {
        this.productCreateView = productCreateView;
        view = productCreateView.getCorrespondingModularView(productCreateView.getProductCategory());
        initListeners();
    }

    @Override
    public void initListeners() {
        int comboBoxCount = 0;
        int textFieldCount = 0;
        ArrayList<String> comboBoxList = new ArrayList<>();
        ArrayList<String> textFieldList = new ArrayList<>();

        for (Component component : view.getContainerPanel().getComponents()) { //POR CADA TIPO DE COMPONENTE PRESENTE EN EL PANEL
            if (component instanceof JRadioButton) { //SI ES UN RADIOBUTTON
                JRadioButton radioButton = (JRadioButton) component; //CASTEARLO A JRadioButton
                radioButton.addActionListener(e -> { //AÑADIRLE UN LISTENER
                    String radioSelected = "";
                    if (radioButton.isSelected()) { //SI ESTÁ SELECCIONADO
                        for (Component c : view.getContainerPanel().getComponents()) { //POR CADA COMPONENTE EN EL PANEL
                            if (c instanceof JRadioButton && c != radioButton) { //SI ES UN RADIOBUTTON Y NO ES EL MISMO QUE EL QUE SE SELECCIONÓ
                                ((JRadioButton) c).setSelected(false); //DESELECCIONARLO
                            } else {
                                radioSelected = radioButton.getText();
                                view.getRadioValues().add(radioSelected);
                            }
                        }
                    }
                });
            } else if (component instanceof JComboBox) { //SI ES UN COMBOBOX
                JComboBox comboBox = (JComboBox) component; //CASTEARLO A JComboBox
                comboBoxCount++;
                comboBoxList.add("ComboBox number: " + comboBoxCount);
                comboBox.addActionListener(e -> {
                    String selected = (String) comboBox.getSelectedItem(); //OBTENER EL ITEM SELECCIONADO
                    String comboBoxName = comboBox.getName(); //OBTENER EL NOMBRE DEL COMBOBOX
                    view.getComboBoxValues().put(comboBoxName, selected); //GUARDAR EL NOMBRE Y EL ITEM SELECCIONADO
                });
            } else if (component instanceof JTextField) { //SI ES UN TEXTFIELD
                textFieldCount++;
                textFieldList.add("TextField number: " + textFieldCount);
                JTextField textField = (JTextField) component; //CASTEARLO A JTextField
                textField.addActionListener(e -> {
                    String text = textField.getText(); //OBTENER EL TEXTO
                    String textFieldName = textField.getName(); //OBTENER EL NOMBRE DEL TEXTFIELD
                    view.getTextFieldValues().put(textFieldName, text); //GUARDAR EL NOMBRE Y EL TEXTO
                });
            }
        }
    }
}
