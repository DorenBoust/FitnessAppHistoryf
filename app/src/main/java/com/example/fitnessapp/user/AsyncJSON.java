package com.example.fitnessapp.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.fitnessapp.keys.KeysFirebaseStore;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AsyncJSON extends AsyncTask<String, Integer, User>{

    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private MutableLiveData<User> integrationCodeLiveData = new MutableLiveData<>();

    public AsyncJSON(MutableLiveData<User> integrationCodeLiveData) {
        this.integrationCodeLiveData = integrationCodeLiveData;
    }

    @Override
    protected User doInBackground(String... strings) {

        List<Day> days = new ArrayList<>();
        List<DietProcessRaw> dietRawList = new ArrayList<>();

        try {

            String urlTraining = "http://appfitness.boust.me/wp-json/acf/v3/trainers?appConnection=" + strings[0];
            String json = urlToString(urlTraining);
            System.out.println("urlTraining" + json);

            JSONArray rootJSONArray = new JSONArray(json);
            JSONObject rootJSONObject = rootJSONArray.getJSONObject(0);

            JSONObject acf = rootJSONObject.getJSONObject("acf");

            String name = acf.getString("name");
            String bDay = acf.getString("bDay");
            String nextMeet = acf.getString("next_meet");
            Date bDayDate = stringToDate(bDay,-1900);

            String height = acf.getString("height");
            double heightDouble = Double.parseDouble(height);

            String job = acf.getString("job");
            String phoneNumber = acf.getString("phoneNumber");
            String email = acf.getString("email");
            String goal = acf.getString("goal");
            String limitation = acf.getString("limitation");

            //training/fitness/workout
            JSONObject training = acf.getJSONObject("training");
            String[] daysName = {"sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday"};

            for (int i = 0; i < daysName.length ; i++) {
                JSONObject day = training.getJSONObject(daysName[i]);

                String numberOfExercies = day.getString("numberOfExercies");
                int numberOfExerciesInt = Integer.parseInt(numberOfExercies);
                System.out.println(daysName[i] + " " + numberOfExerciesInt);

                //extract exercises
                List<Exercise> exercises = new ArrayList<>();
                for (int j = 0; j < numberOfExerciesInt ; j++) {
                    String exNumber = "ex" + (j+1);
                    JSONObject ex = day.getJSONObject(exNumber);

                    String exNameJson = ex.getString("exName");
                    String[] exSplit = exNameJson.split("/");
                    String exJSON = "http://appfitness.boust.me/wp-json/acf/v3/exercises?name=" + exSplit[exSplit.length-1];


                    String jsonEx = urlToString(exJSON);
                    System.out.println("jsonEx " + jsonEx);

                    JSONArray rootJSONArrayEx = new JSONArray(jsonEx);
                    JSONObject rootJSONObjectEx = rootJSONArrayEx.getJSONObject(0);

                    JSONObject acfEx = rootJSONObjectEx.getJSONObject("acf");

                    String exName = acfEx.getString("name");
                    String exNameFinal = exName.replace("-", " ");

                    String imageUrl = acfEx.getString("image");
                    String videoUrl = acfEx.getString("video");


                    String set = ex.getString("sets");
                    int setInt = Integer.parseInt(set);

                    String repitition = ex.getString("repitition");
                    String rest = ex.getString("rest");
                    long restLong = millisectTimer(rest);

                    String notes = ex.getString("הערות");

                    Exercise exercise = new Exercise(exNameFinal,setInt,repitition,restLong,notes,imageUrl,videoUrl);
                    exercises.add(exercise);

                }
                if (numberOfExerciesInt != 0) {
                    days.add(new Day(daysName[i], exercises));
                }

            }

            //diet Table
            JSONObject process = acf.getJSONObject("prosess");
            JSONObject table = process.getJSONObject("table");
            int meetingNumber = Integer.parseInt(process.getString("meeting_number"));
            JSONArray body = table.getJSONArray("body");

            for (int z = 0; z < body.length() ; z++) {
                JSONArray jsonArrayBody = body.getJSONArray(z);

                JSONObject dateProcessJson = jsonArrayBody.getJSONObject(0);
                String dateProcessString = dateProcessJson.getString("c");

                Date dateConvert = stringToDate(dateProcessString,100);

                JSONObject weightProcessJson = jsonArrayBody.getJSONObject(1);
                String weightProcessString = weightProcessJson.getString("c");
                float weightProcessDouble = Float.parseFloat(weightProcessString);

                JSONObject abdominalProcessJson = jsonArrayBody.getJSONObject(2);
                String abdominalProcessString = abdominalProcessJson.getString("c");
                float abdominalProcessDouble = Float.parseFloat(abdominalProcessString);

                JSONObject armProcessJson = jsonArrayBody.getJSONObject(3);
                String armProcessString = armProcessJson.getString("c");
                float armProcessDouble = Float.parseFloat(armProcessString);

                JSONObject bodyFatProcessJson = jsonArrayBody.getJSONObject(4);
                String bodyFatProcessString = bodyFatProcessJson.getString("c");
                float bodyProcessDouble = Float.parseFloat(bodyFatProcessString);

                DietProcessRaw dietProcessRaw = new DietProcessRaw(dateConvert,weightProcessDouble,abdominalProcessDouble,armProcessDouble,bodyProcessDouble, dateProcessString);
                dietRawList.add(dietProcessRaw);

            }

            //diet menu
            JSONObject diet = acf.getJSONObject("diet");
            String numberOfMeals = diet.getString("number_of_meals");
            int numberOfMealsInt = Integer.parseInt(numberOfMeals);
            System.out.println("numberOfMealsInt " + numberOfMealsInt);

            Set<String> namesOfProductToExtract = new HashSet<>();

            List<Meal> meals = new ArrayList<>();

            for (int i = 0; i < numberOfMealsInt; i++) {

                String mealNumber = "meal_" + (i+1);
                JSONObject mealJSONObject = diet.getJSONObject(mealNumber);
                System.out.println("mealNumber" + mealNumber);

                String mealName = mealJSONObject.getString("meal_name");
                System.out.println("mealName " + mealName);


                String mealTime = mealJSONObject.getString("time");
                String numberOfProduct = mealJSONObject.getString("number_of_products");

                int numberOfProductInt = Integer.parseInt(numberOfProduct);
                System.out.println("numberOfProductInt " + numberOfProductInt);

                List<Product> products = new ArrayList<>();

                JSONObject productJSONObject = mealJSONObject.getJSONObject("מרכיבים");

                for (int j = 0; j < numberOfProductInt ; j++) {

                    String productNumber = "product" + (j+1);
                    System.out.println("productNumber " + productNumber);
                    JSONObject productJsonObject = productJSONObject.getJSONObject(productNumber);

                    String productName = productJsonObject.getString("product_name");
                    String[] splitURLProductName = productName.split("/");


                    namesOfProductToExtract.add(splitURLProductName[4]);

                    System.out.println("productName " + productName);


                    String unit = productJsonObject.getString("unit");
                    String qty = productJsonObject.getString("qty");


                    JSONArray alternativeJSONArray = productJsonObject.getJSONArray("alternative");

                    List<String> alternatives = new ArrayList<>();

                    for (int k = 0; k < alternativeJSONArray.length() ; k++) {
                        String alternative = alternativeJSONArray.getString(k);

                        String[] splitURLAlternative = alternative.split("/");

                        alternatives.add(splitURLAlternative[4]);
                        namesOfProductToExtract.add(splitURLAlternative[4]);
                    }

                    products.add(new Product(splitURLProductName[4], unit, qty, alternatives));

                }

                meals.add(new Meal(mealName, mealTime, numberOfProduct, products));
            }

            List<String> newNamsProductList = new ArrayList<>();
            newNamsProductList.addAll(namesOfProductToExtract);


            Dictionary<String, ProductDataBase> productDataBaseList = new Hashtable<>();

            for (int i = 0; i <newNamsProductList.size() ; i++) {

                String urlProduct = "http://appfitness.boust.me/wp-json/acf/v3/food?url_name=" + newNamsProductList.get(i);
                String jsonProduct = urlToString(urlProduct);
                System.out.println("JSON FOOD HEREEEEEEE!!!! - " + jsonProduct);

                JSONArray rootJSONArrayProduct = new JSONArray(jsonProduct);
                JSONObject rootJSONObjectEx = rootJSONArrayProduct.getJSONObject(0);

                JSONObject acfProduct = rootJSONObjectEx.getJSONObject("acf");

                String url_name = acfProduct.getString("url_name");
                String productNameHEB = acfProduct.getString("food_heb");
                String productNameEN = acfProduct.getString("food_en");
                String productImage = acfProduct.getString("image");

                JSONObject nutritionalValuesJSONObject = acfProduct.getJSONObject("nutritional_values");

                String calories = nutritionalValuesJSONObject.getString("calories");
                Double caloriesDouble = Double.parseDouble(calories);

                String proteins = nutritionalValuesJSONObject.getString("proteins");
                Double proteinsDouble = Double.parseDouble(proteins);

                String carbohydrates = nutritionalValuesJSONObject.getString("Carbohydrates");
                Double carbohydratesDouble = Double.parseDouble(carbohydrates);

                String sugar = nutritionalValuesJSONObject.getString("sugar");
                Double sugarDouble = Double.parseDouble(sugar);

                String fats = nutritionalValuesJSONObject.getString("fats");
                Double fatsDouble = Double.parseDouble(fats);

                String saturatedFat = nutritionalValuesJSONObject.getString("saturated_fat");
                Double saturatedFatDouble = Double.parseDouble(saturatedFat);

                String avrageGram = nutritionalValuesJSONObject.getString("avrage_gram_per_unit");
                Integer avrageGramInt = Integer.valueOf(avrageGram);


                productDataBaseList.put(newNamsProductList.get(i),new ProductDataBase(url_name,productNameHEB,productNameEN,productImage,caloriesDouble,proteinsDouble,carbohydratesDouble,sugarDouble,fatsDouble,saturatedFatDouble, avrageGramInt));
            }

            Diet dietFinal = new Diet(numberOfMealsInt, meals);

            fAuth = FirebaseAuth.getInstance();
            fStore = FirebaseFirestore.getInstance();

            DietProcessTab dietProcessTab = new DietProcessTab(dietRawList,meetingNumber);

            User user = new User(name, nextMeet, strings[0],bDayDate,heightDouble,job,phoneNumber,email,strings[1],goal,limitation,days, dietProcessTab,dietFinal, productDataBaseList);




            //Save on FireBaseStore
            Task<Void> documentReference2 = fStore.collection(KeysFirebaseStore.COLLECTION_USER).document(fAuth.getUid()).set(user);

            return user;

        }catch (IOException e){
            System.out.println("Error user JSON");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(User user) {
        System.out.println(user);
        integrationCodeLiveData.setValue(user);
    }

    private Date stringToDate(String string, int year){
        String[] dateSplit = string.split("/");
        int dateDay = Integer.parseInt(dateSplit[0]);
        int dateMonth = Integer.parseInt(dateSplit[1])-1;
        int dateYear = Integer.parseInt(dateSplit[2])+year;

        Date dateConvert = new Date(dateYear,dateMonth,dateDay);

        return dateConvert;
    }

    private long millisectTimer(String string){
        String[] split = string.split(":");
        String min = split[0];
        String sec = split[1];

        long minLong = Long.parseLong(min) * 60 * 1000;
        long secLong = Long.parseLong(sec) * 1000;

        long total = minLong + secLong;

        return total;

    }

    private String urlToString(String inputURL) throws IOException{
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

}
