package views.budget;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import presenters.StandardPresenter;
import presenters.budget.BudgetSearchPresenter;
import views.ToggleableView;

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
    private BudgetSearchPresenter budgetSearchPresenter;

    public BudgetSearchView(){
        windowFrame = new JFrame("Buscar Presupuestos");
        windowFrame.setContentPane(containerPanel);
        windowFrame.pack();
        windowFrame.setLocationRelativeTo(null);
        windowFrame.setIconImage(new ImageIcon("src/main/resources/BGLogo.png").getImage());
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
        //budgetListOpenButton.addActionListener(e -> budgetSearchPresenter.onSearchListButtonClicked());
        //modifyButton.addActionListener(e -> budgetSearchPresenter.onModifyButtonClicked());
        //pdfButton.addActionListener(e -> budgetSearchPresenter.onPDFButtonClicked());
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

}
