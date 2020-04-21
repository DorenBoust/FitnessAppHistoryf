package com.example.fitnessapp.articals;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fitnessapp.R;
import com.example.fitnessapp.user.ProductDataBase;
import com.example.fitnessapp.user.User;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ProductTabOnRecipeActivityFragment extends Fragment {

    private Recpie recpie;
    private User user;
    private Map<String, ProductDataBase> productDataBase;

    public ProductTabOnRecipeActivityFragment(Recpie recpie, Map<String, ProductDataBase> productDataBase) {

        this.recpie = recpie;
        this.productDataBase = productDataBase;


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //todo: init recycler product
        View v = inflater.inflate(R.layout.fragment_product_tab_on_recipe_activity, container, false);

        RecyclerView recyclerViewProduct = v.findViewById(R.id.recyclerview_product_recipe_activity);
        ProductTabRecyclerAdapter adapter = new ProductTabRecyclerAdapter(recpie, getLayoutInflater(), productDataBase);
        recyclerViewProduct.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewProduct.setAdapter(adapter);

        return v;
    }
}
