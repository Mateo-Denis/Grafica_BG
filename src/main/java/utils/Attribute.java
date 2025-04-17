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
        if(value.isEmpty()){
            this.value = "0.0";
        }else {
            this.value = value;
        }
    }

}
