package presenters.categories;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import presenters.StandardPresenter;
import views.categories.CategoryCreateView;
import views.categories.ICategoryCreateView;

import java.util.logging.Logger;

public class CategoryCreatePresenter extends StandardPresenter {
    private final CategoryCreateView createCategoryView;
    private static Logger LOGGER;

    public CategoryCreatePresenter(ICategoryCreateView createCategoryView) {
        this.createCategoryView = new CategoryCreateView();
        view = createCategoryView;

        this.createCategoryView.getAddFieldButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addField();
            }
        });

        this.createCategoryView.getCreateCategoryButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    createCategory();
                } catch (IOException | SQLException ex) {
                    LOGGER.log(null, "ERROR IN METHOD ''", e);
                }
            }
        });

    }

    private void addField() {
        String fieldType = (String) createCategoryView.getFieldTypeComboBox().getSelectedItem();

        if ("Campo de Texto".equals(fieldType)) {
            JTextField textField = new JTextField("Nuevo Campo de Texto");
            createCategoryView.addField(textField);
        } else if ("ComboBox".equals(fieldType)) {
            JComboBox<String> comboBox = new JComboBox<>(new String[]{"Opción 1", "Opción 2"});
            createCategoryView.addField(comboBox);
        }
    }

    private void createCategory() throws IOException, SQLException {
        String categoryName = createCategoryView.getCategoryNameField().getText();
        List<JComponent> fields = createCategoryView.getFieldList();

        if (categoryName.isEmpty() || fields.isEmpty()) {
            JOptionPane.showMessageDialog(createCategoryView, "Debe proporcionar un nombre y al menos un campo.");
            return;
        }

        // Aquí se genera la clase correspondiente a la nueva categoría en el directorio Modular Views.
        String className = "Modular" + categoryName + "View";
        String filePath = "src/ModularViews/" + className + ".java";
        generateCategoryClass(className, fields, filePath);

        // Después de generar la clase, agrega la categoría a la base de datos
        insertCategoryIntoDatabase(categoryName);

        JOptionPane.showMessageDialog(createCategoryView, "Categoría creada exitosamente.");
        createCategoryView.dispose();
    }

    private void generateCategoryClass(String className, List<JComponent> fields, String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write("import javax.swing.*;\n");
            writer.write("import java.awt.*;\n\n");
            writer.write("public class " + className + " extends JPanel {\n");
            writer.write("    public " + className + "() {\n");
            writer.write("        setLayout(new GridLayout(" + fields.size() + ", 1));\n");

            for (int i = 0; i < fields.size(); i++) {
                JComponent field = fields.get(i);
                String fieldType = field instanceof JTextField ? "JTextField" : "JComboBox";

                writer.write("        " + fieldType + " field" + i + " = new " + fieldType + "();\n");
                writer.write("        add(field" + i + ");\n");
            }

            writer.write("    }\n");
            writer.write("}\n");
        }
    }

    private void insertCategoryIntoDatabase(String categoryName) throws SQLException {
        String url = "PRUEBAS.db"; // Reemplaza con la ruta de tu base de datos
        String insertSQL = "INSERT INTO Categorias (Nombre) VALUES (?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

            pstmt.setString(1, categoryName);
            pstmt.executeUpdate();
        }
    }

    @Override
    protected void initListeners() {

    }

    public void onHomeCreateCategoryButtonClicked() {
        createCategoryView.showView();
    }
}
