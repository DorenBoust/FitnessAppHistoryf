package com.example.fitnessapp.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.fitnessapp.R;
import com.example.fitnessapp.keys.KeysIntents;
import com.example.fitnessapp.user.Meal;
import com.example.fitnessapp.user.Product;
import com.example.fitnessapp.user.ProductDataBase;
import com.example.fitnessapp.user.User;

import java.util.Dictionary;
import java.util.List;

public class DietActivity extends AppCompatActivity {

    TextView mealName;
    TextView mealTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);

        mealName = findViewById(R.id.product_meal_name);
        mealTime = findViewById(R.id.product_time_meal);



        Intent intent = getIntent();
        Meal meal = (Meal) intent.getSerializableExtra(KeysIntents.DIET_DATA);
        User user = (User) intent.getSerializableExtra(KeysIntents.SEND_USER);

        List<Product> products = meal.getProducts();
        String productName = products.get(0).getProductName();

        Dictionary<String, ProductDataBase> productDataBase = user.getProductDataBase();

        ProductDataBase productDataBase1 = productDataBase.get(productName);

        final RecyclerView recyclerView = findViewById(R.id.recycler_diet_activity);
        DietActivityRecycler adapter = new DietActivityRecycler(user,meal,getLayoutInflater());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(this,R.anim.layout_ex_activity_recycler_field));
        recyclerView.setAdapter(adapter);


        mealName.setText(meal.getName());
        mealTime.setText(meal.getTime());



    }
}
