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

import org.reflections.Reflections;
import presenters.product.ProductCreatePresenter;
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
     * @param productCreatePresenter An instance of ProductCreatePresenter to be passed to the constructors of the found classes.
     * @return A list of IModularCategoryView instances.
     */
    public static List<IModularCategoryView> loadAllViewPanels(String packageName, ProductCreatePresenter productCreatePresenter) {
        List<IModularCategoryView> modularList = new ArrayList<>();

        // Use Reflections to find all classes in the package that implement IModularCategoryView
        Reflections reflections = new Reflections(packageName);
        var classes = new ArrayList<>(reflections.getSubTypesOf(IModularCategoryView.class));

        // Sort the classes alphabetically by their simple names
        classes.sort(Comparator.comparing(Class::getSimpleName));

        for (Class<?> clazz : classes) {
            // Skip classes whose names end with ".form"
            if (clazz.getName().endsWith(".form")) {
                continue;
            }

            try {
                // Try to instantiate the class using a constructor that takes a ProductCreatePresenter as an argument
                Constructor<?> constructor = clazz.getDeclaredConstructor(ProductCreatePresenter.class);
                Object obj = constructor.newInstance(productCreatePresenter);
                IModularCategoryView instance = (IModularCategoryView) obj;

                // Add the instance to the list
                modularList.add(instance);
            } catch (InvocationTargetException e) {
                Throwable cause = e.getCause();
                System.err.println("Constructor threw an exception: " + cause);
                LOGGER.log(null, "ERROR IN METHOD 'loadAllViewPanels' IN CLASS->'TextUtils'", e);
            } catch (Exception e) {
               LOGGER.log(null,"ERROR IN METHOD 'loadAllViewPanels' IN CLASS->'TextUtils", e);
            }
        }

        // Return the list of IModularCategoryView instances
        return modularList;
    }
}

