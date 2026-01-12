package views.client.BudgetHistory;

import lombok.Getter;
import org.jdesktop.swingx.JXFormattedTextField;
import presenters.StandardPresenter;
import presenters.client.BudgetHistoryPresenter;
import views.ToggleableView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class BudgetHistoryView extends ToggleableView implements IBudgetHistoryView {
    @Getter
    private JPanel containerPanel;
    @Getter
    private JTable budgetHistoryTable;
    private JScrollPane budgetHistoryScrollPanel;
    @Getter
    private JXFormattedTextField BudgetHistoryClientName;
    private BudgetHistoryPresenter budgetHistoryPresenter;

    public BudgetHistoryView() {
        windowFrame = new JFrame("Historial de Presupuestos");
        windowFrame.setContentPane(containerPanel);
        windowFrame.pack();
        windowFrame.setLocationRelativeTo(null);
        windowFrame.setIconImage(new ImageIcon("src/main/resources/BGLogo.png").getImage());
        cambiarTamanioFuente(containerPanel, 14);
        windowFrame.setSize(600, 400);
        windowFrame.setResizable(false);
    }


    @Override
    protected void wrapContainer() {
        containerPanelWrapper = containerPanel;
    }

    public void setClientName(String clientName) {
        BudgetHistoryClientName.setText("Historial de presupuestos de: " + clientName);
        BudgetHistoryClientName.setEditable(false);
        BudgetHistoryClientName.setFocusable(false);
    }

    @Override
    protected void initListeners() {
        budgetHistoryTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    budgetHistoryPresenter.onDoubleClickBudget();
                }
            }
        });
    }

    public void setPresenter(StandardPresenter budgetHistoryPresenter) {
        this.budgetHistoryPresenter = (BudgetHistoryPresenter) budgetHistoryPresenter;
    }

    private void setHistoryTable() {
        DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Nombre del cliente", "Numero de presupuesto", "Fecha del presupuesto", "Precio total del presupuesto"}, 200) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        budgetHistoryTable.setModel(tableModel);
        budgetHistoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    @Override
    public void clearView() {
        budgetHistoryTable.clearSelection();
        clearTable();
    }

    public void clearTable() {
        for (int row = 0; row < budgetHistoryTable.getRowCount(); row++) {
            for (int col = 0; col < budgetHistoryTable.getColumnCount(); col++) {
                budgetHistoryTable.setValueAt("", row, col);
            }
        }
    }

    public void setTableValueAt(int row, int col, String value) {
        budgetHistoryTable.setValueAt(value, row, col);
    }

    public void start() {
        super.start();
        setHistoryTable();
    }
}
