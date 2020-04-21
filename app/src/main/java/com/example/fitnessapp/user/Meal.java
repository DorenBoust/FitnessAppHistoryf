package com.example.fitnessapp.user;

import java.io.Serializable;
import java.util.List;

public class Meal implements Serializable {
    private String mealName;
    private String mealTime;
    private String numberOfProduct;
    private List<Product> products;

    public Meal(String mealName, String mealTime, String numberOfProduct, List<Product> products) {
        this.mealName = mealName;
        this.mealTime = mealTime;
        this.numberOfProduct = numberOfProduct;
        this.products = products;
    }

    public String getName() {
        return mealName;
    }
    public void setName(String name) {
        this.mealName = name;
    }
    public String getTime() {
        return mealTime;
    }
    public void setTime(String time) {
        this.mealTime = time;
    }
    public String getNumberOfProduct() {
        return numberOfProduct;
    }
    public void setNumberOfProduct(String numberOfProduct) {
        this.numberOfProduct = numberOfProduct;
    }
    public List<Product> getProducts() {
        return products;
    }
    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "name='" + mealName + '\'' +
                ", mealTime='" + mealTime + '\'' +
                ", numberOfProduct='" + numberOfProduct + '\'' +
                ", products=" + products +
                '}';
    }
}
