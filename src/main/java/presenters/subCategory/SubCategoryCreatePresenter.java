package presenters.subCategory;

import static utils.MessageTypes.*;

import models.ISubCategoryModel;
import presenters.StandardPresenter;
import views.subCategories.*;
import models.ICategoryModel;
import utils.databases.SubCategoriesDatabaseConnection;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

public class SubCategoryCreatePresenter extends StandardPresenter {
    private final ISubCategoryModel subCategoryModel;
    private final ISubCategoryCreateView subCategoryCreateView;
    private final ICategoryModel categoryModel;
    private final SubCategoriesDatabaseConnection dbConnection;

    public SubCategoryCreatePresenter(ISubCategoryCreateView subCategoryCreateView, ISubCategoryModel subCategoryModel, ICategoryModel categoryModel, SubCategoriesDatabaseConnection dbConnection) {
        this.subCategoryCreateView = subCategoryCreateView;
        this.dbConnection = dbConnection;
        view = subCategoryCreateView;
        this.subCategoryModel = subCategoryModel;
        this.categoryModel = categoryModel;
        subCategoryCreateView.getCreateButton().addActionListener(e -> onCreateButtonClicked());
        cargarCategorias();
    }

    @Override
    protected void initListeners() {
        subCategoryModel.addSubCategoryCreationSuccessListener(() -> subCategoryCreateView.showMessage(SUBCATEGORY_CREATION_SUCCESS));
        subCategoryModel.addSubCategoryCreationFailureListener(() -> subCategoryCreateView.showMessage(SUBCATEGORY_CREATION_FAILURE));
    }

    public void onHomeCreateSubCategoryButtonClicked() {
        subCategoryCreateView.showView();
    }


    public void onCreateButtonClicked() {
        String nombreCategoria = subCategoryCreateView.getCategoryAssociated();
        String nombreSubCategoria = subCategoryCreateView.getSubCategoryName();
        int categoriaId = dbConnection.obtenerIdCategoria(nombreCategoria);
        if (categoriaId != -1) {
            try {
                dbConnection.insertSubCategory(categoriaId, nombreSubCategoria);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            JOptionPane.showMessageDialog(null, "SubCategoría creada con éxito");
        } else {
            JOptionPane.showMessageDialog(null, "Error al obtener la categoría seleccionada");
        }
    }


    private void cargarCategorias() {
        List<String> categorias = categoryModel.getCategoriesName();
        subCategoryCreateView.setCategorias(categorias);
    }
}

