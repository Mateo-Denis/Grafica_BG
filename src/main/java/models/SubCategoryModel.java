package models;

import models.listeners.failed.SubCategoryCreationFailureListener;
import models.listeners.failed.SubCategorySearchFailureListener;
import models.listeners.successful.SubCategoryCreationSuccessListener;
import models.listeners.successful.SubCategorySearchSuccessListener;
import utils.ProductSubCategory;
import utils.databases.SubCategoriesDatabaseConnection;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SubCategoryModel implements ISubCategoryModel {
    private final SubCategoriesDatabaseConnection dbConnection;
    private final List<SubCategoryCreationSuccessListener> subCategoryCreationSuccessListener;
    private final List<SubCategoryCreationFailureListener> subCategoryCreationFailureListener;
    private final List<SubCategorySearchSuccessListener> subCategorySearchSuccessListener;
    private final List<SubCategorySearchFailureListener> subCategorySearchFailureListener;

    public SubCategoryModel(SubCategoriesDatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
        ArrayList<ProductSubCategory> subCategories;

        this.subCategoryCreationSuccessListener = new LinkedList<>();
        this.subCategoryCreationFailureListener = new LinkedList<>();

        this.subCategorySearchSuccessListener = new LinkedList<>();
        this.subCategorySearchFailureListener = new LinkedList<>();
    }


    public void createSubCategory(int categoryAssociated, String subCategoryName) {
        try {
            dbConnection.insertSubCategory(categoryAssociated, subCategoryName);
            notifySubCategoryCreationSuccess();
        } catch (Exception e) {
            notifySubCategoryCreationFailure();
        }
    }

    @Override
    public void addSubCategoryCreationSuccessListener(SubCategoryCreationSuccessListener listener) {
        subCategoryCreationSuccessListener.add(listener);
    }


    @Override
    public void addSubCategoryCreationFailureListener(SubCategoryCreationFailureListener listener) {
        subCategoryCreationFailureListener.add(listener);
    }


    @Override
    public void addSubCategorySearchSuccessListener(SubCategorySearchSuccessListener listener) {
        subCategorySearchSuccessListener.add(listener);
    }

    @Override
    public void addSubCategorySearchFailureListener(SubCategorySearchFailureListener listener) {
        subCategorySearchFailureListener.add(listener);
    }

    @Override
    public void getSubCategoriesName() {

    }

    private void notifySubCategoryCreationSuccess() {
        for (SubCategoryCreationSuccessListener listener : subCategoryCreationSuccessListener) {
            listener.onSuccess();
        }
    }


    private void notifySubCategoryCreationFailure() {
        for (SubCategoryCreationFailureListener listener : subCategoryCreationFailureListener) {
            listener.onFailure();
        }
    }


    private void notifySubCategorySearchSuccess() {
        for (SubCategorySearchSuccessListener listener : subCategorySearchSuccessListener) {
            listener.onSuccess();
        }
    }


    private void notifySubCategorySearchFailure() {
        for (SubCategorySearchFailureListener listener : subCategorySearchFailureListener) {
            listener.onFailure();
        }
    }
}
