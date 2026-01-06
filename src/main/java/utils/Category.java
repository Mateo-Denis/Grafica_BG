package utils;

import lombok.Getter;

import java.util.ArrayList;

@Getter
public class Category {

    private final String name;

    public Category(String name) {
        this.name = name;
    }

    public ArrayList<String> categoriesNamesList() {
        ArrayList<String> names = new ArrayList<>();
        names.add("Cup");
        names.add("Cap");
        names.add("Cloth");
        names.add("Flag");
        names.add("Clothes");
        names.add("CommonServices");
        names.add("CuttingService");
        names.add("LinearPrinting");
        names.add("SquareMeterPrinting");
        return names;
    }

}
