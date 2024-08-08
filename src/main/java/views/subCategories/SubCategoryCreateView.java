package views.subCategories;

import views.ToggleableView;
import javax.swing.*;
import java.util.List;
import presenters.StandardPresenter;
import presenters.subCategory.SubCategoryCreatePresenter;
import java.util.List;

public class SubCategoryCreateView extends ToggleableView implements ISubCategoryCreateView {
    private JPanel containerPanel;
    private JPanel nameContainer;
    private JPanel categoryContainer;
    private JPanel buttonsContainer;
    private JButton createButton;
    private JComboBox<String> categoryComboBox;
    private JTextField nameField;
    private SubCategoryCreatePresenter subCategoryCreatePresenter;

    public SubCategoryCreateView() {
        windowFrame = new JFrame("Crear Sub Categoria");
        windowFrame.setContentPane(containerPanel);
        windowFrame.pack();
        windowFrame.setLocationRelativeTo(null);
        windowFrame.setIconImage(new ImageIcon("src/main/resources/BGLogo.png").getImage());
    }

    @Override
    public String getSubCategoryName() {
        return nameField.getText();
    }

    @Override
    public String getCategoryAssociated() {
        return (String) categoryComboBox.getSelectedItem();
    }

    @Override
    public void setCategorias(List<String> categorias) {
        for (String categoria : categorias) {
            categoryComboBox.addItem(categoria);
        }
    }

    @Override
    public JComboBox<String> getComboBoxCategorias() {
        return categoryComboBox;
    }

    @Override
    protected void wrapContainer() {
        containerPanelWrapper = containerPanel;
    }

    @Override
    protected void initListeners() {
        createButton.addActionListener(e -> subCategoryCreatePresenter.onCreateButtonClicked());
    }

    @Override
    public void clearView() {
        nameField.setText("");
        categoryComboBox.setSelectedIndex(-1);
    }

    @Override
    public void setPresenter(StandardPresenter subCategoryCreatePresenter) {
         this.subCategoryCreatePresenter = (SubCategoryCreatePresenter) subCategoryCreatePresenter;
    }

    public JButton getCreateButton() {
        return createButton;
    }
}
