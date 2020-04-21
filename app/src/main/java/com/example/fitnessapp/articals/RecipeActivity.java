package com.example.fitnessapp.articals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.fitnessapp.R;
import com.example.fitnessapp.keys.KeysIntents;
import com.example.fitnessapp.user.ProductDataBase;
import com.example.fitnessapp.user.User;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeActivity extends AppCompatActivity {

    //tabs
    private ViewPager viewPager;
    private TabLayout recipeTabLayout;
    private ProductTabOnRecipeActivityFragment productTabOnRecipeActivityFragment;
    private StepsTabOnRecipeActivityFragment stepsTabOnRecipeActivityFragment;

    //base
    private Recpie recpie;
    private Map<String, ProductDataBase> productDataBase;
    private Gson gson = new Gson();

    //pallte
    private TextView recipeTitle;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        recipeTitle = findViewById(R.id.title_recipe_activity_tv);


        Intent intent = getIntent();
        String stringExtraRecipe = intent.getStringExtra(KeysIntents.SEND_RECIPE);
        String stringExtraProductDataBase = intent.getStringExtra(KeysIntents.SEND_PRODUCT_DATABASE);
        recpie = gson.fromJson(stringExtraRecipe, Recpie.class);

        recipeTitle.setText(recpie.getName());

        productDataBase = gson.fromJson(stringExtraProductDataBase, new TypeToken<HashMap<String, ProductDataBase>>() {}.getType());

        System.out.println("mapppppp" + productDataBase);

        String[] split = recpie.getProduct().get(0).getProductName().split("/");
        System.out.println(split[4]);
        System.out.println(productDataBase.get(split[4]));


        viewPager = findViewById(R.id.view_page_recipe_activity);
        recipeTabLayout = findViewById(R.id.tabLayout_recipe_activity);

        productTabOnRecipeActivityFragment = new ProductTabOnRecipeActivityFragment(recpie, productDataBase);
        stepsTabOnRecipeActivityFragment = new StepsTabOnRecipeActivityFragment(recpie);

        recipeTabLayout.setupWithViewPager(viewPager);

        RecipeActivityViewPagerAdapter recipeActivityViewPagerAdapter = new RecipeActivityViewPagerAdapter(getSupportFragmentManager(), 0);
        recipeActivityViewPagerAdapter.addFragment(productTabOnRecipeActivityFragment, "מרכיבים");
        recipeActivityViewPagerAdapter.addFragment(stepsTabOnRecipeActivityFragment, "שלבים");
        viewPager.setAdapter(recipeActivityViewPagerAdapter);



    }


    private void paserProductDataBase(String json){



    }





}
