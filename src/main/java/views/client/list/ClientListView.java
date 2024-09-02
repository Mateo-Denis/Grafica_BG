package views.client.list;

import java.awt.*;
import com.intellij.uiDesigner.core.*;
import presenters.StandardPresenter;
import views.ToggleableView;
import presenters.client.ClientListPresenter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ClientListView extends ToggleableView implements IClientListView {
    private DefaultTableModel tableModel;
    private ClientListPresenter clientListPresenter;

    public ClientListView() {
        initComponents();
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

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - unknown
        containerPanel = new JPanel();
        clientScrollPanel = new JScrollPane();
        clientTable = new JTable();
        buttonsContainer = new JPanel();
        var hSpacer1 = new Spacer();
        titleContainer = new JPanel();
        titleLabel = new JLabel();

        //======== containerPanel ========
        {
            containerPanel.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax.swing
            .border.EmptyBorder(0,0,0,0), "JFor\u006dDesi\u0067ner \u0045valu\u0061tion",javax.swing.border.TitledBorder
            .CENTER,javax.swing.border.TitledBorder.BOTTOM,new java.awt.Font("Dia\u006cog",java.
            awt.Font.BOLD,12),java.awt.Color.red),containerPanel. getBorder()))
            ;containerPanel. addPropertyChangeListener(new java.beans.PropertyChangeListener(){@Override public void propertyChange(java.beans.PropertyChangeEvent e
            ){if("bord\u0065r".equals(e.getPropertyName()))throw new RuntimeException();}})
            ;
            containerPanel.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));

            //======== clientScrollPanel ========
            {
                clientScrollPanel.setViewportView(clientTable);
            }
            containerPanel.add(clientScrollPanel, new GridConstraints(1, 0, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
                null, null, null));

            //======== buttonsContainer ========
            {
                buttonsContainer.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
                buttonsContainer.add(hSpacer1, new GridConstraints(0, 0, 1, 1,
                    GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
                    GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK,
                    null, null, null));
            }
            containerPanel.add(buttonsContainer, new GridConstraints(2, 0, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                null, null, null));

            //======== titleContainer ========
            {
                titleContainer.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));

                //---- titleLabel ----
                titleLabel.setText("Todos los clientes:");
                titleContainer.add(titleLabel, new GridConstraints(0, 0, 1, 1,
                    GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
                    GridConstraints.SIZEPOLICY_FIXED,
                    GridConstraints.SIZEPOLICY_FIXED,
                    null, null, null));
            }
            containerPanel.add(titleContainer, new GridConstraints(0, 0, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                null, null, null));
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - unknown
    private JPanel containerPanel;
    private JScrollPane clientScrollPanel;
    private JTable clientTable;
    private JPanel buttonsContainer;
    private JPanel titleContainer;
    private JLabel titleLabel;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
