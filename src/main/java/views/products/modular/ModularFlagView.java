package views.products.modular;

import lombok.Getter;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import presenters.product.ProductCreatePresenter;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static utils.databases.SettingsTableNames.*;
import static utils.databases.SettingsTableNames.SERVICIOS;

public class ModularFlagView extends JPanel implements IModularCategoryView {
    private JPanel containerPanel;
    private JComboBox clothComboBox;
    private JPanel clothSelectionContainer;
    private JPanel clothMeasuresContainer;
    private JPanel heightContainer;
    private JPanel widthContainer;
    private JTextField heightTextField;
    private JTextField widthTextField;
    private JLabel multiplyMeasuresLabel;
    private JLabel multiplyMeasuresAndPriceLabel;
    private JPanel metersPriceContainer;
    private JTextField metersPriceTextField;
    private JLabel measuresPriceEqualsLabel;
    private JPanel clothFinalPriceContainer;
    private JTextField finalPriceTextField;
    private JPanel plankLoweringContainer;
    private JPanel plankLoweringAmountContainer;
    private JPanel plankLoweringPriceContainer;
    private JPanel plankLoweringFinalPriceContainer;
    private JLabel plankLoweringMultiplyLabel;
    private JLabel plankLoweringEqualsLabel;
    private JPanel seamstressContainer;
    private JTextField seamstressPriceTextField;
    private JPanel printingContainer;
    private JPanel printingMetersAmountContainer;
    private JPanel printingMetersPriceContainer;
    private JPanel printingFinalPriceContainer;
    private JTextField printingMetersAmountTextField;
    private JTextField printingMetersPriceTextField;
    private JTextField printingMetersFinalPriceTextField;
    private JPanel centerSideComponentsContainer;
    private JPanel rightSideComponentsContainer;
    private JPanel leftSideComponentsContainer;
    private JLabel profitMultiplyLabel;
    private JPanel profitContainer;
    private JTextField profitTextField;
    private JLabel flagFinalPriceEqualsLabel;
    private JPanel finalFlagPriceContainer;
    private JTextField flagFinalPriceTextField;
    private JTextField plankLoweringAmountTextField;
    private JTextField plankLoweringPriceTextField;
    private JTextField plankLoweringFinalPriceTextField;
    private JLabel printingMetersMultiplyLabel;
    private JLabel printingMetersEqualsLabel;
    private JComboBox sizeComboBox;
    @Getter
    private ArrayList<String> radioValues = new ArrayList<>();
    @Getter
    private Map<String, String> comboBoxValues = new HashMap<>();
    @Getter
    private Map<String, String> textFieldValues = new HashMap<>();
    private ProductCreatePresenter presenter;
    private double profit;
    private double printingMetersPrice;
    private double plankLoweringPrice;
    private double clothSqrMetersPrice;
    private double seamstressPrice;


    public ModularFlagView(ProductCreatePresenter presenter) {
        TitledBorder border = BorderFactory.createTitledBorder("<html>Costo de cada bajada<br>de plancha</html>");
        plankLoweringPriceContainer.setBorder(border);
        this.presenter = presenter;
        initListeners();
    }

    @Override
    public JPanel getContainerPanel() {
        return containerPanel;
    }

    @Override
    public void initListeners() {

    }

    @Override
    public void loadComboBoxValues() {
        ArrayList<Pair<String, Double>> clothList = presenter.getTableAsArrayList(TELAS);
        for (Pair<String, Double> pair : clothList) {
            clothComboBox.addItem(pair.getValue0());
        }

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
        profit = presenter.getIndividualPrice(GANANCIAS, "Banderas");
        printingMetersPrice = presenter.getIndividualPrice(IMPRESIONES, "Sublimación");
        plankLoweringPrice = presenter.getIndividualPrice(BAJADA_PLANCHA, "En bandera");
        clothSqrMetersPrice = presenter.getIndividualPrice(TELAS, getFlagComboBoxSelection());
        seamstressPrice = presenter.getIndividualPrice(SERVICIOS, "Costurera bandera");


        profitTextField.setText(String.valueOf(profit));
        printingMetersPriceTextField.setText(String.valueOf(printingMetersPrice));
        metersPriceTextField.setText(String.valueOf(clothSqrMetersPrice));
        plankLoweringPriceTextField.setText(String.valueOf(plankLoweringPrice));
        seamstressPriceTextField.setText(String.valueOf(seamstressPrice));

    }

    private String getFlagComboBoxSelection() {
        return (String) clothComboBox.getSelectedItem();
    }

    private String getSizeComboBoxSelection() {
        return (String) sizeComboBox.getSelectedItem();
    }
}
