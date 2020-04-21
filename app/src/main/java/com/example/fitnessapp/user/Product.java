package com.example.fitnessapp.user;

import java.io.Serializable;
import java.util.List;

public class Product implements Serializable {
    private String productName;
    private String unit;
    private String qty;
    private List<String> alternative;

    public Product(String productName, String unit, String qty, List<String> alternative) {
        this.productName = productName;
        this.unit = unit;
        this.qty = qty;
        this.alternative = alternative;
    }

    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public String getQty() {
        return qty;
    }
    public void setQty(String qty) {
        this.qty = qty;
    }
    public List<String> getAlternative() {
        return alternative;
    }
    public void setAlternative(List<String> alternative) {
        this.alternative = alternative;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productName='" + productName + '\'' +
                ", unit='" + unit + '\'' +
                ", qty='" + qty + '\'' +
                ", alternative='" + alternative + '\'' +
                '}';
    }
}
