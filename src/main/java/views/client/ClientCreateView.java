package views.client;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import lombok.Getter;
import presenters.StandardPresenter;
import presenters.client.ClientCreatePresenter;
import utils.NumberInputVerifier;
import views.ToggleableView;

import javax.swing.*;
import javax.swing.text.AbstractDocument;

import java.awt.*;

import static utils.WindowFormatter.relativeSizeAndCenter;

public class ClientCreateView extends ToggleableView implements IClientCreateView {
    private JPanel containerPanel;
    private JPanel clientContainer;
    private JButton createButton;
    @Getter
    private JTextField clientTextField;
    private JLabel clientLabel;
    private JPanel addressContainer;
    private JTextField addressTextField;
    private JPanel cityContainer;
    private JLabel cityLabel;
    @Getter
    private JTextField cityTextField;
    private JPanel phoneContainer;
    private JLabel phoneLabel;
    private JTextField phoneTextField;
    private JRadioButton clientRadioButton;
    private JRadioButton particularRadioButton;
    private JPanel buttonsContainer;
    private JPanel radioButtonsContainer;
    private JPanel cityFieldsContainer;
    @Getter
    private JComboBox<String> cityComboBox;
    private ClientCreatePresenter clientCreatePresenter;

    public ClientCreateView() {
        windowFrame = new JFrame("Crear Cliente");
        windowFrame.setContentPane(containerPanel);
        windowFrame.pack();
        windowFrame.setLocationRelativeTo(null);
        windowFrame.setIconImage(new ImageIcon("src/main/resources/BGLogo.png").getImage());
        relativeSizeAndCenter(windowFrame, 0.4, 0.3);
        windowFrame.setResizable(false);
        ((AbstractDocument) phoneTextField.getDocument()).setDocumentFilter(new NumberInputVerifier());
        cambiarTamanioFuente(containerPanel, 14);
    }

    @Override
    public String getClientText() {
        return clientTextField.getText();
    }

    @Override
    public String getAddressText() {
        return addressTextField.getText();
    }

    @Override
    public String getCityText() {
        return cityTextField.getText();
    }

    public String getComboBoxSelectedCity() {
        return (String) cityComboBox.getSelectedItem();
    }

    @Override
    public String getPhoneText() {
        return phoneTextField.getText();
    }

    @Override
    public boolean isClientSelected() {
        return clientRadioButton.isSelected();
    }


    @Override
    public void setPresenter(StandardPresenter clientCreatePresenter) {
        this.clientCreatePresenter = (ClientCreatePresenter) clientCreatePresenter;
    }

    protected void initListeners() {
        createButton.addActionListener(e -> clientCreatePresenter.onCreateButtonClicked());
        createButton.addItemListener(e -> clientCreatePresenter.onEmptyFields(clientTextField, cityTextField, cityComboBox));
        cityComboBox.addActionListener(e -> clientCreatePresenter.onCityComboBoxSelected());

        clientTextField.addActionListener(e -> clientCreatePresenter.onCreateButtonClicked());
        addressTextField.addActionListener(e -> clientCreatePresenter.onCreateButtonClicked());
        cityTextField.addActionListener(e -> clientCreatePresenter.onCreateButtonClicked());
        phoneTextField.addActionListener(e -> clientCreatePresenter.onCreateButtonClicked());
    }

    @Override
    public void clearView() {
        clientTextField.setText("");
        addressTextField.setText("");
        cityTextField.setText("");
        phoneTextField.setText("");
        clientRadioButton.setSelected(true);
        particularRadioButton.setSelected(false);
    }

    protected void wrapContainer() {
        containerPanelWrapper = containerPanel;
    }

    public void addCityToComboBox(String city) {
        cityComboBox.addItem(city);
    }

}