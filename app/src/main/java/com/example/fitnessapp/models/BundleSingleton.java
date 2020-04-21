package com.example.fitnessapp.models;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.example.fitnessapp.articals.Recipes;
import com.example.fitnessapp.articals.Recpie;
import com.example.fitnessapp.keys.KeysBundle;
import com.example.fitnessapp.user.Exercise;
import com.example.fitnessapp.user.Meal;
import com.example.fitnessapp.user.User;

import java.util.List;

public class BundleSingleton {

    private BundleSingleton(){}
    private static Bundle bundle = new Bundle();

    public static void setUser(User user, Fragment fragment){

        bundle.putSerializable(KeysBundle.USER_DATA_TO_FRAGMENT, user);
        fragment.setArguments(bundle);

    }

    public static void setRecipe(Recipes recpies, Fragment fragment){

        bundle.putSerializable(KeysBundle.RECIPE_TO_FRAGMENT, recpies);

    }


}
