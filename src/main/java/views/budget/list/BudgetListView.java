package views.budget.list;

import presenters.StandardPresenter;
import presenters.budget.BudgetListPresenter;
import views.ToggleableView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class BudgetListView extends ToggleableView implements IBudgetListView {
    private JPanel containerPanel;
    private JScrollPane budgetScrollPanel;
    private JTable budgetTable;
    private JPanel buttonsContainer;
    private JPanel titleContainer;
    private JLabel titleLabel;
    private DefaultTableModel tableModel;
    private BudgetListPresenter budgetListPresenter;

    public BudgetListView() {
        windowFrame = new JFrame("Lista de presupuestos");
        windowFrame.setContentPane(containerPanel);
        windowFrame.pack();
        windowFrame.setLocationRelativeTo(null);
        windowFrame.setIconImage(new ImageIcon("src/main/resources/BGLogo.png").getImage());
        wrapContainer();
        setBudgetTableModel();

        cambiarTamanioFuente(containerPanel, 14);
        windowFrame.setSize(720, 470);
        windowFrame.setResizable(false);
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
        budgetTable.setValueAt(value, row, col);
    }

    @Override
    public void setDoubleTableValueAt(int row, int col, double value) {
        budgetTable.setValueAt(value, row, col);
    }

    @Override
    public void setIntTableValueAt(int row, int col, int value) {
        budgetTable.setValueAt(value, row, col);
    }

    @Override
    public void clearView() {
        for (int row = 0; row < budgetTable.getRowCount(); row++) {
            for (int col = 0; col < budgetTable.getColumnCount(); col++) {
                budgetTable.setValueAt("", row, col);
            }
        }
    }

    @Override
    public void setBudgetTableModel() {
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nombre del Cliente", "Fecha de creación del presupuesto", "Cliente / Particular", "Numero de presupuesto"}, 200);
        budgetTable.setModel(tableModel);
    }

    @Override
    public int getSelectedTableRow() {
        return budgetTable.getSelectedRow();
    }

    @Override
    public void deselectAllRows() {
        budgetTable.clearSelection();
    }

    @Override
    public JFrame getJFrame() {
        return windowFrame;
    }

    @Override
    public void setPresenter(StandardPresenter budgetListPresenter) {
        this.budgetListPresenter = (BudgetListPresenter) budgetListPresenter;

    }

}
