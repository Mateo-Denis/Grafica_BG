package PdfFormater.codingerror.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.Optional;

@Getter
public class Product {
    private Optional<String> pname;
    @Setter
    private int quantity;
    @Setter
    private String observations;
    private final String dimensions;
    private double priceperpeice;
    private final double total;


    public Product(String pname, int quantity, String dimensions, String observations, double priceperpeice, double total ) {
        this.pname = Optional.ofNullable(pname);
        this.quantity = quantity;
        this.dimensions = dimensions;
        this.observations = observations;
        this.priceperpeice = priceperpeice;
        this.total = total;
    }


    public void setPname(String pname) {
        this.pname = Optional.ofNullable(pname);
    }

    public void setPriceperpeice(float priceperpeice) {
        this.priceperpeice = priceperpeice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return Objects.equals(pname, product.pname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pname);
    }

    @Override
    public String toString() {
        return "{" +
                "pname=" + pname +
                ", quantity=" + quantity +
                ", measures=" + dimensions +
                ", priceperpeice=" + priceperpeice +
                ", total=" + total +
                '}';
    }
}

