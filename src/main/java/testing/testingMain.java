package testing;

import models.CategoryModel;
import models.ICategoryModel;
import models.ProductModel;
import presenters.product.ProductCreatePresenter;
import utils.Product;
import utils.databases.BudgetsDatabaseConnection;
import views.budget.BudgetCreateView;
import views.products.ProductCreateView;
import utils.TextUtils;
import org.reflections.Reflections;
import views.products.modular.IModularCategoryView;
import views.products.modular.ModularCapView;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.*;

public class testingMain {
    private BudgetsDatabaseConnection budgetsDBConnection;

    public static void main(String[] args) throws SQLException {
        BudgetsDatabaseConnection budgetsdbConnection = new BudgetsDatabaseConnection();
        System.out.println(budgetsdbConnection.getBudgetID("Juancito"));
    }
}
