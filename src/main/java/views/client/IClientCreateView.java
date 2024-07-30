package views.client;

public interface IClientCreateView {
    String getClient();
    String getAddress();
    void showSuccessMessage();
    void showErrorMessage(String message);
}
