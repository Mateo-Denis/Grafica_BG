package PdfFormater.codingerror.model;

import PdfFormater.codingerror.util.ConstantUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductTableHeader {
    String description= ConstantUtil.PRODUCT_TABLE_DESCRIPTION;
    String quantity=ConstantUtil.PRODUCT_TABLE_QUANTITY;
    String dimensions=ConstantUtil.PRODUCT_TABLE_DIMENSIONS;
    String price=ConstantUtil.PRODUCT_TABLE_PRICE;
    String total = ConstantUtil.PRODUCT_TABLE_TOTAL;

    public ProductTableHeader setDescription(String description) {
        this.description = description;
        return this;
    }

    public ProductTableHeader setQuantity(String quantity) {
        this.quantity = quantity;
        return this;
    }

    public ProductTableHeader setPrice(String price) {
        this.price = price;
        return this;
    }

    public ProductTableHeader setDimensions(String dimensions) {
        this.dimensions = dimensions;
        return this;
    }

    public ProductTableHeader setTotal(String total) {
        this.total = total;
        return this;
    }
    public ProductTableHeader build(){
        return this;
    }
}
