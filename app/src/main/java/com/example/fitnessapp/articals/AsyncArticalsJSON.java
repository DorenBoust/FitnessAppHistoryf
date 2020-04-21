package com.example.fitnessapp.articals;

import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AsyncArticalsJSON extends AsyncTask<String,Integer,Recipes> {

    MutableLiveData<Recipes> mLiveDataRecpie;

    public AsyncArticalsJSON(MutableLiveData<Recipes> mLiveDataRecpie) {
        this.mLiveDataRecpie = mLiveDataRecpie;
    }

    @Override
    protected Recipes doInBackground(String... strings) {

        String address = "http://appfitness.boust.me/wp-json/acf/v3/recipe";
        try {
            String jsonString = urlToString(address);

            System.out.println(jsonString);

            JSONArray jsonArray = new JSONArray(jsonString);


            List<Recpie> recpies = new ArrayList<>();
            for (int i = 0; i < jsonArray.length() ; i++) {

                JSONObject recpie = jsonArray.getJSONObject(i);
                JSONObject acf = recpie.getJSONObject("acf");

                String id = acf.getString("id");
                System.out.println("rec id - " + id);

                String name = acf.getString("name");
                String sentencePreview = acf.getString("sentencePreview");
                String estTime = acf.getString("est_time");
                String level = acf.getString("level");
                String mainImage = acf.getString("mainImage");

                List<String> goals = new ArrayList<>();
                JSONArray goal = acf.getJSONArray("for_who_goal");

                for (int j = 0; j < goal.length() ; j++) {

                    goals.add(goal.getString(j));

                }

                JSONObject products = acf.getJSONObject("products");
                int numberOfProducts = Integer.parseInt(products.getString("number_of_products"));
                JSONObject productsFinal = products.getJSONObject("products");

                List<RecpieProduct> recpieProducts = new ArrayList<>();

                for (int j = 0; j < numberOfProducts ; j++) {

                    String productNumber = "product_" + (j+1);

                    JSONObject productObject = productsFinal.getJSONObject(productNumber);

                    String productName = productObject.getString("product_name");
                    System.out.println("rec productName - " + productName);
                    String unit = productObject.getString("unit");
                    String qty = productObject.getString("qty");

                    recpieProducts.add(new RecpieProduct(productName,unit,qty));

                }

                JSONObject preparationMethod = acf.getJSONObject("preparation_method");
                int numberOfSteps = Integer.parseInt(preparationMethod.getString("מספר_שלבים"));
                JSONObject steps = preparationMethod.getJSONObject("steps");

                List<String> stepsList = new ArrayList<>();
                for (int j = 0; j < numberOfSteps ; j++) {

                    String stepNumber = "step_" + (j+1);
                    stepsList.add(steps.getString(stepNumber));
                }

                RecpieSteps recpieSteps = new RecpieSteps(numberOfSteps,stepsList);


                recpies.add(new Recpie(id,name,sentencePreview,estTime,goals,mainImage,recpieProducts,recpieSteps,level));
            }


            return new Recipes(recpies);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }


    private String urlToString(String inputURL) throws IOException {
        URL url = new URL(inputURL);
        System.out.println(url);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        InputStream inputStream = con.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        StringBuilder sb = new StringBuilder();
        String line = null;

        while ((line = reader.readLine()) != null){
            sb.append(line);
        }

        return sb.toString();
    }

    @Override
    protected void onPostExecute(Recipes recpies) {
        super.onPostExecute(recpies);
        System.out.println("recpies - " + recpies);
        mLiveDataRecpie.setValue(recpies);
    }
}


