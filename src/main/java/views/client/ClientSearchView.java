package views.client;

import presenters.StandardPresenter;
import presenters.client.ClientSearchPresenter;
import views.ToggleableView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ClientSearchView extends ToggleableView implements IClientSearchView  {
    private JPanel containerPanel;
    private JTextField searchField;
    private JTable clientResultTable;
    private JPanel clientResultContainer;
    private JScrollPane clientResultScrollPanel;
    private JPanel clientSearchContainer;
    private JButton searchButton;
    private JPanel clientListButtonsContainer;
    private JButton clientListOpenButton;
    private JComboBox<String> addressComboBox;
    private JLabel addressLabel;
    private DefaultTableModel tablemodel;
    private ClientSearchPresenter clientSearchPresenter;

    public ClientSearchView() {
        //Esta línea define el modelo que tendra la tabla en cuanto a los nombres de sus columnas. PD: Definir nombres de las columnas, estas son de ejemplo.
        windowFrame = new JFrame("Buscar Cliente");
        windowFrame.setContentPane(containerPanel);
        windowFrame.pack();
        windowFrame.setLocationRelativeTo(null);
        windowFrame.setIconImage(new ImageIcon("src/main/resources/BGLogo.png").getImage());
    }
    @Override
    public void start() {
        super.start();
        tablemodel = new DefaultTableModel(new Object[]{"ID", "Nombre", "Dirección", "Localidad", "Teléfono", "Cliente/Particular"}, 0);
        clientResultTable.setModel(tablemodel);

        addressComboBox.addItem("Cualquier localidad");
        addressComboBox.setSelectedItem("Cualquier localidad");
    }

    @Override
    public void setPresenter(StandardPresenter clientSearchPresenter) {
        this.clientSearchPresenter = (ClientSearchPresenter) clientSearchPresenter;
    }

    @Override
    protected void wrapContainer() {
        containerPanelWrapper = containerPanel;
    }

    @Override
    protected void initListeners() {
        searchButton.addActionListener(e -> clientSearchPresenter.onSearchButtonClicked());
        clientListOpenButton.addActionListener(e -> clientSearchPresenter.onClientListOpenButtonClicked());
    }

    @Override
    public void clearView() {
        for (int row = 0; row < clientResultTable.getRowCount(); row++) {
            for (int col = 0; col < clientResultTable.getColumnCount(); col++) {
                clientResultTable.setValueAt("", row, col);
            }
        }
        searchField.setText("");
    }

    public String getSearchText() {
        return searchField.getText();
    }

    public String getSelectedAddress() {
        return (String) addressComboBox.getSelectedItem();
    }

    public void addRow(Object[] rowData) {
        tablemodel.addRow(rowData);
    }
    /*
    *//* metodo utilizado para añadir un listener al JTextField. Es utilizado por el presentador *//*
    public void addTextFieldListener(ActionListener listener) {
        searchField.addActionListener(listener);
    }
    */
}
