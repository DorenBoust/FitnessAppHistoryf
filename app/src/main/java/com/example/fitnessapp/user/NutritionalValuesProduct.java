package com.example.fitnessapp.user;

public class NutritionalValuesProduct {

    private int cal;
    private int pro;
    private int carboh;
    private int fat;

    public NutritionalValuesProduct(int cal, int pro, int carboh, int fat) {
        this.cal = cal;
        this.pro = pro;
        this.carboh = carboh;
        this.fat = fat;
    }

    public int getCal() {
        return cal;
    }

    public void setCal(int cal) {
        this.cal = cal;
    }

    public int getPro() {
        return pro;
    }

    public void setPro(int pro) {
        this.pro = pro;
    }

    public int getCarboh() {
        return carboh;
    }

    public void setCarboh(int carboh) {
        this.carboh = carboh;
    }

    public int getFat() {
        return fat;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    @Override
    public String toString() {
        return "NutritionalValuesProduct{" +
                "cal=" + cal +
                ", pro=" + pro +
                ", carboh=" + carboh +
                ", fat=" + fat +
                '}';
    }
}
