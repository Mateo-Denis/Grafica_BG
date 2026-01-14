package utils;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class WorkBudgetTablesCellRenderer extends JTextArea implements TableCellRenderer {

    public WorkBudgetTablesCellRenderer() {
        setLineWrap(true);
        setWrapStyleWord(true);
        setOpaque(true);

        // 🔥 FlatLaf FIX
        putClientProperty("FlatLaf.style", "border:0,0,0,0");

        // padding interno seguro
        setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {

        setText(value == null ? "" : value.toString());
        setFont(table.getFont());

        if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }

        // 🔥 ancho REAL usable en FlatLaf
        int width = table.getCellRect(row, column, false).width;

        // FlatLaf necesita 1px menos
        setSize(width - 1, Short.MAX_VALUE);

        int height = getPreferredSize().height;

        if (table.getRowHeight(row) != height) {
            table.setRowHeight(row, height);
        }

        return this;
    }
}
