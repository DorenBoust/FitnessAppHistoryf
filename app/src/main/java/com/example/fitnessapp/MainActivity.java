package com.example.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import android.app.Notification;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fitnessapp.articals.AsyncArticalsJSON;
import com.example.fitnessapp.articals.Recipes;
import com.example.fitnessapp.articals.Recpie;
import com.example.fitnessapp.keys.KeysFirebaseStore;
import com.example.fitnessapp.keys.KeysSharedPrefercence;
import com.example.fitnessapp.main.ArticlesFragment;
import com.example.fitnessapp.main.DietFragment;
import com.example.fitnessapp.main.FitnessFragment;
import com.example.fitnessapp.main.SettingsFragment;
import com.example.fitnessapp.main.StatusFragment;
import com.example.fitnessapp.models.BundleSingleton;
import com.example.fitnessapp.models.NotificationDietThread;
import com.example.fitnessapp.user.AsyncJSON;
import com.example.fitnessapp.models.FireBaseSingleton;
import com.example.fitnessapp.user.Diet;
import com.example.fitnessapp.user.Exercise;
import com.example.fitnessapp.user.Meal;
import com.example.fitnessapp.user.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.fitnessapp.models.AppNotification.CHANNEL_2_ID;

public class MainActivity extends AppCompatActivity {

    private MutableLiveData<User> userJsonLiveData = new MutableLiveData<>();
    private MutableLiveData<Recipes> recpiesJsonLiveData = new MutableLiveData<>();
    private User userObject;
    private Recipes recpiesList;
    private ConstraintLayout splash;
    private ConstraintLayout menuBar;


    private int lastClicked = 1;
    private boolean iconSwitch = false;

    private AnimatedVectorDrawableCompat avd;
    private AnimatedVectorDrawable avd2;

    private ImageView iconStatus;
    private ImageView iconLineStatus;
    private boolean statusClicked = false;

    private ImageView iconFitness;
    private ImageView iconLineFitness;
    private boolean fitnessClicked = false;

    private ImageView iconDiet;
    private ImageView iconLineDiet;
    private boolean dietClicked = false;

    private ImageView iconArticles;
    private ImageView iconLineArticles;
    private boolean articlesClicked = false;

    private ImageView iconSetting;
    private ImageView iconLineSetting;
    private boolean settingClicked = false;

    //notification
    private NotificationManagerCompat notificationManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        splash = findViewById(R.id.splash);
        menuBar = findViewById(R.id.menuBar);

        iconStatus = findViewById(R.id.menu_icon_status);
        iconLineStatus = findViewById(R.id.iconline_status);
        iconFitness = findViewById(R.id.menu_icon_fitness);
        iconLineFitness = findViewById(R.id.iconline_fitness);
        iconStatus.setImageDrawable(getResources().getDrawable(R.drawable.status_animation));
        iconDiet = findViewById(R.id.menu_icon_diet);
        iconLineDiet = findViewById(R.id.iconline_diet);
        iconArticles = findViewById(R.id.menu_icon_articals);
        iconLineArticles = findViewById(R.id.iconline_articals);
        iconSetting = findViewById(R.id.menu_icon_settings);
        iconLineSetting = findViewById(R.id.iconline_setting);

        notificationManager = NotificationManagerCompat.from(this);




        jsonParser();



        recpiesJsonLiveData.observe(this, new Observer<Recipes>() {
            @Override
            public void onChanged(Recipes recpies) {

                recpiesList = recpies;
                ArticlesFragment articlesFragment = new ArticlesFragment();
                BundleSingleton.setRecipe(recpies, articlesFragment);

            }
        });




        userJsonLiveData.observe(this, new Observer<User>() {
           @Override

           public void onChanged(User user) {

               userObject = user;
               StatusFragment statusFragment = new StatusFragment();

               BundleSingleton.setUser(userObject, statusFragment);


               splash.setAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.faidout));
               splash.setVisibility(View.INVISIBLE);
               menuBar.setAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.faidin));
               menuBar.setVisibility(View.VISIBLE);
               getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.faidin, R.anim.faidout).replace(R.id.mainFragment, statusFragment).commit();




               NotificationDietThread notificationDietThread = new NotificationDietThread(MainActivity.this, notificationManager, userObject);

               notificationDietThread.start();


           }
       });










        iconStatus.setOnClickListener(v->{
            if (!statusClicked){
                mainIconAnimation(iconStatus,iconLineStatus,iconSwitch,R.drawable.status_animation,R.drawable.status_stati);

                lastClicked = 1;
                statusClicked = true;
                fitnessClicked = false;
                dietClicked = false;
                settingClicked = false;
                articlesClicked = false;


            }

        });


        iconFitness.setOnClickListener(v->{
            if (!fitnessClicked) {
                mainIconAnimation(iconFitness,iconLineFitness, iconSwitch, R.drawable.fitness_animation, R.drawable.fitness_stati);


                lastClicked = 2;
                fitnessClicked = true;
                statusClicked = false;
                dietClicked = false;
                settingClicked = false;
                articlesClicked = false;


            }
        });

        iconDiet.setOnClickListener(v->{
            if (!dietClicked) {
                mainIconAnimation(iconDiet,iconLineDiet,iconSwitch, R.drawable.diet_animation, R.drawable.diet_stati);

                lastClicked = 3;
                dietClicked = true;
                statusClicked = false;
                fitnessClicked = false;
                settingClicked = false;
                articlesClicked = false;

            }
        });

        iconArticles.setOnClickListener(v->{
            if ((!articlesClicked)){
                mainIconAnimation(iconArticles,iconLineArticles,iconSwitch,R.drawable.articels_animation,R.drawable.articels_stati);

                lastClicked = 4;
                articlesClicked = true;
                statusClicked = false;
                dietClicked = false;
                fitnessClicked = false;
                settingClicked = false;


            }
        });

        iconSetting.setOnClickListener(v->{
            if (!settingClicked) {
                mainIconAnimation(iconSetting, iconLineSetting,iconSwitch, R.drawable.menu_animation, R.drawable.menu_static);

                lastClicked = 5;
                settingClicked = true;
                dietClicked = false;
                statusClicked = false;
                fitnessClicked = false;
                articlesClicked = false;

            }
        });



    }

    private void jsonParser(){

        DocumentReference documentReference = FireBaseSingleton.documentReference(KeysFirebaseStore.COLLECTION_USER);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    String integrationCode = documentSnapshot.getString(KeysFirebaseStore.INTEGRATION_CODE);
                    String email = documentSnapshot.getString(KeysFirebaseStore.EMAIL);
                    new AsyncJSON(userJsonLiveData).execute(integrationCode, email);
                    new AsyncArticalsJSON(recpiesJsonLiveData).execute();
                }else {
                    Toast.makeText(MainActivity.this, "Doc not exesite", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void mainIconAnimation(ImageView icon,ImageView iconLine, boolean iconSwitch, int drawableResFirstAnimation, int drawableResSecAnimation){



        List<Drawable> statiImages = new ArrayList<>();
        statiImages.add(getResources().getDrawable(R.drawable.status_stati));
        statiImages.add(getResources().getDrawable(R.drawable.fitness_stati));
        statiImages.add(getResources().getDrawable(R.drawable.diet_stati));
        statiImages.add(getResources().getDrawable(R.drawable.articels_stati));
        statiImages.add(getResources().getDrawable(R.drawable.menu_static));

        List<ImageView> iconsNames = new ArrayList<>();
        iconsNames.add(iconStatus);
        iconsNames.add(iconFitness);
        iconsNames.add(iconDiet);
        iconsNames.add(iconArticles);
        iconsNames.add(iconSetting);

        List<ImageView> iconLines = new ArrayList<>();
        iconLines.add(iconLineStatus);
        iconLines.add(iconLineFitness);
        iconLines.add(iconLineDiet);
        iconLines.add(iconLineArticles);
        iconLines.add(iconLineSetting);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new StatusFragment());
        fragments.add(new FitnessFragment());
        fragments.add(new DietFragment());
        fragments.add(new ArticlesFragment());
        fragments.add(new SettingsFragment());




        //set icon images static
        for (int i = 0; i < statiImages.size() ; i++) {
            if (icon != iconsNames.get(i))
            iconsNames.get(i).setImageDrawable(statiImages.get(i));
        }


        //Animation icon Lines
        for (int i = 0; i < iconLines.size() ; i++) {
            if (iconLine != iconLines.get(i)){
                faidoutIconLine(iconLines.get(i));
            }else {
                faidinIconLine(iconLines.get(i));

                Fragment fragment = fragments.get(i);
                BundleSingleton.setUser(userObject, fragment);
//                BundleSingleton.setRecipe(recpiesList,fragment);
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.faidin,R.anim.faidout,R.anim.faidin,R.anim.faidout).replace(R.id.mainFragment, fragment).commitNow();



            }
        }


        //do the animation
        if(!iconSwitch){
            icon.setImageDrawable(getResources().getDrawable(drawableResFirstAnimation));
            Drawable drawable = icon.getDrawable();

            if(drawable instanceof AnimatedVectorDrawableCompat){
                avd = (AnimatedVectorDrawableCompat) drawable;
                avd.start();
            }else if (drawable instanceof AnimatedVectorDrawable){
                avd2 = (AnimatedVectorDrawable) drawable;
                avd2.start();
            }

            iconSwitch = true;

        }else {
            icon.setImageDrawable(getResources().getDrawable(drawableResSecAnimation));
            Drawable drawable = icon.getDrawable();

            if(drawable instanceof AnimatedVectorDrawableCompat){
                avd = (AnimatedVectorDrawableCompat) drawable;
                avd.start();
            }else if (drawable instanceof AnimatedVectorDrawable){
                avd2 = (AnimatedVectorDrawable) drawable;
                avd2.start();
            }

            iconSwitch = false;

        }
    }

    private void faidinIconLine(ImageView iconLine){
        iconLine.setVisibility(View.VISIBLE);
        iconLine.setAnimation(AnimationUtils.loadAnimation(this,R.anim.menu_line_animation_faidin));

    }

    private void faidoutIconLine(ImageView iconLine){

        switch (lastClicked) {
            case 1:
                iconLineStatus.setAnimation(AnimationUtils.loadAnimation(this,R.anim.menu_line_animation_faidout));
                break;
            case 2:
                iconLineFitness.setAnimation(AnimationUtils.loadAnimation(this,R.anim.menu_line_animation_faidout));
                break;
            case 3:
                iconLineDiet.setAnimation(AnimationUtils.loadAnimation(this,R.anim.menu_line_animation_faidout));
                break;
            case 4:
                iconLineArticles.setAnimation(AnimationUtils.loadAnimation(this,R.anim.menu_line_animation_faidout));
                break;
            case 5:
                iconLineSetting.setAnimation(AnimationUtils.loadAnimation(this,R.anim.menu_line_animation_faidout));
                break;
        }
        iconLine.setVisibility(View.INVISIBLE);
    }



}
