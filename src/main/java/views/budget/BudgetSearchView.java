package views.budget;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import presenters.StandardPresenter;
import presenters.budget.BudgetListPresenter;
import presenters.budget.BudgetSearchPresenter;
import views.ToggleableView;

import java.util.ArrayList;
import java.util.List;


public class BudgetSearchView extends ToggleableView implements IBudgetSearchView{
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
    private JButton modifyButton;
    private JButton pdfButton;
    private JButton cleanTableButton;
    private JButton deleteButton;
    private BudgetSearchPresenter budgetSearchPresenter;
    private BudgetListPresenter budgetListPresenter;

    public BudgetSearchView(BudgetListPresenter budgetListPresenter) {
        windowFrame = new JFrame("Buscar Presupuestos");
        windowFrame.setContentPane(containerPanel);
        windowFrame.pack();
        windowFrame.setLocationRelativeTo(null);
        windowFrame.setIconImage(new ImageIcon("src/main/resources/BGLogo.png").getImage());

        this.budgetListPresenter = budgetListPresenter;

        initListeners();
    }

    @Override
    public void start() {
        super.start();
        DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Nombre del Cliente", "Fecha del presupuesto", "Cliente / Particular", "Numero de Presupuesto"}, 200);
        budgetResultTable.setModel(tableModel);
    }

    @Override
    protected void wrapContainer() {
        containerPanelWrapper = containerPanel;
    }

    @Override
    protected void initListeners() {
        searchButton.addActionListener(e -> budgetSearchPresenter.onSearchButtonClicked());
        cleanTableButton.addActionListener(e -> budgetSearchPresenter.onCleanTableButtonClicked());
        budgetListOpenButton.addActionListener(e -> budgetListPresenter.onSearchViewOpenListButtonClicked());
        //modifyButton.addActionListener(e -> budgetSearchPresenter.onModifyButtonClicked());
        //pdfButton.addActionListener(e -> budgetSearchPresenter.onPDFButtonClicked());
        deleteButton.addActionListener(e -> budgetSearchPresenter.onDeleteButtonClicked());
        modifyButton.addActionListener(e -> budgetSearchPresenter.onModifyButtonClicked());
    }

    @Override
    public void clearView() {
        clearTable();
        searchField.setText("");
    }

    @Override
    public void setPresenter(StandardPresenter budgetSearchPresenter) {
        this.budgetSearchPresenter = (BudgetSearchPresenter) budgetSearchPresenter;
    }

    @Override
    public String getSearchText() {
        return searchField.getText();
    }

    public void setStringTableValueAt(int row, int col, String value) {
        budgetResultTable.setValueAt(value, row, col);
    }

    public void clearTable() {
        for (int row = 0; row < budgetResultTable.getRowCount(); row++) {
            for (int col = 0; col < budgetResultTable.getColumnCount(); col++) {
                budgetResultTable.setValueAt("", row, col);
            }
        }
    }

    @Override
    public String getSelectedBudgetName() {
        String budgetName = "";
        try {
            budgetName = (String) budgetResultTable.getValueAt(getSelectedTableRow(), 0);
            return budgetName;
        } catch (ArrayIndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(null, "No hay ningún presupuesto seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    @Override
    public ArrayList<String> getMultipleSelectedBudgetNames() {
        ArrayList<String> budgetNames = new ArrayList<>();
        int[] selectedRows = budgetResultTable.getSelectedRows();
        for (int row : selectedRows) {
            String budgetName = (String) budgetResultTable.getValueAt(row, 0);
            budgetNames.add(budgetName);
        }
        return budgetNames;
    }

    @Override
    public int getSelectedTableRow() {
        return budgetResultTable.getSelectedRow();
    }

    @Override
    public JTable getBudgetResultTable() {
        return budgetResultTable;
    }

    @Override
    public void deselectAllRows() {
        budgetResultTable.clearSelection();
    }

    @Override
    public String getStringValueAt(int row, int col) {
        return (String) budgetResultTable.getValueAt(row, col);
    }

    @Override
    public int getIntValueAt(int row, int col) {
        return (int) budgetResultTable.getValueAt(row, col);
    }

    @Override
    public double getDoubleValueAt(int row, int col) {
        return (double) budgetResultTable.getValueAt(row, col);
    }
}
