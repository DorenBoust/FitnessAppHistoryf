package com.example.fitnessapp.articals;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.fitnessapp.R;
import com.example.fitnessapp.keys.KeysIntents;
import com.example.fitnessapp.keys.KeysSharedPrefercence;
import com.example.fitnessapp.user.ProductDataBase;
import com.example.fitnessapp.user.User;
import com.google.gson.Gson;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipesFragmentTab extends Fragment implements RecipeRecyclerAdapter.OnRecipeListener {

    private TextView titleName;
    private User user;
    private Recipes recipesList;
    private Recipes refrashRecipes;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MutableLiveData<Recipes> mutableLiveData = new MutableLiveData<>();
    private Gson gson = new Gson();



    public RecipesFragmentTab(Recipes recipes, User user) {
        // Required empty public constructor
        this.recipesList = recipes;
        this.user = user;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(KeysSharedPrefercence.REFRESH_RESIPE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        String recipe = gson.toJson(refrashRecipes);
        edit.putString(KeysSharedPrefercence.PUT_REFRESH_RESIPE, recipe);
        edit.apply();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_recipes_tab, container, false);

        final RecyclerView recyclerView = v.findViewById(R.id.recycler_recipes);
        swipeRefreshLayout = v.findViewById(R.id.swipe_refrash);


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(KeysSharedPrefercence.REFRESH_RESIPE,Context.MODE_PRIVATE);

        String spRecipes = sharedPreferences.getString(KeysSharedPrefercence.PUT_REFRESH_RESIPE, "null");


        if (spRecipes.equals("null")) {

            RecipeRecyclerAdapter adapter = new RecipeRecyclerAdapter(recipesList, getLayoutInflater(), RecipesFragmentTab.this);
            recyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_diet_fragment_meal_recycler));
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);


        } else {

            Gson gson = new Gson();
            recipesList = gson.fromJson(spRecipes, Recipes.class);

            RecipeRecyclerAdapter adapter = new RecipeRecyclerAdapter(recipesList, getLayoutInflater(), RecipesFragmentTab.this);
            recyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_diet_fragment_meal_recycler));
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);

        }


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                recyclerView.setVisibility(View.INVISIBLE);

                new AsyncArticalsJSON(mutableLiveData).execute();


            }
        });

        mutableLiveData.observe(getActivity(), new Observer<Recipes>() {
            @Override
            public void onChanged(Recipes recipes) {

                System.out.println("OBSERRRRRR");
                refrashRecipes = recipes;

                RecipeRecyclerAdapter adapter = new RecipeRecyclerAdapter(refrashRecipes, getLayoutInflater(), RecipesFragmentTab.this);
                recyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_diet_fragment_meal_recycler));
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(adapter);

                recyclerView.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);



            }
        });



        return v;


    }

    @Override
    public void OnRecipeClick(int position) {

        System.out.println("On Click Have ProductBase? " + user.getProductDataBase());

        Recpie recpie = recipesList.getRecipes().get(position);
        Intent intent = new Intent(getActivity(), RecipeActivity.class);
        String jsonRecipe = gson.toJson(recpie);
        String jsonDataBase = gson.toJson(user.getProductDataBase());
        intent.putExtra(KeysIntents.SEND_RECIPE, jsonRecipe);
        intent.putExtra(KeysIntents.SEND_PRODUCT_DATABASE, jsonDataBase);


        startActivity(intent);

    }

    private void recyclerViewAdapter(RecyclerView recyclerView, Recipes recipes){

        RecipeRecyclerAdapter adapter = new RecipeRecyclerAdapter(recipes, getLayoutInflater(), RecipesFragmentTab.this);
        recyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_diet_fragment_meal_recycler));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);


    }
}
