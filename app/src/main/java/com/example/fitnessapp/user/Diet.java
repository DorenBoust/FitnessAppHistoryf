package com.example.fitnessapp.user;

import java.io.Serializable;
import java.util.List;

public class Diet implements Serializable {
    private int numberOfMeals;
    private List<Meal> meals;

    public Diet(int numberOfMeals, List<Meal> meals) {
        this.numberOfMeals = numberOfMeals;
        this.meals = meals;
    }

    public int getNumberOfMeals() {
        return numberOfMeals;
    }
    public void setNumberOfMeals(int numberOfMeals) {
        this.numberOfMeals = numberOfMeals;
    }
    public List<Meal> getMeals() {
        return meals;
    }
    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }

    @Override
    public String toString() {
        return "Diet{" +
                "numberOfMeals='" + numberOfMeals + '\'' +
                ", meals=" + meals +
                '}';
    }
}
