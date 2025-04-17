package models;

import utils.Budget;
import java.util.ArrayList;

public interface IBudgetListModel {
    ArrayList<Budget> getBudgetsFromDB();
}
