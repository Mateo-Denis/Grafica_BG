package PdfFormater;

import lombok.Getter;
import lombok.Setter;

//Someone pls find a better place for this class
@Getter
@Setter
public class Row {
    private String description;
    private int quantity;
    private float dimensions;
    private float price;
    private float total;

    public Row(String description, int quantity,float dimensions,float price,float total){
        this.description = description;
        this.quantity = quantity;
        this.dimensions = dimensions;
        this.price = price;
        this.total = total;
    }

}
