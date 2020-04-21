package com.example.fitnessapp.main;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fitnessapp.R;
import com.example.fitnessapp.articals.ArticalsFragmentTab;
import com.example.fitnessapp.articals.PageAdapter;
import com.example.fitnessapp.articals.Recipes;
import com.example.fitnessapp.articals.RecipesFragmentTab;
import com.example.fitnessapp.keys.KeysBundle;
import com.example.fitnessapp.user.User;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class ArticlesFragment extends Fragment {

    private ArticlesViewModel mViewModel;
    private User user;
    private Recipes recipes;

    //tab
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PageAdapter pageAdapter;
    private TabItem tabRecipes;
    private TabItem tabArticals;

    private RecipesFragmentTab recipesFragmentTab;
    private ArticalsFragmentTab articalsFragmentTab;

    public static ArticlesFragment newInstance() {
        return new ArticlesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.articles_fragment, container, false);

        user = (User) getArguments().getSerializable(KeysBundle.USER_DATA_TO_FRAGMENT);
        System.out.println("Articals ++ " + user);

        recipes = (Recipes) getArguments().getSerializable(KeysBundle.RECIPE_TO_FRAGMENT);
        System.out.println("Articals ++ " + recipes);




        tabLayout = v.findViewById(R.id.layoutTab);
        tabRecipes = v.findViewById(R.id.tabRecipes);
        tabArticals = v.findViewById(R.id.tabArticals);
        viewPager = v.findViewById(R.id.viewPager);
        tabLayout = v.findViewById(R.id.layoutTab);

        recipesFragmentTab = new RecipesFragmentTab(recipes, user);
        articalsFragmentTab = new ArticalsFragmentTab();

        tabLayout.setupWithViewPager(viewPager);

        PageAdapter pageAdapter = new PageAdapter(getChildFragmentManager(), 0);
        pageAdapter.addFragment(recipesFragmentTab, "מתכונים");
        pageAdapter.addFragment(articalsFragmentTab, "מאמרים");
        viewPager.setAdapter(pageAdapter);



        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ArticlesViewModel.class);
        // TODO: Use the ViewModel
    }

}
