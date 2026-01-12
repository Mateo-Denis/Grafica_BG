package views.workbudget;

import presenters.StandardPresenter;
import presenters.budget.BudgetSearchPresenter;
import presenters.workbudget.WorkBudgetSearchPresenter;
import views.ToggleableView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

import static utils.TableSettings.adjustColumnWidthsToHeader;

public class WorkBudgetSearchView extends ToggleableView implements IWorkBudgetSearchView {
	private JPanel budgetSearchContainer;
	private JPanel searchFieldContainer;
	private JTextField searchField;
	private JPanel searchButtonsContainer;
	private JButton searchButton;
	private JButton cleanTableButton;
	private JPanel budgetResultContainer;
	private JScrollPane budgetResultScrollPanel;
	private JTable budgetResultTable;
	private JPanel budgetListButtonsContainer;
	private JButton pdfButton;
	private JButton deleteButton;
	private JButton modifyButton;
	private JPanel containerPanel;
	private static Logger LOGGER;

	private WorkBudgetSearchPresenter workBudgetSearchPresenter;

	public WorkBudgetSearchView() {
		windowFrame = new JFrame("Crear Presupuesto de Trabajo");
		windowFrame.setContentPane(containerPanel);
		windowFrame.pack();
		windowFrame.setLocationRelativeTo(null);
		windowFrame.setIconImage(new ImageIcon("src/main/resources/BGLogo.png").getImage());
		windowFrame.setResizable(true);

		cambiarTamanioFuente(containerPanel, 14);
		windowFrame.setSize(550, 588);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screenSize.width - windowFrame.getWidth()) / 2;    // Centered horizontally
		int y = (screenSize.height - windowFrame.getHeight()) / 2;  // Centered vertically

		// Set the location of the frame
		windowFrame.setLocation(x, y);
	}

	@Override
	public void start() {
		super.start();
		setBudgetTableModel();
	}

	private void setBudgetTableModel() {
		DefaultTableModel tableModel = new DefaultTableModel(
				new Object[]{"ID de cliente", "Nombre del Cliente", "Fecha del presupuesto", "Número de Presupuesto", "Precio final"}, 200
		) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false; // Todas las celdas no serán editables
			}
		};
		budgetResultTable.setModel(tableModel);
		budgetResultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		adjustColumnWidthsToHeader(budgetResultTable);
	}

	@Override
	protected void wrapContainer() {
		containerPanelWrapper = containerPanel;
	}

	@Override
	protected void initListeners() {
		searchButton.addActionListener(e-> workBudgetSearchPresenter.onSearchButtonClicked());
		modifyButton.addActionListener(e-> workBudgetSearchPresenter.onModifyButtonClicked());
		deleteButton.addActionListener(e-> workBudgetSearchPresenter.onDeleteButtonClicked());
		cleanTableButton.addActionListener(e -> clearTable());
		budgetResultTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				if (evt.getClickCount() == 2) {
					workBudgetSearchPresenter.onDoubleClickBudget();
				}
			}
		});
	}

	@Override
	public void clearView() {
		searchField.setText("");
		clearTable();

	}

	public String getSearchText() {
		return searchField.getText();
	}

	public void clearTable() {
		for (int row = 0; row < budgetResultTable.getRowCount(); row++) {
			for (int col = 0; col < budgetResultTable.getColumnCount(); col++) {
				budgetResultTable.setValueAt("", row, col);
			}
		}
		budgetResultTable.clearSelection();
	}

	public boolean isTableSelected() {
		int row = budgetResultTable.getSelectedRow();
		if( row == -1 ){
			return false;
		}else return (budgetResultTable.getModel().getValueAt(row, 0) != null) && !budgetResultTable.getModel().getValueAt(row, 2).equals("");
	}


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

	public String getSelectedBudgetClientName(){
		int selectedRow = getSelectedTableRow();
		if (selectedRow != -1) {
			Object clientNameObj = budgetResultTable.getValueAt(selectedRow, 1);
			if (clientNameObj != null) {
				try {
					return (String) clientNameObj;
				} catch (ArrayIndexOutOfBoundsException e) {
					LOGGER.log(null, "ERROR IN METHOD 'getSelectedBudgetClientName', CLASS->BudgetSearchPresenter");
				}
			}
		}
		return "";
	}

	public String getSelectedBudgetDate(){
		int selectedRow = getSelectedTableRow();
		if (selectedRow != -1) {
			Object budgetDateObj = budgetResultTable.getValueAt(selectedRow, 2);
			if (budgetDateObj != null) {
				try {
					return (String) budgetDateObj;
				} catch (ArrayIndexOutOfBoundsException e) {
					LOGGER.log(null, "ERROR IN METHOD 'getSelectedBudgetDate', CLASS->BudgetSearchPresenter");
				}
			}
		}
		return "";
	}

	public int getSelectedTableRow() {
		return budgetResultTable.getSelectedRow();
	}


	@Override
	public void setPresenter(StandardPresenter workBudgetSearchPresenter) {
		this.workBudgetSearchPresenter = (WorkBudgetSearchPresenter) workBudgetSearchPresenter;
	}

	public void setStringTableValueAt(int row, int col, String value) {
		budgetResultTable.setValueAt(value, row, col);
	}
}
