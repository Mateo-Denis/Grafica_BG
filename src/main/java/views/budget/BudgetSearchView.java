package views.budget;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import presenters.StandardPresenter;
import presenters.budget.BudgetSearchPresenter;
import views.ToggleableView;

public class BudgetSearchView extends ToggleableView{
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
        DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Nombre del Cliente", "Fecha del presupuesto", "Cliente / Particular"}, 200);
        budgetResultTable.setModel(tableModel);
    }

    @Override
    protected void wrapContainer() {

    }

    @Override
    protected void initListeners() {

    }

    @Override
    public void clearView() {

    }

/*    @Override
    public void setPresenter(StandardPresenter budgetSearchPresenter) {
        this.budgetSearchPresenter = (BudgetSearchPresenter) budgetSearchPresenter;
    }*/

}
