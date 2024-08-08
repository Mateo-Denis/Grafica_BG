package models;

import models.listeners.failed.SubCategoryCreationFailureListener;
import models.listeners.failed.SubCategorySearchFailureListener;
import models.listeners.successful.SubCategoryCreationSuccessListener;
import models.listeners.successful.SubCategorySearchSuccessListener;

public interface ISubCategoryModel {
    void createSubCategory(int categoryIDAssociated, String subCategoryName);
    void addSubCategoryCreationSuccessListener(SubCategoryCreationSuccessListener listener);
    void addSubCategoryCreationFailureListener(SubCategoryCreationFailureListener listener);
    void addSubCategorySearchSuccessListener(SubCategorySearchSuccessListener listener);
    void addSubCategorySearchFailureListener(SubCategorySearchFailureListener listener);
    void getSubCategoriesName();
}
