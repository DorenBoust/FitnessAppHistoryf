package com.example.fitnessapp.models;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.fitnessapp.R;
import com.example.fitnessapp.user.ProductDataBase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ChangeProductImageDietFragment {


    private ImageView productImage;
    private List<ProductDataBase> productDataBases;
    private Context context;

    public ChangeProductImageDietFragment(ImageView productImage, List<ProductDataBase> productDataBases, Context context) {
        this.productImage = productImage;
        this.productDataBases = productDataBases;
        this.context = context;
    }

    public void start(){



        List<String> productsImages = new ArrayList<>();
        for (ProductDataBase productDataBase : productDataBases) {

            productsImages.add(productDataBase.getProductImage());

        }



        CountDownTimer countDownTimer = new CountDownTimer(productsImages.size() * 3_000, 3_000) {

            int i = 0;
            @Override
            public void onTick(long millisUntilFinished) {

                productImage.setAnimation(AnimationUtils.loadAnimation(context, R.anim.faidout));
                productImage.setVisibility(View.INVISIBLE);
                Picasso.get().load(productsImages.get(i++ % productsImages.size())).into(productImage);
                productImage.setVisibility(View.VISIBLE);
                productImage.setAnimation(AnimationUtils.loadAnimation(context, R.anim.faidin));


            }

            @Override
            public void onFinish() {


                this.start();

            }
        }.start();


    }
}
