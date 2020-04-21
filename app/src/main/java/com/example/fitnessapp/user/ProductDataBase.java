package com.example.fitnessapp.user;

import com.example.fitnessapp.models.CustomMethods;

import java.io.Serializable;

public class ProductDataBase implements Serializable {
    private String productURL;
    private String productNameHEB;
    private String productNameEN;
    private String productImage;
    private double calories;
    private double proteins;
    private double carbohydrates;
    private double sugar;
    private double fats;
    private double saturatedFat;
    private int avrageGram;

    public ProductDataBase(String productURL, String productNameHEB, String productNameEN, String productImage, double calories, double proteins, double carbohydrates, double sugar, double fats, double saturatedFat, int avrageGram) {
        this.productURL = productURL;
        this.productNameHEB = productNameHEB;
        this.productNameEN = productNameEN;
        this.productImage = productImage;
        this.calories = calories;
        this.proteins = proteins;
        this.carbohydrates = carbohydrates;
        this.sugar = sugar;
        this.fats = fats;
        this.saturatedFat = saturatedFat;
        this.avrageGram = avrageGram;
    }

    public String getProductURL() {
        return productURL;
    }
    public void setProductURL(String productURL) {
        this.productURL = productURL;
    }
    public String getProductNameHEB() {
        return productNameHEB;
    }
    public void setProductNameHEB(String productNameHEB) {
        this.productNameHEB = productNameHEB;
    }
    public String getProductNameEN() {
        return productNameEN;
    }
    public void setProductNameEN(String productNameEN) {
        this.productNameEN = productNameEN;
    }
    public String getProductImage() {
        return CustomMethods.httpsTohttp(productImage);
    }
    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }
    public double getCalories() {
        return calories;
    }
    public void setCalories(double calories) {
        this.calories = calories;
    }
    public double getProteins() {
        return proteins;
    }
    public void setProteins(double proteins) {
        this.proteins = proteins;
    }
    public double getCarbohydrates() {
        return carbohydrates;
    }
    public void setCarbohydrates(double carbohydrates) {
        this.carbohydrates = carbohydrates;
    }
    public double getSugar() {
        return sugar;
    }
    public void setSugar(double sugar) {
        this.sugar = sugar;
    }
    public double getFats() {
        return fats;
    }
    public void setFats(double fats) {
        this.fats = fats;
    }
    public double getSaturated_fat() {
        return saturatedFat;
    }
    public void setSaturated_fat(double saturated_fat) {
        this.saturatedFat = saturated_fat;
    }

    public double getSaturatedFat() {
        return saturatedFat;
    }

    public void setSaturatedFat(double saturatedFat) {
        this.saturatedFat = saturatedFat;
    }

    public int getAvrageGram() {
        return avrageGram;
    }

    public void setAvrageGram(int avrageGram) {
        this.avrageGram = avrageGram;
    }

    @Override
    public String toString() {
        return "ProductDataBase{" +
                "productURL='" + productURL + '\'' +
                ", productNameHEB='" + productNameHEB + '\'' +
                ", productNameEN='" + productNameEN + '\'' +
                ", productImage='" + productImage + '\'' +
                ", calories=" + calories +
                ", proteins=" + proteins +
                ", carbohydrates=" + carbohydrates +
                ", sugar=" + sugar +
                ", fats=" + fats +
                ", saturated_fat=" + saturatedFat +
                '}';
    }
}
