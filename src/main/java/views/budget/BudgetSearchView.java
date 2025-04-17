package views.budget;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import PdfFormater.Row;
import presenters.StandardPresenter;
import presenters.budget.BudgetListPresenter;
import presenters.budget.BudgetModifyPresenter;
import presenters.budget.BudgetSearchPresenter;
import views.ToggleableView;

import java.awt.*;
import java.util.ArrayList;
import java.util.logging.Logger;


public class BudgetSearchView extends ToggleableView implements IBudgetSearchView {
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
    private final BudgetListPresenter budgetListPresenter;
    private final BudgetModifyPresenter budgetModifyPresenter;
    private static Logger LOGGER;

    public BudgetSearchView(BudgetListPresenter budgetListPresenter, BudgetModifyPresenter budgetModifyPresenter) {
        windowFrame = new JFrame("Buscar Presupuestos");
        windowFrame.setContentPane(containerPanel);
        windowFrame.pack();
        windowFrame.setLocationRelativeTo(null);
        windowFrame.setIconImage(new ImageIcon("src/main/resources/BGLogo.png").getImage());

        this.budgetListPresenter = budgetListPresenter;
        this.budgetModifyPresenter = budgetModifyPresenter;

        cambiarTamanioFuente(containerPanel, 14);
        windowFrame.setSize(850, 580);
        windowFrame.setResizable(false);

        //initListeners();
    }


    @Override
    public void start() {
        super.start();
        SetBudgetTableModel();
    }

    public void SetBudgetTableModel() {
        DefaultTableModel tableModel = new DefaultTableModel(
                new Object[]{"Nombre del Cliente", "Fecha del presupuesto", "Cliente / Particular", "Numero de Presupuesto"}, 200
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Todas las celdas no serán editables
            }
        };
        budgetResultTable.setModel(tableModel);
        budgetResultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    @Override
    public void setPresenter(StandardPresenter budgetSearchPresenter) {
        this.budgetSearchPresenter = (BudgetSearchPresenter) budgetSearchPresenter;
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
        pdfButton.addActionListener(e -> budgetSearchPresenter.onPDFButtonClicked(getSelectedBudgetNumber()));
        deleteButton.addActionListener(e -> budgetSearchPresenter.onDeleteButtonClicked());
        modifyButton.addActionListener(e -> budgetModifyPresenter.onModifySearchViewButtonClicked(isTheRowFilled(), getSelectedTableRow(), getSelectedBudgetNumber()));
    }

    public void setStringTableValueAt(int row, int col, String value) {
        budgetResultTable.setValueAt(value, row, col);
    }

    @Override
    public void clearView() {
        clearTable();
        searchField.setText("");
    }

    public String getSearchText() {
        return searchField.getText();
    }

    public ArrayList<Row> getBudgetContent() {
        return new ArrayList<>();
    }


    public void clearTable() {
        for (int row = 0; row < budgetResultTable.getRowCount(); row++) {
            for (int col = 0; col < budgetResultTable.getColumnCount(); col++) {
                budgetResultTable.setValueAt("", row, col);
            }
        }
        budgetResultTable.clearSelection();
    }

    @Override
    public String getSelectedBudgetName() {
        String budgetName = "";
        int selectedRow = getSelectedTableRow();
        if (selectedRow != -1) {
            budgetName = (String) budgetResultTable.getValueAt(getSelectedTableRow(), 0);
        }
        return budgetName;
    }

    public String getSelectedBudgetClientType() {
        String clientType = "";
        int selectedRow = getSelectedTableRow();
        if (selectedRow != -1) {
            clientType = (String) budgetResultTable.getValueAt(getSelectedTableRow(), 2);
        }
        return clientType;
    }

    @Override
    public int getSelectedBudgetNumber() {
        int budgetNumber = 0;
        int selectedRow = getSelectedTableRow();
        if (selectedRow != -1) {
            Object budgetNumberObj = budgetResultTable.getValueAt(selectedRow, 3);
            if (budgetNumberObj != null) {
                try {
                    String budgetNumberStr = (String) budgetNumberObj;
                    if (!budgetNumberStr.isEmpty()) {
                        budgetNumber = Integer.parseInt(budgetNumberStr);
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    LOGGER.log(null, "ERROR IN METHOD 'getSelectedBudgetNumber', CLASS->BudgetSearchPresenter");
                }
            }
        }
        return budgetNumber;
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

    public ArrayList<Integer> getMultipleSelectedBudgetNumbers() {
        ArrayList<Integer> budgetNumbers = new ArrayList<>();
        int[] selectedRows = budgetResultTable.getSelectedRows();
        for (int row : selectedRows) {
            int budgetNumber = Integer.parseInt((String) budgetResultTable.getValueAt(row, 3));
            budgetNumbers.add(budgetNumber);
        }
        return budgetNumbers;
    }

    @Override
    public int getSelectedTableRow() {
        return budgetResultTable.getSelectedRow();
    }

    public boolean isTheRowFilled() {
        boolean isFilled;
        int selectedRow = getSelectedTableRow();

        if (selectedRow != -1) {
            if (budgetResultTable.getValueAt(selectedRow, 0) != null) {
                isFilled = !getStringValueAt(getSelectedTableRow(), 0).isEmpty();
            } else {
                isFilled = false;
            }
        } else {
            isFilled = false;
        }

        return isFilled;
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

    @Override
    public JButton getModifyButton() {
        return modifyButton;
    }

}


