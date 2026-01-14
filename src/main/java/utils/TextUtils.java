/*
CLASE UTILIZADA PARA EL MAP DE JPANELS Y SUS NOMBRES.
EXTRAE EL SUBSTRIN DE CADA MODULARVIEW. EJEMPLO: DE MODULARTSHIRTVIEW SE EXTRAE TSHIRT
PARA ASOCIAR EL JPANEL CON EL NOMBRE DE LA CATEGORIA.
*/

package utils;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.logging.Logger;

import PdfFormater.NewRow;
import org.javatuples.Pair;
import org.reflections.Reflections;
import presenters.product.ProductCreatePresenter;
import presenters.product.ProductPresenter;
import presenters.product.ProductSearchPresenter;
import views.products.modular.IModularCategoryView;

public class TextUtils {
    private static Logger LOGGER;

    public String extractor(String text) {
        int startIndex = text.indexOf("Modular") + "Modular".length();
        int endIndex = text.indexOf("View");
        return text.substring(startIndex, endIndex);
    }

    public List<String> getFileNamesInDirectory(String directoryPath) {
        Set<String> fileNamesSet = new HashSet<>();
        File directory = new File(directoryPath);

        // Verificar si el directorio existe y es realmente un directorio
        if (directory.exists() && directory.isDirectory()) {
            // Obtener la lista de archivos en el directorio
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) {
                    // Verificar si el elemento es un archivo
                    if (file.isFile()) {
                        // Obtener el nombre del archivo sin la extensión
                        String fileName = file.getName();
                        int dotIndex = fileName.lastIndexOf('.');
                        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
                            fileName = fileName.substring(0, dotIndex);
                        }
                        fileNamesSet.add(fileName);
                    }
                }
            }
        }

        // Convertir el Set a una Lista
        List<String> fileNamesList = new ArrayList<>(fileNamesSet);

        // Ordenar la lista alfabéticamente
        Collections.sort(fileNamesList);

        // Devolver la lista ordenada
        return fileNamesList;
    }


    /**
     * Loads and instantiates all classes that implement the IModularCategoryView interface within the specified package.
     *
     * @param packageName The name of the package to search for classes that implement IModularCategoryView.
     * @param presenter   An instance of ProductPresenter to be passed to the constructors of the found classes.
     * @param isCreate    A boolean flag indicating whether the view is in "create" mode (true) or "search" mode (false).
     * @return A list of IModularCategoryView instances that were successfully instantiated.
     */
    public static List<IModularCategoryView> loadAllViewPanels(String packageName, ProductPresenter presenter, boolean isCreate) {
        // List to store the instantiated views
        List<IModularCategoryView> views = new ArrayList<>();

        // Use Reflections to find all classes in the specified package that implement IModularCategoryView
        Reflections reflections = new Reflections(packageName);
        var classes = new ArrayList<>(reflections.getSubTypesOf(IModularCategoryView.class));

        // Sort the classes alphabetically by their simple names for consistent ordering
        classes.sort(Comparator.comparing(Class::getSimpleName));

        // Iterate over each class found
        for (Class<?> clazz : classes) {
            // Skip classes whose names end with ".form" (likely non-instantiable or irrelevant classes)
            if (clazz.getName().endsWith(".form")) {
                continue;
            }

            // Iterate over the classes again (this nested loop seems redundant and may need review)
            for (Class<? extends IModularCategoryView> clazzz : classes) {
                try {
                    // Get the constructor that accepts a boolean and a ProductPresenter as parameters
                    Constructor<? extends IModularCategoryView> constructor =
                            clazzz.getConstructor(boolean.class, ProductPresenter.class);

                    // Instantiate the class using the provided parameters
                    IModularCategoryView view = constructor.newInstance(isCreate, presenter);

                    // Add the instantiated view to the list
                    views.add(view);
                } catch (Exception e) {
                    // Print the stack trace for debugging purposes
                    e.printStackTrace();
                    // Optionally handle the error (e.g., log it or notify the user)
                }
            }
        }

        // Return the list of instantiated views
        return views;
    }

    public static String truncateAndRound(String valor) {
        try {
            double numero = Double.parseDouble(valor);
            int redondeado = (int) (Math.ceil(numero / 5.0) * 5);
            return String.valueOf(redondeado);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El valor ingresado no es un número válido: " + valor);
        }
    }

    public ArrayList<NewRow> toTableRow(ArrayList<Pair<String, String>> material){
        ArrayList<NewRow> tableRows = new ArrayList<>();
        for(Pair<String, String> item : material){
            NewRow row = new NewRow(item.getValue0(), (item.getValue1()));
            tableRows.add(row);
        }
        return tableRows;
    }
}

