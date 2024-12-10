package PdfFormater.codingerror.model;

import java.util.Objects;
import java.util.Optional;

public class Product {
    private Optional<String> pname;
    private int quantity;
    private String observations;
    private String dimensions;
    private double priceperpeice;
    private double total;


    public Product(String pname, int quantity, String dimensions, String observations, double priceperpeice, double total ) {
        this.pname = Optional.ofNullable(pname);
        this.quantity = quantity;
        this.dimensions = dimensions;
        this.observations = observations;
        this.priceperpeice = priceperpeice;
        this.total = total;
    }

    public Optional<String> getPname() {
        return pname;
    }
    public String getDimensions(){
        return dimensions;
    }
    public double getTotal(){
        return total;
    }

    public String getObservations(){
        return observations;
    }

    public void setObservations(String observations){
        this.observations = observations;
    }


    public void setPname(String pname) {
        this.pname = Optional.ofNullable(pname);
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPriceperpeice() {
        return priceperpeice;
    }

    public void setPriceperpeice(float priceperpeice) {
        this.priceperpeice = priceperpeice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
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

