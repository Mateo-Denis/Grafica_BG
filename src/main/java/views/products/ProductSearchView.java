package views.products;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;

public class ProductSearchView {
    private JPanel containerPanel;
    private JTextField SearchField;
    private JPanel productSearchContainer;
    private JTable productResultTable;
    private JPanel productResultContainer;
    private JScrollPane productResultScrollPanel;
    private JPanel searchButtonsContainer;
    private JButton searchButton;
    private JPanel searchFieldContainer;
    private JPanel productListButtonsContainer;
    private JButton productListOpenButton;
    private final DefaultTableModel tablemodel;

    public ProductSearchView() {
        //Esta línea define el modelo que tendra la tabla en cuanto a los nombres de sus columnas. PD: Definir nombres de las columnas, estas son de ejemplo.
        tablemodel = new DefaultTableModel(new Object[]{"ID", "Nombre", "Descripción", "Precio"}, 0);

        // Esta línea asigna el modelo a la tabla
        productResultTable.setModel(tablemodel);
    }

/*
    *//* metodo utilizado para recuperar el JPanel global. Lo utiliza el Main principal para setear el contentpane *//*
    public JPanel getContainerPanel() {
        return containerPanel;
    }

    *//* metodo utilizado para recuperar el texto introducido en el JTextField. Es utilizado por el presentador *//*
    public String getInputText() {
        return SearchField.getText();
    }

    *//* metodo utilizado para añadir una fila a la tabla. Es utilizado por el presentador *//*
    public void addRow(Object[] rowData) {
        tablemodel.addRow(rowData);
    }

    *//* metodo utilizado para añadir un listener al JTextField. Es utilizado por el presentador *//*
    public void addTextFieldListener(ActionListener listener) {
        SearchField.addActionListener(listener);
    }

    */
}

