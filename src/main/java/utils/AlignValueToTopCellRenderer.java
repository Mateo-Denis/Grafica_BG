package utils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class AlignValueToTopCellRenderer extends DefaultTableCellRenderer {

    public AlignValueToTopCellRenderer() {
        setVerticalAlignment(SwingConstants.TOP);

        // Padding para que quede alineado con multilinea
        setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {

        JLabel label = (JLabel) super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);

        // FlatLaf fix
        label.setVerticalAlignment(SwingConstants.TOP);

        return label;
    }
}
