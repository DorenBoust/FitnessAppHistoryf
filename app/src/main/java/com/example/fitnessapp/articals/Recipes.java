package com.example.fitnessapp.articals;

import java.io.Serializable;
import java.util.List;

public class Recipes implements Serializable {

    private List<Recpie> recipes;

    public Recipes(List<Recpie> recipes) {
        this.recipes = recipes;
    }

    public List<Recpie> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recpie> recipes) {
        this.recipes = recipes;
    }

    @Override
    public String toString() {
        return "Recipes{" +
                "recipes=" + recipes +
                '}';
    }
}
