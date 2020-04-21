package com.example.fitnessapp.main;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fitnessapp.MainActivity;
import com.example.fitnessapp.R;
import com.example.fitnessapp.keys.KeysBundle;
import com.example.fitnessapp.keys.KeysIntents;
import com.example.fitnessapp.models.ChangeProductImageDietFragment;
import com.example.fitnessapp.user.Diet;
import com.example.fitnessapp.user.Meal;
import com.example.fitnessapp.user.NutritionalValues;
import com.example.fitnessapp.user.Product;
import com.example.fitnessapp.user.ProductDataBase;
import com.example.fitnessapp.user.User;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

public class DietFragment extends Fragment implements DietRecyclerAdapter.OnMealLisiner {

    private DietViewModel mViewModel;
    private User user;
    private Diet diet;

    private NutritionalValues nutritionalValues;
    private TextView calNumber;
    private TextView calLabel;
    private ImageView calImage;
    private TextView proNumber;
    private TextView proLabel;
    private TextView fatNumber;
    private TextView fatLabel;
    private TextView carbohNumber;
    private TextView carboLebel;
    private PieChart pieChart;



    public static DietFragment newInstance() {
        return new DietFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.diet_fragment, container, false);

        //without this contact animation crashing if change fragment before the animation finish
        MainActivity contextFragment = (MainActivity) getActivity();


        calNumber = v.findViewById(R.id.number_of_cal_tv);
        calLabel = v.findViewById(R.id.textView10);
        calImage = v.findViewById(R.id.cal_graph);

        proNumber = v.findViewById(R.id.number_of_pro_tv);
        proLabel = v.findViewById(R.id.textView15);

        fatNumber = v.findViewById(R.id.number_of_fat_tv);
        fatLabel = v.findViewById(R.id.textView13);

        carbohNumber = v.findViewById(R.id.number_of_carboh_tv);
        carboLebel = v.findViewById(R.id.textView14);

        pieChart = v.findViewById(R.id.pieGraph_nat);

        user = (User) getArguments().getSerializable(KeysBundle.USER_DATA_TO_FRAGMENT);
        System.out.println("Diet" + user);
        System.out.println("Diet - " + user.getDiet().toString());

        diet = user.getDiet();
        nutritionalValues = new NutritionalValues(user);

        calNumber.setText(String.valueOf(nutritionalValues.getCal()));
        proNumber.setText(String.valueOf(nutritionalValues.getPro()));
        fatNumber.setText(String.valueOf(nutritionalValues.getFat()));
        carbohNumber.setText(String.valueOf(nutritionalValues.getCarboh()));

        nutriPie();
        v.postDelayed(new Runnable() {
            @Override
            public void run() {
                animationPlatte(contextFragment);
            }
        }, 500);

        final RecyclerView recyclerView = v.findViewById(R.id.diet_recyclerView);
        DietRecyclerAdapter adapter = new DietRecyclerAdapter(diet, user, getLayoutInflater(),this, contextFragment);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(contextFragment,R.anim.layout_diet_fragment_meal_recycler));
        recyclerView.setAdapter(adapter);





        //without the delayed itemView crashing!!
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {

                int x = 0;
                List<Meal> meals = user.getDiet().getMeals();
                System.out.println("meals.size " + meals.size());
                //TODO: Crash if the meal box not show right in the UI // like at the field exsercie history.
                for (int i = 0; i < 0; i++) { // need to change 0 to (meals.size() - 1), put 0 just for not to be crash

                    View itemView = recyclerView.getChildAt(i);
                    ImageView productImage = itemView.findViewById(R.id.iv_image_product);
                    List<ChangeProductImageDietFragment> changeProductImageDietFragmentList = new ArrayList<>();


                    List<Product> products = meals.get(i).getProducts();
                    List<ProductDataBase> productDataBaseList = new ArrayList<>();

                    for (int j = 0; j < products.size() ; j++) {

                        productDataBaseList.add(user.getProductDataBase().get(products.get(j).getProductName()));

                    }

                    changeProductImageDietFragmentList.add(new ChangeProductImageDietFragment(productImage, productDataBaseList, contextFragment));


                    for (ChangeProductImageDietFragment changeProductImageDietFragment : changeProductImageDietFragmentList) {

                        changeProductImageDietFragment.start();

                    }

                }

            }
        }, 5000);


//        List<Meal> meals = user.getDiet().getMeals();
//        List<ChangeProductImageDietFragment> changeProductImageDietFragmentList = new ArrayList<>();
//        for (int i = 0; i < meals.size(); i++) {
//
//            View itemView = recyclerView.getChildAt(i);
//            ImageView productImage = itemView.findViewById(R.id.productImageView);
//
//            List<Product> products = meals.get(i).getProducts();
//            List<ProductDataBase> productDataBaseList = new ArrayList<>();
//            for (int j = 0; j < products.size() ; j++) {
//
//                productDataBaseList.add(user.getProductDataBase().get(products.get(j).getProductName()));
//
//            }
//            changeProductImageDietFragmentList.add(new ChangeProductImageDietFragment(productImage, productDataBaseList));
//
//        }
//
//        for (ChangeProductImageDietFragment changeImage : changeProductImageDietFragmentList) {
//
//            changeImage.start();
//
//        }


        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(DietViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onMealClick(int position) {
        Meal meal = diet.getMeals().get(position);
        Dictionary<String, ProductDataBase> productDataBase = user.getProductDataBase();

        Intent intent = new Intent(getContext(), DietActivity.class);
        intent.putExtra(KeysIntents.DIET_DATA, meal);
        intent.putExtra(KeysIntents.SEND_USER, user);
        startActivity(intent);
        System.out.println("Click On Meal");
    }


    private void nutriPie(){

        pieChart.setVisibility(View.INVISIBLE);

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(-2, 8, -2, 5);

//        pieChart.setDragDecelerationFrictionCoef(0.50f);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(75f);
        pieChart.setHoleColor(Color.alpha(0));
        pieChart.setTransparentCircleRadius(55f);

        pieChart.setDrawEntryLabels(false);
        pieChart.getLegend().setEnabled(false);






        pieChart.postDelayed(new Runnable() {
            @Override
            public void run() {
                pieChart.setVisibility(View.VISIBLE);
                pieChart.animateY(2000, Easing.EaseInOutCirc);
            }
        }, 1_200);


        pieChart.getRenderer().getPaintRender().setShadowLayer(50,0,0, ContextCompat.getColor(getContext(),R.color.piechet_shadow));





        ArrayList<PieEntry> yValue = new ArrayList<>();

        yValue.add(new PieEntry(Float.parseFloat(proNumber.getText().toString()),"pro"));
        yValue.add(new PieEntry(Float.parseFloat(carbohNumber.getText().toString()),"carbh"));
        yValue.add(new PieEntry(Float.parseFloat(fatNumber.getText().toString()),"fat"));


        PieDataSet dataSet = new PieDataSet(yValue, "nutritional Values");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
//        dataSet.setColor(Color.RED);


        dataSet.setColors(proNumber.getCurrentTextColor(),carbohNumber.getCurrentTextColor(),fatNumber.getCurrentTextColor());

        dataSet.setDrawValues(false);



        PieData data = new PieData(dataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.BLUE);

        pieChart.setData(data);


    }

    private void animationPlatte(Context context){


        calImage.setVisibility(View.VISIBLE);
        calImage.setAnimation(AnimationUtils.loadAnimation(context,R.anim.diet_animation_faidin));

        calNumber.postDelayed(new Runnable() {
            @Override
            public void run() {

                calNumber.setVisibility(View.VISIBLE);
                calNumber.setAnimation(AnimationUtils.loadAnimation(context, R.anim.diet_animation_faidin));

                calLabel.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        calLabel.setVisibility(View.VISIBLE);
                        calLabel.setAnimation(AnimationUtils.loadAnimation(context,R.anim.diet_animation_faidin));

                        fatNumber.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                animationTextPlatte(fatNumber,fatLabel,context);

                                carbohNumber.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        animationTextPlatte(carbohNumber, carboLebel, context);

                                        proLabel.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {

                                                animationTextPlatte(proNumber, proLabel, context);

                                            }
                                        }, 300);

                                    }
                                }, 300);

                            }
                        }, 500);

                    }
                }, 500);

            }
        }, 500);

    }

    private void animationTextPlatte(TextView number, TextView label, Context context){

        number.setVisibility(View.VISIBLE);
        number.setAnimation(AnimationUtils.loadAnimation(context, R.anim.diet_animation_rtl));

        label.setVisibility(View.VISIBLE);
        label.setAnimation(AnimationUtils.loadAnimation(context, R.anim.diet_animation_rtl));

    }

}
