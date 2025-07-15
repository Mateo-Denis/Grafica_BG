package views.client;

import lombok.Getter;
import presenters.StandardPresenter;
import presenters.client.ClientListPresenter;
import presenters.client.ClientSearchPresenter;
import views.ToggleableView;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ClientSearchView extends ToggleableView implements IClientSearchView {
    private JPanel containerPanel;
    private JTextField nameSearchField;
    @Getter
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
    private final ClientListPresenter clientListPresenter;

    public ClientSearchView(ClientListPresenter clientListPresenter) {
        windowFrame = new JFrame("Buscar Cliente");
        windowFrame.setContentPane(containerPanel);
        windowFrame.pack();
        windowFrame.setLocationRelativeTo(null);
        windowFrame.setIconImage(new ImageIcon("src/main/resources/BGLogo.png").getImage());

        this.clientListPresenter = clientListPresenter;
        cambiarTamanioFuente(containerPanel, 14);
        windowFrame.setSize(470, 560);
        windowFrame.setResizable(false);
    }

    @Override
    public void start() {
        super.start();
        SetClientTable();
        SetCityComboBox();
    }

    private void SetCityComboBox() {
        cityComboBox.addItem("Cualquier localidad");
        cityComboBox.setSelectedItem("Cualquier localidad");
    }

    private void SetClientTable() {
        DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Nombre", "Dirección", "Localidad", "Teléfono", "Cliente/Particular"}, 200) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        clientResultTable.setModel(tableModel);
        clientResultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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

        nameSearchField.addActionListener(e -> clientSearchPresenter.onSearchButtonClicked());
    }

    @Override
    public void clearView() {
        clearTable();
        nameSearchField.setText("");
        cityComboBox.setSelectedItem("Cualquier localidad");
        clientResultTable.clearSelection();
    }

    public void clearTable() {
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

    public void deselectAllRows() {
        clientResultTable.clearSelection();
    }

    public String getClientStringTableValueAt(int row, int col) {
        return (String) clientResultTable.getValueAt(row, col);
    }

}
