package views.categories;

import presenters.StandardPresenter;
import presenters.product.ProductCreatePresenter;
import views.ToggleableView;
import presenters.categories.CategoryCreatePresenter;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryCreateView extends ToggleableView implements ICategoryCreateView {
    private JTextField categoryNameField;
    private JButton addFieldButton;
    private JButton createCategoryButton;
    private JComboBox<String> fieldTypeComboBox;
    private JPanel previewPanel;
    private List<JComponent> fieldList;
    private CategoryCreatePresenter categoryCreatePresenter;

    public CategoryCreateView() {
        windowFrame = new JFrame("Crear Categoria");
        windowFrame.setTitle("Crear Categoría");
        windowFrame.setSize(500, 400);
        windowFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        windowFrame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2));

        categoryNameField = new JTextField();
        addFieldButton = new JButton("Añadir Campo");
        createCategoryButton = new JButton("Crear Categoría");
        fieldTypeComboBox = new JComboBox<>(new String[]{"Campo de Texto", "ComboBox"});

        inputPanel.add(new JLabel("Nombre de la Categoría:"));
        inputPanel.add(categoryNameField);
        inputPanel.add(new JLabel("Tipo de Campo:"));
        inputPanel.add(fieldTypeComboBox);
        inputPanel.add(addFieldButton);

        previewPanel = new JPanel();
        previewPanel.setLayout(new BoxLayout(previewPanel, BoxLayout.Y_AXIS));
        fieldList = new ArrayList<>();

        windowFrame.add(inputPanel, BorderLayout.NORTH);
        windowFrame.add(new JScrollPane(previewPanel), BorderLayout.CENTER);
        windowFrame.add(createCategoryButton, BorderLayout.SOUTH);
    }

    public JTextField getCategoryNameField() {
        return categoryNameField;
    }

    public JButton getAddFieldButton() {
        return addFieldButton;
    }

    public JButton getCreateCategoryButton() {
        return createCategoryButton;
    }

    public JComboBox<String> getFieldTypeComboBox() {
        return fieldTypeComboBox;
    }

    public JPanel getPreviewPanel() {
        return previewPanel;
    }

    public List<JComponent> getFieldList() {
        return fieldList;
    }

    public void addField(JComponent field) {
        fieldList.add(field);
        previewPanel.add(field);
        previewPanel.revalidate();
        previewPanel.repaint();
    }


    @Override
    protected void wrapContainer() {
    }

    @Override
    protected void initListeners() {
    }

    @Override
    public void clearView() {
    }

    @Override
    public void setPresenter(StandardPresenter categoryCreatePresenter) {
        this.categoryCreatePresenter = (CategoryCreatePresenter) categoryCreatePresenter;
    }
}