package views.budget;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;

public class BudgetSearchView {
    private JPanel containerPanel;
    private JPanel budgetSearchContainer;
    private JTextField searchField;
    private JPanel searchFieldContainer;
    private JPanel searchButtonsContainer;
    private JButton searchButton;
    private JPanel budgetResultContainer;
    private JTable budgetResultTable;
    private JScrollPane budgetResultScrollPanel;
    private JPanel budgetListButtonsContainer;
    private JButton budgetListOpenButton;

    public BudgetSearchView(){
        //Esta línea define el modelo que tendra la tabla en cuanto a los nombres de sus columnas. PD: Definir nombres de las columnas, estas son de ejemplo.
        DefaultTableModel tablemodel = new DefaultTableModel(new Object[]{"ID", "Cliente", "Fecha", "Total"}, 0);

        //Esta línea asigna el modelo a la tabla
        budgetResultTable.setModel(tablemodel);
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
