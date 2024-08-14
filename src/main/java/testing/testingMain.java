package testing;

import views.budget.BudgetCreateView;
import views.products.ProductCreateView;
import utils.TextUtils;

import javax.swing.*;
import java.util.*;

public class testingMain {
    public static void main(String[] args) {
        BudgetCreateView budgetCreateView = new BudgetCreateView();
        int columns = budgetCreateView.getColumnCount();
        System.out.println(columns);
    }
}
