package PdfFormater;

import lombok.Getter;
import lombok.Setter;

//Someone pls find a better place for this class
@Getter
@Setter
public class NewRow {
    private String productDescription;
    private double total;

    public NewRow(String productDescription, double total){
        this.productDescription = productDescription;
        this.total = total;
    }

}
