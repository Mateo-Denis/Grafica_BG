package views.products.modular;

import org.javatuples.Triplet;
import presenters.product.ProductCreatePresenter;
import presenters.product.ProductPresenter;
import presenters.product.ProductSearchPresenter;
import utils.Attribute;
import utils.MessageTypes;
import utils.Product;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static utils.databases.SettingsTableNames.*;

public class ModularLinearPrintingView extends JPanel implements IModularCategoryView {
    private JPanel containerPanel;
    private JPanel leftSideComponentsContainer;
    private JPanel centerSideComponentsContainer;
    private JPanel rightSideComponentsContainer;
    private JPanel paperMeterPriceContainer;
    private JPanel inkByMeterPriceContainer;
    private JTextField paperMeterPriceTextField;
    private JTextField inkByMeterPriceTextField;
    private JLabel profitMultiplyLabel;
    private JLabel linearPrintingFinalPriceEqualsLabel;
    private JPanel profitContainer;
    private JPanel finalPriceContainer;
    private JTextField profitTextField;
    private JTextField finalPriceTextField;
    private JLabel profitLabel;
    private JPanel IVAContainer;
    private JLabel ivaSumLabel;
    private JLabel ivaPercentLabel;
    private JPanel IVAComboboxContainer;
    private JComboBox IVAcombobox;
    private JPanel particularAddContainer;
    private JLabel particularAddSumLabel;
    private JPanel ParticularAddTextFieldContainer;
    private JTextField particularAddTextField;
    private JLabel particularAddPercentLabel;
    private double paperMeterPrice;
    private double inkByMeterPrice;
    private double profit;
    private boolean initialization;
    private final ProductCreatePresenter createPresenter;
    private final ProductSearchPresenter searchPresenter;
    private final ProductPresenter presenter;

    public ModularLinearPrintingView(boolean isCreate, ProductPresenter presenter) {
        if (isCreate) {
            this.createPresenter = (ProductCreatePresenter) presenter;
            this.searchPresenter = null;
        } else {
            this.createPresenter = null;
            this.searchPresenter = (ProductSearchPresenter) presenter;
        }

        this.presenter = presenter;
        initListeners();
        adjustPanels();
    }

    private void adjustPanels() {

        ArrayList<JPanel> panels = new ArrayList<>();
        panels.add(paperMeterPriceContainer);
        panels.add(inkByMeterPriceContainer);
        panels.add(profitContainer);
        panels.add(finalPriceContainer);
        for (JPanel panel : panels) {

            TitledBorder border = (TitledBorder) panel.getBorder();

            FontMetrics fm = panel.getFontMetrics(border.getTitleFont());
            int titleWidth = fm.stringWidth(border.getTitle());
            panel.setPreferredSize(new Dimension(titleWidth + 20, 50));
        }
    }

    @Override
    public JPanel getContainerPanel() {
        return containerPanel;
    }

    @Override
    public void initListeners() {
        ArrayList<JTextField> textFields = new ArrayList<>();

        textFields.add(paperMeterPriceTextField);
        textFields.add(inkByMeterPriceTextField);
        textFields.add(profitTextField);

        for (JTextField textField : textFields) {
            textField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    calculateDependantPrices();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    calculateDependantPrices();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    calculateDependantPrices();
                }
            });
        }
    }

    @Override
    public void calculateDependantPrices() {
        try {
            float paperMeterPrice = paperMeterPriceTextField.getText().isEmpty() ? 0
                    : Float.parseFloat(paperMeterPriceTextField.getText());
            float inkByMeterPrice = inkByMeterPriceTextField.getText().isEmpty() ? 0
                    : Float.parseFloat(inkByMeterPriceTextField.getText());
            float profit = profitTextField.getText().isEmpty() ? 0 : Float.parseFloat(profitTextField.getText());

            float finalPrice = paperMeterPrice + inkByMeterPrice + (profit / 100);
            finalPriceTextField.setText(String.valueOf(finalPrice));

        } catch (NumberFormatException | NullPointerException e) {
            showMessage(MessageTypes.FLOAT_PARSING_ERROR, containerPanel);
        }
    }

    @Override
    public Map<String, String> getComboBoxValues() {
        return Map.of();
    }

    @Override
    public Map<String, String> getTextFieldValues() {
        return Map.of();
    }

    @Override
    public ArrayList<String> getRadioValues() {
        return null;
    }

    @Override
    public void loadComboBoxValues() {

    }

    @Override
    public List<Triplet<String, String, Double>> getModularPrices() {
        return List.of();
    }

    @Override
    public void unlockTextFields() {

    }

    @Override
    public void blockTextFields() {

    }

    @Override
    public void setPriceTextFields() {
        paperMeterPrice = presenter.getIndividualPrice(IMPRESIONES, "Metro de papel");
        inkByMeterPrice = presenter.getIndividualPrice(IMPRESIONES, "Metro de tinta");
        profit = presenter.getIndividualPrice(GANANCIAS, "Impresión lineal");

        profitTextField.setText(String.valueOf(profit));
        paperMeterPriceTextField.setText(String.valueOf(paperMeterPrice));
        inkByMeterPriceTextField.setText(String.valueOf(inkByMeterPrice));
    }

    @Override
    public ArrayList<Attribute> getAttributes() {
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("T1A", paperMeterPriceTextField.getText()));
        attributes.add(new Attribute("T2A", inkByMeterPriceTextField.getText()));
        attributes.add(new Attribute("GANANCIA", profitTextField.getText()));
        return attributes;
    }

    @Override
    public void comboBoxListenerSet(ItemListener listener) {

    }

    @Override
    public void setSearchTextFields(Product product) {
        //
    }

}
