package views.products.modular;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public interface IModularCategoryView {
    JPanel getContainerPanel();
    ArrayList<String> getModularAttributes();
}
