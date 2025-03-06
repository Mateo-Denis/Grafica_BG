package utils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class MultiLineHeaderRenderer extends DefaultTableCellRenderer {
    public MultiLineHeaderRenderer() {
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JTextArea textArea = new JTextArea(value.toString());
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setOpaque(true);
        textArea.setBackground(UIManager.getColor("TableHeader.background"));
        textArea.setForeground(UIManager.getColor("TableHeader.foreground"));
        textArea.setFont(UIManager.getFont("TableHeader.font").deriveFont(14f));
        textArea.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        return textArea;
    }

    public static void applyToTable(JTable table, boolean increaseHeight) {
        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new MultiLineHeaderRenderer());
        if(increaseHeight)
        {
            header.setPreferredSize(new Dimension(header.getWidth(), 50)); // Increase header height to 50
        }
    }
}