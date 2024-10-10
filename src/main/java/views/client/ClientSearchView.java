package views.client;

import presenters.StandardPresenter;
import presenters.client.ClientListPresenter;
import presenters.client.ClientSearchPresenter;
import views.ToggleableView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class ClientSearchView extends ToggleableView implements IClientSearchView  {
    private JPanel containerPanel;
    private JTextField nameSearchField;
    private JTable clientResultTable;
    private JPanel clientResultContainer;
    private JScrollPane clientResultScrollPanel;
    private JPanel clientSearchContainer;
    private JButton searchButton;
    private JPanel bottomButtonsContainer;
    private JButton clientListOpenButton;
    private JComboBox<String> cityComboBox;
    private JLabel addressLabel;
    private JButton deleteButton;
    private ClientSearchPresenter clientSearchPresenter;
    private ClientListPresenter clientListPresenter;

    public ClientSearchView(ClientListPresenter clientListPresenter) {
        windowFrame = new JFrame("Buscar Cliente");
        windowFrame.setContentPane(containerPanel);
        windowFrame.pack();
        windowFrame.setLocationRelativeTo(null);
        windowFrame.setIconImage(new ImageIcon("src/main/resources/BGLogo.png").getImage());

        this.clientListPresenter = clientListPresenter;
        cambiarTamanioFuente(containerPanel, 14);
        windowFrame.setSize(470,560);
        windowFrame.setResizable(false);

        initListeners();
    }

    @Override
    public void start() {
        super.start();
		DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Nombre", "Dirección", "Localidad", "Teléfono", "Cliente/Particular"}, 200);
        clientResultTable.setModel(tableModel);

        cityComboBox.addItem("Cualquier localidad");
        cityComboBox.setSelectedItem("Cualquier localidad");
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
        deleteButton.addActionListener(e -> clientSearchPresenter.onDeleteClientButtonClicked());
        clientListOpenButton.addActionListener(e -> clientListPresenter.onSearchViewOpenListButtonClicked());
    }

    @Override
    public void clearView() {
        clearTable();
        nameSearchField.setText("");
        cityComboBox.setSelectedItem("Cualquier localidad");
    }
    public void clearTable(){
        for (int row = 0; row < clientResultTable.getRowCount(); row++) {
            for (int col = 0; col < clientResultTable.getColumnCount(); col++) {
                clientResultTable.setValueAt("", row, col);
            }
        }
    }

    public String getnameSearchText() {
        return nameSearchField.getText();
    }

    public String getSelectedCity() {
        return (String) cityComboBox.getSelectedItem();
    }


    public void addCityToComboBox(String city) {
        cityComboBox.addItem(city);
    }

    public void setTableValueAt(int row, int col, String value) {
        clientResultTable.setValueAt(value, row, col);
    }
    public void setSelectedCity(String city) {
        cityComboBox.setSelectedItem(city);
    }

    public boolean isCityInComboBox(String city) {
        for (int i = 0; i < cityComboBox.getItemCount(); i++) {
            if (cityComboBox.getItemAt(i).equals(city)) {
                return true;
            }
        }
        return false;
    }

    public int getSelectedTableRow() {
        return clientResultTable.getSelectedRow();
    }

    public String getSelectedClientName() {
        String clientName = "";
        try {
            clientName = (String) clientResultTable.getValueAt(getSelectedTableRow(), 0);
            return clientName;
        } catch (ArrayIndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(null, "No client selected", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    @Override
    public ArrayList<String> getMultipleSelectedClientNames() {
        ArrayList<String> clientNames = new ArrayList<>();
        int[] selectedRows = clientResultTable.getSelectedRows();
        for (int row : selectedRows) {
            String clientName = (String) clientResultTable.getValueAt(row, 0);
            clientNames.add(clientName);
        }
        return clientNames;
    }

    public void deselectAllRows() {
        clientResultTable.clearSelection();
    }

    public JTable getClientResultTable() {
        return clientResultTable;
    }
}
