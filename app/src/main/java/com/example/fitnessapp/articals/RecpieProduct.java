package com.example.fitnessapp.articals;

import java.io.Serializable;

public class RecpieProduct implements Serializable {

    private String productName;
    private String unit;
    private String qty;

    public RecpieProduct(String productName, String unit, String qty) {
        this.productName = productName;
        this.unit = unit;
        this.qty = qty;
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

    @Override
    public String toString() {
        return "RecpieProduct{" +
                "productName='" + productName + '\'' +
                ", unit='" + unit + '\'' +
                ", qty='" + qty + '\'' +
                '}';
    }
}
