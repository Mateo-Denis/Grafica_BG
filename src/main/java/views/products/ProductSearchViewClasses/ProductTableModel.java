package views.products.ProductSearchViewClasses;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionListener;
import java.util.List;
import models.ProductSearchModel;

public class ProductTableModel extends AbstractTableModel {
    private String[] columnNames = {"ID", "Name", "Description", "Price"};
    private List<ProductSearchModel> products;

    public void setProducts(List<ProductSearchModel> products) {
        this.products = products;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return products == null ? 0 : products.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ProductSearchModel product = products.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return product.getID();
            case 1:
                return product.getName();
            case 2:
                return product.getDescription();
            case 3:
                return product.getPrice();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}
