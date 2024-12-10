package PdfFormater;

import lombok.Getter;
import lombok.Setter;

//Someone pls find a better place for this class
@Getter
@Setter
public class Row {
    private String description;
    private int quantity;
    private String measures;
    private String observations;
    private double price;
    private double total;

    public Row(String description, int quantity, String measures, String observations, double price, double total){
        this.description = description;
        this.quantity = quantity;
        this.measures = measures;
        this.observations = observations;
        this.price = price;
        this.total = total;
    }

}
