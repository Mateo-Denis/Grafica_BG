package views.client;

import lombok.Getter;
import presenters.StandardPresenter;
import presenters.client.ClientCreatePresenter;
import utils.NumberInputVerifier;
import views.ToggleableView;

import javax.swing.*;
import javax.swing.text.AbstractDocument;

import static utils.WindowFormatter.relativeSizeAndCenter;

public class ClientCreateView extends ToggleableView implements IClientCreateView {
    private JPanel containerPanel;
    private JPanel clientContainer;
    private JButton createButton;
    @Getter
    private JTextField clientTextField;
    private JLabel clientLabel;
    private JPanel addressContainer;
    @Getter
    private JTextField addressTextField;
    private JPanel cityContainer;
    private JLabel cityLabel;
    @Getter
    private JTextField cityTextField;
    private JPanel phoneContainer;
    private JLabel phoneLabel;
    @Getter
    private JTextField phoneTextField;
    private JRadioButton clientRadioButton;
    private JRadioButton particularRadioButton;
    private JPanel buttonsContainer;
    private JPanel radioButtonsContainer;
    private JPanel cityFieldsContainer;
    @Getter
    private JComboBox<String> cityComboBox;
    private ClientCreatePresenter clientCreatePresenter;

    private boolean isEditMode = false;
    private int editingClientID = -1;

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
    public void toggleToClientRadioButton() {
        clientRadioButton.setSelected(true);
        particularRadioButton.setSelected(false);
    }

    @Override
    public void toggleToParticularRadioButton() {
        particularRadioButton.setSelected(true);
        clientRadioButton.setSelected(false);
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
        isEditMode = false;
        editingClientID = -1;
        createButton.setText("Crear cliente");
        windowFrame.setTitle("Crear cliente");

    }

    protected void wrapContainer() {
        containerPanelWrapper = containerPanel;
    }

    public void addCityToComboBox(String city) {
        cityComboBox.addItem(city);
    }

    public void loadClientToEdit(int clientID) {
        isEditMode = true;
        editingClientID = clientID;
        createButton.setText("Guardar cambios");
        windowFrame.setTitle("Editar cliente");
        clientCreatePresenter.loadClientToEdit(String.valueOf(clientID));
    }

    @Override
    public boolean isEditMode() {
        return isEditMode;
    }
    @Override
    public int getEditingClientID(){
        return editingClientID;
    }
    public int getClientIDByName(String name) {
        return clientCreatePresenter.getClientIDByName(name);
    }
}