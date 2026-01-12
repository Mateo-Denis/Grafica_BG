package utils;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;

public class TableSettings {
	public static void adjustColumnWidthsToHeader(JTable table) {
		JTableHeader header = table.getTableHeader();
		FontMetrics fm = header.getFontMetrics(header.getFont());

		TableColumnModel columnModel = table.getColumnModel();

		for (int i = 0; i < columnModel.getColumnCount(); i++) {
			TableColumn column = columnModel.getColumn(i);
			String headerText = column.getHeaderValue().toString();

			int textWidth = fm.stringWidth(headerText);
			int padding = 20; // margen visual

			column.setPreferredWidth(textWidth + padding);
		}

		table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
	}

}
