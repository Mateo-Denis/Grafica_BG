package models;

import models.listeners.failed.ProductListOpeningFailureListener;
import models.listeners.successful.ProductListOpeningSuccessListener;
import utils.Product;

import java.util.ArrayList;
import java.util.List;

public interface IProductListModel {
    ArrayList<Product> getProductsFromDB();

}
