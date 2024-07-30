package views.client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;

public class ClientSearchView {
    private JPanel containerPanel;
    private JTextField textField1;
    private JTable table1;
    private DefaultTableModel tablemodel;

    public ClientSearchView() {
        // Crear el modelo de la tabla con las columnas
        tablemodel = new DefaultTableModel(new Object[]{"ID", "Nombre", "Dirección", "Localidad", "Teléfono", "Cliente/Particular"}, 0);

        // Asignar el modelo a la tabla
        table1.setModel(tablemodel);
    }

    /* metodo utilizado para recuperar el JPanel global. Lo utiliza el Main principal para setear el contentpane */
    public JPanel getContainerPanel() {
        return containerPanel;
    }

    /* metodo utilizado para recuperar el texto introducido en el JTextField. Es utilizado por el presentador */
    public String getInputText() {
        return textField1.getText();
    }

    /* metodo utilizado para añadir una fila a la tabla. Es utilizado por el presentador */
    public void addRow(Object[] rowData) {
        tablemodel.addRow(rowData);
    }

    /* metodo utilizado para añadir un listener al JTextField. Es utilizado por el presentador */
    public void addTextFieldListener(ActionListener listener) {
        textField1.addActionListener(listener);
    }
}
