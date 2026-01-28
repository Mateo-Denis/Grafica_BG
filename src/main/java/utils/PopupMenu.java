package utils;

import presenters.client.BudgetHistoryPresenter;
import views.client.ClientCreateView;
import views.client.IClientSearchView;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

public class PopupMenu {
    private final IClientSearchView clientSearchView;
    private final ClientCreateView clientCreateView;

    public PopupMenu(IClientSearchView clientSearchView, ClientCreateView clientCreateView) {
        this.clientSearchView = clientSearchView;
        this.clientCreateView = clientCreateView;
    }

    public void createPopupMenu(JTable table) {
        // 1. Create the JPopupMenu and MenuItems
        final JPopupMenu popupMenu = new JPopupMenu();

        // Create menu items
        JMenuItem viewBudgetHistoryItem = new JMenuItem("Ver historial de presupuestos");
        JMenuItem editClientItem = new JMenuItem("Editar cliente");

        // Add ActionListeners to the menu items
        viewBudgetHistoryItem.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                clientSearchView.setBudgetHistoryTable();
            }
        });

        editClientItem.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                clientCreateView.loadClientToEdit(clientCreateView.getClientIDByName((String) table.getValueAt(selectedRow, 0)));
                clientCreateView.showView();
            }
        });

        // Add menu items to the popup menu
        popupMenu.add(viewBudgetHistoryItem);
        popupMenu.add(editClientItem);

        // 2. Add a MouseListener to the table
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                maybeShowPopup(e);
                if(table.getSelectedRowCount() > 0) {
                    // Enable or disable menu items based on whether the selected row is filled

                    // Use SwingUtilities.invokeLater to ensure the table selection is updated before checking the cell value
                    SwingUtilities.invokeLater(() -> {
                        int selectedRow = table.getSelectedRow();
                        boolean isRowFilled = table.getValueAt(selectedRow, 0) != null && !table.getValueAt(selectedRow, 0).toString().isEmpty();
                        viewBudgetHistoryItem.setEnabled(isRowFilled);
                    });
                } else {
                    viewBudgetHistoryItem.setEnabled(false);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                maybeShowPopup(e);
                if(table.getSelectedRowCount() > 0) {
                    // Enable or disable menu items based on whether the selected row is filled

                    // Use SwingUtilities.invokeLater to ensure the table selection is updated before checking the cell value
                    SwingUtilities.invokeLater(() -> {
                        int selectedRow = table.getSelectedRow();
                        boolean isRowFilled = table.getValueAt(selectedRow, 0) != null && !table.getValueAt(selectedRow, 0).toString().isEmpty();
                        viewBudgetHistoryItem.setEnabled(isRowFilled);
                    });
                } else {
                    viewBudgetHistoryItem.setEnabled(false);
                }
            }

            private void maybeShowPopup(MouseEvent e) {
                // Check if the event is the popup trigger (right-click on most systems)
                if (e.isPopupTrigger()) {
                    // Get the row index at the mouse pointer's location
                    int row = table.rowAtPoint(e.getPoint());
                    int column = table.columnAtPoint(e.getPoint());

                    // Select the row if it is not already selected
                    if (!table.isRowSelected(row)) {
                        table.changeSelection(row, column, false, false);
                    }

                    // Show the popup menu at the mouse's location
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }
}
