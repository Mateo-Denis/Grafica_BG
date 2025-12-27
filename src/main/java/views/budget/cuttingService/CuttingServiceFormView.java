package views.budget.cuttingService;

import presenters.StandardPresenter;
import presenters.budget.CuttingServiceFormPresenter;
import utils.MessageTypes;
import utils.TextUtils;
import views.ToggleableView;

import javax.swing.*;
import java.util.ArrayList;

import static utils.TextUtils.truncateAndRound;

public class CuttingServiceFormView extends ToggleableView implements ICuttingServiceFormView {
    private JPanel containerPanel;
    private JPanel materialCostContainer;
    private JPanel linealMetersContainer;
    private JLabel materialCostLabel;
    private JTextField materialCostTextField;
    private JTextField linealMetersTextField;
    private JLabel linealMetersLabel;
    private JPanel subTotalContainer;
    private JLabel subTotalLabel;
    private JTextField subTotalTextField;
    private JPanel profitContainer;
    private JLabel profitLabel;
    private JTextField profitTextField;
    private JLabel totalLabel;
    private JTextArea finalTextArea;
    private JButton addCuttingServiceButton;
    private JPanel totalContainer;
    private JPanel amountContainer;
    private JTextField amountTextField;
    private JLabel amountLabel;
    private JLabel descriptionLabel;
    private JTextArea descriptionTextArea;
    private CuttingServiceFormPresenter cuttingServiceFormPresenter;

    public CuttingServiceFormView() {
        windowFrame = new JFrame("Crear servicio de corte");
        windowFrame.setContentPane(containerPanel);
        windowFrame.pack();
        windowFrame.setLocationRelativeTo(null);
        addWindowBorders();
    }

    @Override
    protected void wrapContainer() {
        containerPanelWrapper = containerPanel;
    }

    @Override
    public void start() {
        super.start();
        amountTextField.setText("1");
        linealMetersTextField.setText("1");
        profitTextField.setText("100");
        subTotalTextField.setEnabled(false);
        finalTextArea.setEnabled(false);
    }

    @Override
    protected void initListeners() {
        // Dependant prices listeners
        ArrayList<JTextField> textFields = new ArrayList<>();
        textFields.add(materialCostTextField);
        textFields.add(linealMetersTextField);
        textFields.add(profitTextField);

        for (JTextField textField : textFields) {
            if(cuttingServiceFormPresenter != null) {
                cuttingServiceFormPresenter.clearView();
                textField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                    public void changedUpdate(javax.swing.event.DocumentEvent e) {
                        calculateDependantPrices();
                    }

                    public void removeUpdate(javax.swing.event.DocumentEvent e) {
                        calculateDependantPrices();
                    }

                    public void insertUpdate(javax.swing.event.DocumentEvent e) {
                        calculateDependantPrices();
                    }
                });
            }
        }

        // Dependant prices listeners

        addCuttingServiceButton.addActionListener(e -> cuttingServiceFormPresenter.onAddCuttingServiceButtonClicked());
    }

    public void calculateDependantPrices() {
        try {
            double materialCost = materialCostTextField.getText().isEmpty() ? 0.0 : Double.parseDouble(materialCostTextField.getText());
            double linealMeters = linealMetersTextField.getText().isEmpty() ? 1.0 : Double.parseDouble(linealMetersTextField.getText());
            double profit = profitTextField.getText().isEmpty() ? 0.0 : Double.parseDouble(profitTextField.getText());
            int amount = amountTextField.getText().isEmpty() ? 1 : Integer.parseInt(amountTextField.getText());

            double subTotal = (materialCost * linealMeters) * amount;
            subTotalTextField.setText(String.format("%.2f", subTotal));

            String total = truncateAndRound(String.valueOf(subTotal + (subTotal * (profit / 100))));
            finalTextArea.setText(String.format("%.2f", Double.parseDouble(total)));
        } catch (NumberFormatException | NullPointerException e) {
            showMessage(MessageTypes.FLOAT_PARSING_ERROR, containerPanel);
        }
    }

    @Override
    public void clearView() {
        materialCostTextField.setText("");
        linealMetersTextField.setText("");
        profitTextField.setText("");
        subTotalTextField.setText("");
        finalTextArea.setText("");
        amountTextField.setText("");
        descriptionTextArea.setText("");
    }

    @Override
    public void setPresenter(StandardPresenter cuttingServiceFormPresenter) {
        this.cuttingServiceFormPresenter = (CuttingServiceFormPresenter) cuttingServiceFormPresenter;
    }

    // Adds padding around the window content and black borders
    private void addWindowBorders() {
        int padding = 10;
        containerPanel.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
        windowFrame.getRootPane().setBorder(BorderFactory.createLineBorder(java.awt.Color.BLACK, 2));
    }

    public double getMaterialCost() {
        try {
            return Double.parseDouble(materialCostTextField.getText());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public double getLinealMeters() {
        try {
            return Double.parseDouble(linealMetersTextField.getText());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public double getProfit() {
        try {
            return Double.parseDouble(profitTextField.getText());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public int getAmount() {
        try {
            return Integer.parseInt(amountTextField.getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public String getDescription() {
        return descriptionTextArea.getText();
    }

    public void setSubTotal(double subTotal) {
        subTotalTextField.setText(String.format("%.2f", subTotal));
    }

    public void setFinalText(String finalText) {
        finalTextArea.setText(finalText);
    }


}
