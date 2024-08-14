package utils;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Attribute {

    private final String name;
    private final String value;

    public Attribute(String name, String value) {
        this.name = name;
        this.value = value;
    }

}
