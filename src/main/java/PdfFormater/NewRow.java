package PdfFormater;

import lombok.Getter;
import lombok.Setter;
import org.javatuples.Pair;

import java.util.ArrayList;

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
