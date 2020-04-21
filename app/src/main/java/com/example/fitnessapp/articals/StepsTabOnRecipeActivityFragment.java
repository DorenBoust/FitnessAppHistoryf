package com.example.fitnessapp.articals;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fitnessapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepsTabOnRecipeActivityFragment extends Fragment {

    private Recpie recpie;



    public StepsTabOnRecipeActivityFragment(Recpie recpie) {

        this.recpie = recpie;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_steps_tab_on_recipe_activity, container, false);

        RecyclerView recyclerViewProduct = v.findViewById(R.id.steps_recycler_recipe_activity);
        StepsTabRecyclerAdapder adapter = new StepsTabRecyclerAdapder(recpie, getLayoutInflater());
        recyclerViewProduct.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewProduct.setAdapter(adapter);

        return v;
    }
}
