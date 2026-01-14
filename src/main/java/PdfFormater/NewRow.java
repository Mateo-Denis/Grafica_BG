package PdfFormater;

import lombok.Getter;
import lombok.Setter;
import org.javatuples.Pair;

import java.util.ArrayList;

@Getter
@Setter
public class NewRow {
    private String productDescription;
    private String total;

    public NewRow(String productDescription, String total){
        this.productDescription = productDescription;
        this.total = total;
    }

}
