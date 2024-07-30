package views.client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;

public class ClientSearchView {
    private JPanel containerPanel;
    private JTextField searchField;
    private JTable clientResultTable;
    private JPanel clientResultContainer;
    private JScrollPane clientResultScrollPanel;
    private JPanel clientSearchContainer;
    private JPanel searchFieldContainer;
    private JPanel searchButtonsContainer;
    private JButton searchButton;
    private JPanel clientListButtonsContainer;
    private JButton clientListOpenButton;
    private DefaultTableModel tablemodel;

    public ClientSearchView() {
        //Esta línea define el modelo que tendra la tabla en cuanto a los nombres de sus columnas. PD: Definir nombres de las columnas, estas son de ejemplo.
        tablemodel = new DefaultTableModel(new Object[]{"ID", "Nombre", "Dirección", "Localidad", "Teléfono", "Cliente/Particular"}, 0);

        // Esta línea asigna el modelo a la tabla
        clientResultTable.setModel(tablemodel);
    }

/*
    *//* metodo utilizado para recuperar el JPanel global. Lo utiliza el Main principal para setear el contentpane *//*
    public JPanel getContainerPanel() {
        return containerPanel;
    }

    *//* metodo utilizado para recuperar el texto introducido en el JTextField. Es utilizado por el presentador *//*
    public String getInputText() {
        return searchField.getText();
    }

    *//* metodo utilizado para añadir una fila a la tabla. Es utilizado por el presentador *//*
    public void addRow(Object[] rowData) {
        tablemodel.addRow(rowData);
    }

    *//* metodo utilizado para añadir un listener al JTextField. Es utilizado por el presentador *//*
    public void addTextFieldListener(ActionListener listener) {
        searchField.addActionListener(listener);
    }
    */
}
