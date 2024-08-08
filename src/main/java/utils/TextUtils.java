/*
CLASE UTILIZADA PARA EL MAP DE JPANELS Y SUS NOMBRES.
EXTRAE EL SUBSTRIN DE CADA MODULARVIEW. EJEMPLO: DE MODULARTSHIRTVIEW SE EXTRAE TSHIRT
PARA ASOCIAR EL JPANEL CON EL NOMBRE DE LA CATEGORIA.
*/

package utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.reflections.Reflections;

import javax.swing.*;
import java.lang.reflect.Method;
import java.util.Set;

public class TextUtils {

    public String extractor(String text) {
        int startIndex = text.indexOf("Modular") + "Modular".length();
        int endIndex = text.indexOf("View");
        String extracted = text.substring(startIndex, endIndex);
        return extracted;
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

        // Convertir el Set a una Lista y devolverla
        return new ArrayList<>(fileNamesSet);
    }


    //METODO PARA LLENAR UNA LISTA DE JPANELS
    public static List<JPanel> loadAllViewPanels(String packageName) {
        List<JPanel> panelList = new ArrayList<>();

        // Usar Reflections para encontrar todas las clases en el paquete
        Reflections reflections = new Reflections(packageName);
        var classes = reflections.getSubTypesOf(JPanel.class);

        //System.out.println("Classes found in package: " + classes);

        for (Class<?> clazz : classes) {
            //System.out.println("Processing class: " + clazz.getName());

            if(clazz.getName().endsWith(".form")) {
                continue;
            }

            try {
                // Intentar instanciar la clase
                Object instance = clazz.getDeclaredConstructor().newInstance();

                // Buscar un método que retorne un JPanel
                for (Method method : clazz.getDeclaredMethods()) {
                    if (JPanel.class.isAssignableFrom(method.getReturnType())) {
                        // Invocar el método y obtener el JPanel
                        JPanel panel = (JPanel) method.invoke(instance);
                        panelList.add(panel);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                // Manejo de errores
            }
        }

        return panelList;
    }

}

