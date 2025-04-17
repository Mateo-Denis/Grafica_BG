package utils;

import lombok.Getter;

import java.util.ArrayList;

@Getter
public class Category {

    private final String name;
    private final ArrayList<Attribute> attributes;

    public Category(String name) {
        this.name = name;
        this.attributes = new ArrayList<Attribute>();
    }

    public void addAttribute(Attribute attribute) {
        attributes.add(attribute);
    }

}
