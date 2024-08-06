package views.budget;

import presenters.StandardPresenter;
import presenters.budget.BudgetCreatePresenter;
import views.ToggleableView;

import javax.swing.*;
import java.util.List;

public class BudgetCreateView extends ToggleableView implements IBudgetCreateView {
    private JPanel containerPanel;
    private JPanel clientSearchingContainer;
    private JPanel productSearchingContainer;
    private JPanel budgetPreviewContainer;
    private JLabel budgetNumberLabel;
    private JPanel nameAndCityContainer;
    private JTextField clientTextField;
    private JComboBox cityComboBox;
    private JLabel cityLabel;
    private JLabel clientLabel;
    private JButton clientSearchButton;
    private JPanel clientResultContainer;
    private JScrollPane clientResultScrollPanel;
    private JTable clientResultTable;
    private JLabel clientSelectedLabel;
    private JPanel categoriesContainer;
    private JLabel productLabel;
    private JComboBox productCategoryComboBox;
    private JPanel especificationsContainer;
    private JTextField amountTextField;
    private JPanel amountContainer;
    private JLabel amountLabel;
    private JPanel observationsContainer;
    private JTextField observationsTextField;
    private JLabel observationsLabel;
    private JComboBox subCategoryComboBox;
    private JButton addProductButton;
    private JScrollPane budgetPreviewScrollPanel;
    private JTable budgetPreviewTable;
    private JPanel budgetCreationButtonsContainer;
    private JButton budgetPreviewButton;
    private JLabel selectedProductsLabel;
    private JButton budgetCreateButton;
    private JPanel tableContainer;
    private JPanel buttonsContainer;
    private JButton deleteSelectedProductButton;
    private JButton eliminarTodosButton;
    private JLabel previewTableLabe;
    private BudgetCreatePresenter budgetCreatePresenter;

    public BudgetCreateView(){
        windowFrame = new JFrame("Crear Presupuesto");
        windowFrame.setContentPane(containerPanel);
        windowFrame.pack();
        windowFrame.setLocationRelativeTo(null);
        windowFrame.setIconImage(new ImageIcon("src/main/resources/BGLogo.png").getImage());
    }

    @Override
    protected void wrapContainer() {
        containerPanelWrapper = containerPanel;
    }

    @Override
    protected void initListeners() {
        budgetCreateButton.addActionListener(e -> budgetCreatePresenter.onCreateButtonClicked());
    }

    @Override
    public void clearView() {
        clientTextField.setText("");
        cityComboBox.setSelectedIndex(-1);
        amountTextField.setText("");
        observationsTextField.setText("");
        subCategoryComboBox.setSelectedIndex(-1);
        clearTable();
    }

    @Override
    public String getBudgetClientName() {
        return clientTextField.getText();
    }

    @Override
    public String getBudgetDate() {
        return "21-03-2021";
    }

    @Override
    public String getBudgetClientType() {
        return "Particular";
    }

    @Override
    public int getBudgetNumber() {
        return 8888;
    }

    @Override
    public void clearTable() {
        for (int row = 0; row < budgetPreviewTable.getRowCount(); row++) {
            for (int col = 0; col < budgetPreviewTable.getColumnCount(); col++) {
                budgetPreviewTable.setValueAt("", row, col);
            }
        }
    }

    @Override
    public void setStringTableValueAt(int row, int col, String value) {
        budgetPreviewTable.setValueAt(value, row, col);
    }

    @Override
    public void setPresenter(StandardPresenter budgetCreatePresenter) {
        this.budgetCreatePresenter = (BudgetCreatePresenter) budgetCreatePresenter;
    }
}
