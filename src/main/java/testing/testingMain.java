package testing;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import models.BudgetModifyModel;
import utils.databases.BudgetsDatabaseConnection;
import utils.Product;

import java.awt.*;
import java.sql.*;

public class testingMain {
    private static final Multimap<Integer, Product> productMap = ArrayListMultimap.create();
    private static final BudgetsDatabaseConnection budgetsDatabaseConnection = new BudgetsDatabaseConnection();
    private static final BudgetModifyModel budgetModifyModel = new BudgetModifyModel(budgetsDatabaseConnection);

    public static void main(String[] args) throws SQLException {
//        Multimap<Integer,String> products = budgetModifyModel.getSavedProducts(1, "aaaa");
//        for (Map.Entry<Integer, String> entry : products.entries()) {
//            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
//        }
        //TuVieja();
        TuViejaElComeback();
    }

    public static void TuVieja() {
        // Obtener el dispositivo gráfico de la pantalla principal
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        // Obtener la configuración gráfica de la pantalla
        GraphicsConfiguration gc = gd.getDefaultConfiguration();

        // Obtener las dimensiones de la pantalla
        Rectangle bounds = gc.getBounds();

        // Mostrar las dimensiones
        System.out.println("Ancho de la pantalla: " + bounds.width);
        System.out.println("Alto de la pantalla: " + bounds.height);
    }

    public static void TuViejaElComeback()
    {
        // Obtener el dispositivo gráfico principal
        GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        // Obtener la configuración gráfica del dispositivo
        GraphicsConfiguration gc = graphicsDevice.getDefaultConfiguration();

        // Obtener las dimensiones de la pantalla
        Rectangle bounds = gc.getBounds();

        // Obtener el tamaño de la pantalla lógica
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // Calcular el factor de escala: (dimensión lógica / dimensión física)
        double scaleFactor = (double) screenSize.width / (double) bounds.width;

        // Mostrar el factor de escala
        System.out.println("Factor de escala: " + scaleFactor);
    }
}
