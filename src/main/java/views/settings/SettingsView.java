package views.settings;

import org.javatuples.Pair;
import presenters.StandardPresenter;
import presenters.settings.SettingsPresenter;
import utils.MessageTypes;
import utils.databases.SettingsTableNames;
import views.ToggleableView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import static utils.WindowFormatter.relativeSizeAndCenter;

public class SettingsView extends ToggleableView implements ISettingsView {
    private JPanel containerPanel;
    private JPanel generalValuesPanel;
    private JPanel clothValuesPanel;
    private JTable clothValuesTable;
    private JTable clothesValuesTable;
    private JButton updateDataButton;
    private JTable cutValuesTable;
    private JTable plankLoweringValuesTable;
    private JTable serviceValuesTable;
    private JTable printingValuesTable;
    private JTable canvasValuesTable;
    private JTable materialsValuesTable;
    private JTable generalValuesTable;
    private JPanel vinylValuesPanel;
    private JPanel serviceValuesPanel;
    private JButton addDollarButton;
    private JButton removeDollarButton;
    private JButton addClothButton;
    private JButton removeClothButton;
    private JButton addServiceButton;
    private JButton removeServiceButton;
    private JButton addMaterialButton;
    private JButton removeMaterialButton;
    private JButton saveButton;
    private JTable profitValuesTable;
    private JTable measuresValuesTable;
    private SettingsPresenter settingsPresenter;


    public SettingsView() {
        windowFrame = new JFrame("Configuración");
        windowFrame.setContentPane(containerPanel);
        windowFrame.pack();
        windowFrame.setLocationRelativeTo(null);
        windowFrame.setIconImage(new ImageIcon("src/main/resources/BGLogo.png").getImage());
        wrapContainer();

        initTableListeners(generalValuesTable, SettingsTableNames.GENERAL);
        initTableListeners(clothValuesTable, SettingsTableNames.TELAS);
        initTableListeners(serviceValuesTable, SettingsTableNames.SERVICIOS);
        initTableListeners(materialsValuesTable, SettingsTableNames.MATERIALES);

        relativeSizeAndCenter(windowFrame, 0.7, 0.5);

        windowFrame.setResizable(false);

    }

    @Override
    public void setPresenter(StandardPresenter standardPresenter) {
        this.settingsPresenter = (SettingsPresenter) standardPresenter;
    }

    @Override
    protected void wrapContainer() {
        containerPanelWrapper = containerPanel;
    }

    @Override
    protected void initListeners() {
        addClothButton.addActionListener(e -> settingsPresenter.onAddButtonPressed(SettingsTableNames.TELAS));
        addDollarButton.addActionListener(e -> settingsPresenter.onAddButtonPressed(SettingsTableNames.GENERAL));
        addServiceButton.addActionListener(e -> settingsPresenter.onAddButtonPressed(SettingsTableNames.SERVICIOS));
        addMaterialButton.addActionListener(e -> settingsPresenter.onAddButtonPressed(SettingsTableNames.MATERIALES));

        removeClothButton.addActionListener(e -> settingsPresenter.onDeleteRowButtonPressed(SettingsTableNames.TELAS, clothValuesTable.getSelectedRow()));
        removeDollarButton.addActionListener(e -> settingsPresenter.onDeleteRowButtonPressed(SettingsTableNames.GENERAL, generalValuesTable.getSelectedRow()));
        removeServiceButton.addActionListener(e -> settingsPresenter.onDeleteRowButtonPressed(SettingsTableNames.SERVICIOS, serviceValuesTable.getSelectedRow()));
        removeMaterialButton.addActionListener(e -> settingsPresenter.onDeleteRowButtonPressed(SettingsTableNames.MATERIALES, materialsValuesTable.getSelectedRow()));

    }


    public void addEmptyRow(SettingsTableNames tableName) {
        JTable tableToAdd;
        switch (tableName) {
            case GENERAL -> tableToAdd = generalValuesTable;
            case TELAS -> tableToAdd = clothValuesTable;
            case SERVICIOS -> tableToAdd = serviceValuesTable;
            case MATERIALES -> tableToAdd = materialsValuesTable;
            default -> throw new IllegalArgumentException("Invalid table name: " + tableName);
        }

        DefaultTableModel model = (DefaultTableModel) tableToAdd.getModel();

        for (int i = 0; i < model.getRowCount(); i++) {
            Object cellValue = model.getValueAt(i, 0);
            if (cellValue == null || cellValue.toString().trim().isEmpty()) {
                // Ya hay una fila vacía, no agregamos otra
                focusAndEdit(tableToAdd, i);
                return;
            }
        }

        if (tableToAdd == generalValuesTable) {
            model.addRow(new Object[]{"", 0.0});
        } else {
            model.addRow(new Object[]{""});
        }

        autoResizeColumns(tableToAdd);

//        tableToAdd.editCellAt(model.getRowCount() - 1, 0);
        int lastRow = model.getRowCount() - 1;
        focusAndEdit(tableToAdd, lastRow);
    }

    private void focusAndEdit(JTable table, int row) {
        table.requestFocus(); // Asegura que la tabla tenga el foco
        table.changeSelection(row, 0, false, false); // Selecciona la celda
        table.editCellAt(row, 0); // Pone la celda en modo edición

        Component editor = table.getEditorComponent();
        if (editor != null) {
            editor.requestFocusInWindow(); // Enfoca el editor (ej: JTextField)
        }
    }


    public String removeRow(SettingsTableNames tableName, int rowIndex) {
        JTable tableToRemoveFrom;
        switch (tableName) {
            case GENERAL -> tableToRemoveFrom = generalValuesTable;
            case TELAS -> tableToRemoveFrom = clothValuesTable;
            case SERVICIOS -> tableToRemoveFrom = serviceValuesTable;
            case MATERIALES -> tableToRemoveFrom = materialsValuesTable;
            default -> throw new IllegalArgumentException("Invalid table name: " + tableName);
        }
        DefaultTableModel model = (DefaultTableModel) tableToRemoveFrom.getModel();
        String field = tableToRemoveFrom.getValueAt(rowIndex, 0).toString();
        if (rowIndex >= 0 && rowIndex < model.getRowCount()) {
            model.removeRow(rowIndex);
        }
        autoResizeColumns(tableToRemoveFrom);
        return field;
    }

    @Override
    public boolean confirmDeletion(String rowName) {
        int result = JOptionPane.showConfirmDialog(
                null,
                "¿Estás seguro que quieres borrar '" + rowName + "'?",
                "Confirmar borrado",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        return result == JOptionPane.YES_OPTION;
    }

    @Override
    public String getRowName(SettingsTableNames tableName, int rowIndex) {
        JTable tableToGetFrom;
        switch (tableName) {
            case GENERAL -> tableToGetFrom = generalValuesTable;
            case TELAS -> tableToGetFrom = clothValuesTable;
            case SERVICIOS -> tableToGetFrom = serviceValuesTable;
            case MATERIALES -> tableToGetFrom = materialsValuesTable;
            default -> throw new IllegalArgumentException("Invalid table name: " + tableName);
        }
        return tableToGetFrom.getValueAt(rowIndex, 0).toString();
    }


    private void initTableListeners(JTable table, SettingsTableNames name) {


        table.setDefaultEditor(Object.class, new DefaultCellEditor(new JTextField()) {
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value,
                                                         boolean isSelected, int row, int column) {
                JTextField editor = (JTextField) super.getTableCellEditorComponent(table, value, isSelected, row, column);

                // Remove existing action listeners to prevent duplicates
                for (ActionListener al : editor.getActionListeners()) {
                    editor.removeActionListener(al);
                }

                editor.addActionListener(e -> {
                    stopCellEditing(); // First commit the edit

                    // Then call your custom logic
                    settingsPresenter.onSaveButtonPressed(name);

                    // Move to the next row if desired
                    if (row < table.getRowCount() - 1) {
                        table.changeSelection(row + 1, column, false, false);
                    }
                });

                return editor;
            }
        });

        table.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "enterPressed");

        table.getActionMap().put("enterPressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (table.isEditing()) {
                    table.getCellEditor().stopCellEditing(); // Confirmamos edición

                    int row = table.getSelectedRow();
                    int column = table.getSelectedColumn();

                    // Mueve a la siguiente fila si es posible
                    if (row < table.getRowCount() - 1) {
                        table.changeSelection(row + 1, column, false, false);
                    }

                    settingsPresenter.onSaveButtonPressed(name); // Guardar cambios
                } else {
                    settingsPresenter.onSaveButtonPressed(name);
                }
            }
        });


        // Capturar tecla ESC para salir de la edición y deseleccionar la tabla
        table.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cancelEdit");

        table.getActionMap().put("cancelEdit", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Cancela la edición si está activa
                if (table.isEditing()) {
                    table.getCellEditor().cancelCellEditing();
                }
                // Mostrar mensaje de confirmación
                int option = JOptionPane.showOptionDialog(
                        table,
                        "¿Quieres guardar los cambios?",
                        "Confirmación",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new Object[]{"SI", "NO", "SALIR"},
                        "SI"
                );

                switch (option) {
                    case JOptionPane.YES_OPTION:
                        // Guardar cambios
                        settingsPresenter.onSaveButtonPressed(name);
                        break;
                    case JOptionPane.NO_OPTION:
                        // Continuar editando
                        table.requestFocusInWindow();
                        break;
                    case JOptionPane.CANCEL_OPTION:
                        // Salir del programa
                        Window window = SwingUtilities.getWindowAncestor(table);
                        if (window != null) {
                            window.dispose();
                        }
                        break;
                }
            }
        });

        //IF THE TABLE IS EDITING, STOP EDITING WHEN THE MOUSE IS PRESSED OUTSIDE THE TABLE
        containerPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Verificar que fue botón izquierdo
                if (SwingUtilities.isLeftMouseButton(e)) {
                    // Quitar foco de la tabla
                    if (table.isEditing()) {
                        table.getCellEditor().stopCellEditing(); // guarda cambios antes de quitar selección
                    }
                    table.clearSelection(); // deselecciona
                }
            }
        });


        table.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                // Stop editing if focus is lost
                if (table.isEditing()) {
                    table.getCellEditor().stopCellEditing();
                }
            }
        });

        // Add a listener to commit changes on selection change (cell navigation)
        table.getSelectionModel().addListSelectionListener(e -> {
            if (table.isEditing()) {
                table.getCellEditor().stopCellEditing();
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JTable table = (JTable) e.getSource();
                int row = table.rowAtPoint(e.getPoint());
                int column = table.columnAtPoint(e.getPoint());

                // Check if the click is on a different cell
                if (!table.isEditing() || table.getEditingRow() != row || table.getEditingColumn() != column) {
                    if (table.isEditing()) {
                        table.getCellEditor().stopCellEditing();
                    }

                    table.editCellAt(row, column);
                }
            }
        });

        table.setSelectionBackground(table.getBackground());
        table.setSelectionForeground(table.getForeground());
        table.setShowGrid(true);
        table.setGridColor(Color.LIGHT_GRAY);
        // Al final de initTableListeners(...)
        TableCellEditor customEditor = table.getDefaultEditor(Object.class); // Ya contiene tu lógica de Enter

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellEditor(customEditor);
        }
    }

    @Override
    public void clearView() {

    }

    public void autoResizeColumns(JTable table) {
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 0;
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width, width);
            }
            table.getColumnModel().getColumn(column).setPreferredWidth(width + 10); // Añadir un pequeño margen
        }
    }

    @Override
    public void setModularTable(SettingsTableNames tableName, ArrayList<Pair<String, Double>> values, ArrayList<String> oneFieldValues) {
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Nombre", "Valor"}, 0);

        DefaultTableModel oneFieldModel = new DefaultTableModel(new Object[]{"Nombre"}, 0);

        JTable table = getModularTable(tableName);

        if (tableName.equals(SettingsTableNames.GENERAL)) {
            table.setModel(model);
            // Convert the ArrayList<Pair<String, Double>> into table rows
            for (Pair<String, Double> pair : values) {
                model.addRow(new Object[]{pair.getValue0(), pair.getValue1()});
            }
        } else {
            table.setModel(oneFieldModel);
            // Convert the ArrayList<String> into table rows
            for (String value : oneFieldValues) {
                oneFieldModel.addRow(new Object[]{value});
            }
        }

        autoResizeColumns(table);
    }

    @Override
    public ArrayList<Pair<String, Double>> generalTableToArrayList() throws NumberFormatException {
        ArrayList<Pair<String, Double>> arrayList = new ArrayList<>();
        Object obj;
        JTable table = getModularTable(SettingsTableNames.GENERAL);
        for (int i = 0; i < table.getRowCount(); i++) {
            obj = table.getValueAt(i, 1);
            arrayList.add(new Pair<>(table.getValueAt(i, 0).toString(), Double.parseDouble(obj.toString())));
        }
        return arrayList;
    }

    @Override
    public ArrayList<String> normalTableToArrayList(SettingsTableNames tableName) {
        ArrayList<String> arrayList = new ArrayList<>();
        JTable table = getModularTable(tableName);
        for (int i = 0; i < table.getRowCount(); i++) {
            arrayList.add(table.getValueAt(i, 0).toString());
        }
        return arrayList;
    }

    @Override
    public JTable getModularTable(SettingsTableNames table) {
        return switch (table) {
            case GENERAL -> generalValuesTable;
            case TELAS -> clothValuesTable;
            case SERVICIOS -> serviceValuesTable;
            case MATERIALES -> materialsValuesTable;
        };
    }

    @Override
    public void showDetailedMessage(MessageTypes messageType, SettingsTableNames tableName) {
        JOptionPane.showMessageDialog(containerPanelWrapper, messageType.getMessage() + tableName.getName() + "."
                , messageType.getTitle()
                , messageType.getMessageType());
    }

}
