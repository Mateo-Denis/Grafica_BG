package testing;
import org.reflections.Reflections;
import views.products.ProductCreateView;
import utils.TextUtils;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import views.products.modular.*;

public class testingMain {
    public static void main(String[] args) {
        // Cargar todos los JPanels desde el paquete com.example.views
        List<JPanel> panels = TextUtils.loadAllViewPanels("views.products.modular");

        // Ejemplo de uso de la lista de JPanels
        for (JPanel panel : panels) {
            System.out.println("JPanel cargado: " + panel);
        }
    }
}
