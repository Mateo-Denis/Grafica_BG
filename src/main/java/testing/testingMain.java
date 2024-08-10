package testing;

import views.products.ProductCreateView;
import utils.TextUtils;

import javax.swing.*;
import java.util.*;

public class testingMain {
    public static void main(String[] args) {
        ProductCreateView productCreateView = new ProductCreateView();
        TextUtils textUtils = new TextUtils();
        Map<String, JPanel> map = productCreateView.getCategoryPanelsMap();
        JPanel panel = productCreateView.getCorrespondingModularView("TAZAS");
        System.out.println(panel);
    }
}
