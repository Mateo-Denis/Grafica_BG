package views.client.list;

import presenters.StandardPresenter;
import views.ToggleableView;
import presenters.client.ClientListPresenter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ClientListView extends ToggleableView implements IClientListView {
    private JPanel containerPanel;
    private JPanel buttonsContainer;
    private JScrollPane clientScrollPanel;
    private JTable clientTable;
    private JPanel titleContainer;
    private JLabel titleLabel;
    private DefaultTableModel tableModel;
    private ClientListPresenter clientListPresenter;

    public ClientListView() {
        windowFrame = new JFrame("Lista de Clientes");
        windowFrame.setContentPane(containerPanel);
        windowFrame.pack();
        windowFrame.setLocationRelativeTo(null);
        windowFrame.setIconImage(new ImageIcon("src/main/resources/BGLogo.png").getImage());
        wrapContainer();
        setClientTableModel();
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    protected void wrapContainer() {
        containerPanelWrapper = containerPanel;
    }

    @Override
    protected void initListeners() {

    }

    @Override
    public void setStringTableValueAt(int row, int col, String value) {
        clientTable.setValueAt(value, row, col);
    }

    @Override
    public void setDoubleTableValueAt(int row, int col, double value) {
        clientTable.setValueAt(value, row, col);
    }

    @Override
    public void setIntTableValueAt(int row, int col, int value) {
        clientTable.setValueAt(value, row, col);
    }

    @Override
    public void clearView() {
        for (int row = 0; row < clientTable.getRowCount(); row++) {
            for (int col = 0; col < clientTable.getColumnCount(); col++) {
                clientTable.setValueAt("", row, col);
            }
        }
    }

    @Override
    public void setClientTableModel() {
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nombre", "Dirección", "Localidad", "Teléfono", "Cliente / Particular"}, 200);
        clientTable.setModel(tableModel);
    }

    @Override
    public int getSelectedTableRow() {
        return clientTable.getSelectedRow();
    }

    @Override
    public void deselectAllRows() {
        clientTable.clearSelection();
    }

    @Override
    public JFrame getJFrame() {
        return windowFrame;
    }

    @Override
    public void setPresenter(StandardPresenter clientListPresenter) {
        this.clientListPresenter = (ClientListPresenter) clientListPresenter;
    }
}
