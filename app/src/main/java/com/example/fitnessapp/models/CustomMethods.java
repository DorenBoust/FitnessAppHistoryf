package com.example.fitnessapp.models;

import com.example.fitnessapp.user.Day;
import com.example.fitnessapp.user.Exercise;
import com.example.fitnessapp.user.NutritionalValuesProduct;
import com.example.fitnessapp.user.Product;
import com.example.fitnessapp.user.ProductDataBase;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class CustomMethods {

    private CustomMethods(){

    }

    public static String getEsTime(Day day){
        List<Exercise> exercises = day.getExercises();
        long restTime = 0;
        long exTime = 0;
        for (Exercise exercise : exercises) {
            restTime += exercise.getRest();
            restTime += exercise.getSets() * 60_000;
        }
        long hours = TimeUnit.MILLISECONDS.toHours(restTime);
        long min = TimeUnit.MILLISECONDS.toMinutes(restTime) - (hours*60);
        String minString = String.valueOf(min);
        String hourString = String.valueOf(hours);
        String total;


        if (min < 10){
            total = hourString + ":0" + min;
        } else {
            total = hourString + ":" + min;
        }

        return total;

    }

    public static String getEsTime(long restTime){
        long hours = TimeUnit.MILLISECONDS.toHours(restTime);
        long min = TimeUnit.MILLISECONDS.toMinutes(restTime) - (hours*60);
        String minString = String.valueOf(min);
        String hourString = String.valueOf(hours);
        String total;


        if (min < 10){
            total = hourString + ":0" + min;
        } else {
            total = hourString + ":" + min;
        }

        return total;
    }

    public static String convertDate(String correctDay){
        switch (correctDay){
            case "sunday":
                return "יום ראשון";
            case "monday":
                return "יום שני";
            case "tuesday":
                return "יום שלישי";
            case "wednesday":
                return "יום רביעי";
            case "thursday":
                return "יום חמישי";
            case "friday":
                return "יום שישי";
            case "saturday":
                return "יום שבת";
            case "יום ראשון":
                return "יום ראשון";
            case "יום שני":
                return "יום שני";
            case "יום שלישי":
                return "יום שלישי";
            case "יום רביעי":
                return "יום רביעי";
            case "יום חמישי":
                return "יום חמישי";
            case "יום שישי":
                return "יום שישי";
            case "יום שבת":
                return "יום שבת";
        }

        return null;
    }

    public static String convertDateToEnglish(String correctDay){
        switch (correctDay){
            case "יום ראשון":
                return "sunday";
            case "יום שני":
                return "monday";
            case "יום שלישי":
                return "tuesday";
            case "יום רביעי":
                return "wednesday";
            case "יום חמישי":
                return "thursday";
            case "יום שישי":
                return "friday";
            case "יום שבת":
                return "saturday";
        }

        return null;
    }

    public static NutritionalValuesProduct getProductNut(ProductDataBase productDataBaseOfOneProduct, Product product){

        String productName = product.getProductName();
        double qty = Double.parseDouble(product.getQty());
        String unit = product.getUnit();

        int cal = 0;
        int pro = 0;
        int fat = 0;
        int carboh = 0;


        switch (unit){
            case "גרם":

                cal += (qty / 100.0) * productDataBaseOfOneProduct.getCalories();
                pro += (qty / 100.0) * productDataBaseOfOneProduct.getProteins();
                fat += (qty / 100.0) * productDataBaseOfOneProduct.getFats();
                carboh += (qty / 100.0) * productDataBaseOfOneProduct.getCarbohydrates();

                break;

            case "כפיות":

                cal += (qty / 5.0) * productDataBaseOfOneProduct.getCalories();
                pro += (qty / 5.0) * productDataBaseOfOneProduct.getProteins();
                fat += (qty / 5.0) * productDataBaseOfOneProduct.getFats();
                carboh += (qty / 5.0) * productDataBaseOfOneProduct.getCarbohydrates();

                break;

            case "כפות":

                cal += (qty / 10.0) * productDataBaseOfOneProduct.getCalories();
                pro += (qty / 10.0) * productDataBaseOfOneProduct.getProteins();
                fat += (qty / 10.0) * productDataBaseOfOneProduct.getFats();
                carboh += (qty / 10.0) * productDataBaseOfOneProduct.getCarbohydrates();

                break;

            case "כוסות":

                cal += (qty * 2) * productDataBaseOfOneProduct.getCalories(); // avrage gram per cup 200 gram. if have (3 cups * 2) * 100 gram = 600
                pro += (qty * 2) * productDataBaseOfOneProduct.getProteins();
                fat += (qty * 2) * productDataBaseOfOneProduct.getFats();
                carboh += (qty * 2) * productDataBaseOfOneProduct.getCarbohydrates();

                break;

            case "יחידות":

                cal += ((qty * productDataBaseOfOneProduct.getAvrageGram()) / 100) * productDataBaseOfOneProduct.getCalories();  // ((2 unit * 250 gram per unit) /100) * 100 gram
                pro += ((qty * productDataBaseOfOneProduct.getAvrageGram()) / 100) * productDataBaseOfOneProduct.getProteins();
                fat += ((qty * productDataBaseOfOneProduct.getAvrageGram()) / 100) * productDataBaseOfOneProduct.getFats();
                carboh += ((qty * productDataBaseOfOneProduct.getAvrageGram()) / 100) * productDataBaseOfOneProduct.getCarbohydrates();

                break;



        }



        return new NutritionalValuesProduct(cal,pro,carboh,fat);

    }

    //for some resone picasso not loading image from fitness web **that https**, this metud convert to http.
    public static String httpsTohttp(String url){
        String[] splitHTTPS = url.split("//");
        String http = "http://" + splitHTTPS[1];
        return http;
    }



}
