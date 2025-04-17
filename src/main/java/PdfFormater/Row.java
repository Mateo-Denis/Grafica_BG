package PdfFormater;

import lombok.Getter;
import lombok.Setter;

//Someone pls find a better place for this class
@Getter
@Setter
public class Row {
    private String productName;
    private int quantity;
    private String measures;
    private String observations;
    private double price;
    private double total;

    public Row(String productName, int quantity, String measures, String observations, double price, double total){
        this.productName = productName;
        this.quantity = quantity;
        this.measures = measures;
        this.observations = observations;
        this.price = price;
        this.total = total;
    }

}
