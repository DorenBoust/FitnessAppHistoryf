package com.example.fitnessapp.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.fitnessapp.R;
import com.example.fitnessapp.keys.KeysFirebaseStore;
import com.example.fitnessapp.keys.KeysIntents;
import com.example.fitnessapp.keys.KeysSharedPrefercence;
import com.example.fitnessapp.models.CustomMethods;
import com.example.fitnessapp.user.ExerciseHistory;
import com.example.fitnessapp.user.ExersixeOneRawHistory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class FitnessHistoryMainActivity extends AppCompatActivity implements FitnessHistoryReciclerAdapter.OnHistoryListener
{

    private AllHistoryExName allHistoryExName;
    private RecyclerView recyclerView;
    private LottieAnimationView lottieAnimationView;

    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private MutableLiveData<List<ExerciseHistory>> listMutableLiveData = new MutableLiveData<>();
    private int dateCounter = 0;

    private RecyclerView recyclerViewMainHistory;
    private List<ExerciseHistory> exerciseHistoriesRoot;
    private TextView exNameMain;
    private TextView exDayMain;
    private TextView exDateMain;
    private ImageView btnNextMain;
    private ImageView btnBackMain;
    private boolean btnCrash = true;
    private int lastPositionExThatClicked = 0;

    private Gson gson = new Gson();
    private SharedPreferences sharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness_history_main);

        recyclerViewMainHistory = findViewById(R.id.recycler_history_details_per_ex);
        lottieAnimationView = findViewById(R.id.lottieAnimationView_load_data);
        recyclerView = findViewById(R.id.recyclerview_fitness_history);
        exNameMain = findViewById(R.id.tv_ex_name_history_fitness_activity);
        exDayMain = findViewById(R.id.tv_ex_day_history_fitness_activity);
        exDateMain = findViewById(R.id.tv_ex_date_history_fitness_activity);
        btnNextMain = findViewById(R.id.btn_history_next_iv);
        btnBackMain = findViewById(R.id.btn_history_back_iv);
        sharedPreferences = getSharedPreferences(KeysSharedPrefercence.HISTORY_ACTIVITY, MODE_PRIVATE);


        lottieAnimationView.playAnimation();

        Intent intent = getIntent();
        Gson gson = new Gson();
        String stringExtra = intent.getStringExtra(KeysIntents.SEND_HISTORY_EXERCISE);
        allHistoryExName = gson.fromJson(stringExtra, AllHistoryExName.class);

        System.out.println("listOFExerciseFullHistory " + allHistoryExName);


        FitnessHistoryReciclerAdapter adapter = new FitnessHistoryReciclerAdapter(getLayoutInflater(),allHistoryExName, FitnessHistoryMainActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);






        //start
        exNameMain.setText(allHistoryExName.getHistoryExNames().get(lastPositionExThatClicked).getExName());
        getHistoryExFromFirebase(allHistoryExName.getHistoryExNames().get(0).getExName());


        listMutableLiveData.observe(this, new Observer<List<ExerciseHistory>>() {
            @Override
            public void onChanged(List<ExerciseHistory> exerciseHistories) {
                lottieAnimationView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dateCounter = 0;
                        exerciseHistoriesRoot = exerciseHistories;
                        System.out.println("exerciseHistoriesRoot " + exerciseHistoriesRoot );
                        lottieAnimationView.setVisibility(View.INVISIBLE);
                        System.out.println(exerciseHistories);
                        ExsercieHistoryRecyclerAdapter adapterMainHistory = new ExsercieHistoryRecyclerAdapter(exerciseHistories, dateCounter, getLayoutInflater());
                        recyclerViewMainHistory.setLayoutManager(new LinearLayoutManager(FitnessHistoryMainActivity.this));
                        recyclerViewMainHistory.setAdapter(adapterMainHistory);

                        exNameMain.setVisibility(View.VISIBLE);

                        exDayMain.setVisibility(View.VISIBLE);
                        exDateMain.setVisibility(View.VISIBLE);
                        btnBackMain.setVisibility(View.VISIBLE);
                        recyclerViewMainHistory.setVisibility(View.VISIBLE);

                        ConstraintLayout constraintLayout = recyclerView.getChildAt(lastPositionExThatClicked).findViewById(R.id.inner_constrain_ecycler);
                        constraintLayout.setBackground(ContextCompat.getDrawable(FitnessHistoryMainActivity.this, R.drawable.background_history_clicked));
                        constraintLayout.setElevation(0f);



                    }
                }, 2_000);

            }
        });

        btnBackMain.setOnClickListener(v->{
            if (btnCrash) {
                btnCrash = false;
                dateCounter++;
                btnNextMain.setVisibility(View.VISIBLE);
                btnBackMain.setAnimation(AnimationUtils.loadAnimation(this, R.anim.ex_activity_back_button));

                exDateMain.setText(exerciseHistoriesRoot.get(dateCounter).getDate());

                System.out.println("exerciseHistoriesRoot size = " + exerciseHistoriesRoot.size() + "/// dataCounter size = " + dateCounter);

                SimpleDateFormat dayNameFormat = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    Date dayName = dayNameFormat.parse(exDateMain.getText().toString());
                    SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
                    String dayNameString = outFormat.format(dayName);

                    System.out.println("dayNameString " + dayNameString);

                    if (!dayNameString.contains("י")) {
                        exDayMain.setText(CustomMethods.convertDate(dayNameString));

                    }

                    exDayMain.setText(dayNameString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }



                ExsercieHistoryRecyclerAdapter adapterMainHistory = new ExsercieHistoryRecyclerAdapter(exerciseHistoriesRoot, dateCounter, getLayoutInflater());
                recyclerViewMainHistory.setLayoutManager(new LinearLayoutManager(FitnessHistoryMainActivity.this));
                recyclerViewMainHistory.setAdapter(adapterMainHistory);

                if (dateCounter == exerciseHistoriesRoot.size() - 1) {
                    btnBackMain.setAnimation(AnimationUtils.loadAnimation(this, R.anim.faidout));
                    btnBackMain.setVisibility(View.INVISIBLE);

                }

                btnBackMain.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btnCrash = true;
                    }
                }, 500);

            }


        });

        btnNextMain.setOnClickListener(v->{
            if (btnCrash) {
                btnCrash = false;
                dateCounter--;
                btnBackMain.setVisibility(View.VISIBLE);
                btnNextMain.setAnimation(AnimationUtils.loadAnimation(this, R.anim.ex_activity_next_button));

                System.out.println("exerciseHistoriesRoot size = " + exerciseHistoriesRoot.size() + "/// dataCounter size = " + dateCounter);

                exDateMain.setText(exerciseHistoriesRoot.get(dateCounter).getDate());

                SimpleDateFormat dayNameFormat = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    Date dayName = dayNameFormat.parse(exDateMain.getText().toString());
                    SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
                    String dayNameString = outFormat.format(dayName);

                    System.out.println("dayNameString " + dayNameString);

                    if (!dayNameString.contains("י")) {
                        System.out.println("getIn");
                        System.out.println("CustomMethods.convertDate(dayNameString) " + CustomMethods.convertDate(dayNameString));
                        exDayMain.setText(CustomMethods.convertDate(dayNameString));

                    }

                    exDayMain.setText(dayNameString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }



                ExsercieHistoryRecyclerAdapter adapterMainHistory = new ExsercieHistoryRecyclerAdapter(exerciseHistoriesRoot, dateCounter, getLayoutInflater());
                recyclerViewMainHistory.setLayoutManager(new LinearLayoutManager(FitnessHistoryMainActivity.this));
                recyclerViewMainHistory.setAdapter(adapterMainHistory);

                if (dateCounter == 0) {
                    btnNextMain.setAnimation(AnimationUtils.loadAnimation(this, R.anim.faidout));
                    btnNextMain.setVisibility(View.INVISIBLE);

                }
                btnNextMain.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btnCrash = true;
                    }
                }, 500);

            }

        });


    }

    private void getHistoryExFromFirebase(String exName){

        try {
            if (!sharedPreferences.getString(exName,"").equals("")){

                exNameMain.setVisibility(View.INVISIBLE);
                exDayMain.setVisibility(View.INVISIBLE);
                exDateMain.setVisibility(View.INVISIBLE);
                btnBackMain.setVisibility(View.INVISIBLE);
                recyclerViewMainHistory.setVisibility(View.INVISIBLE);
                lottieAnimationView.setVisibility(View.VISIBLE);

                System.out.println("HAVE SHAREDPREFRENCE");

                List<ExerciseHistory> listExerciseHistory = new ArrayList<>();
                String json = sharedPreferences.getString(exName, "");
                Type type = new TypeToken<List<ExerciseHistory>>() {}.getType();
                listExerciseHistory = gson.fromJson(json, type);
                listMutableLiveData.setValue(listExerciseHistory);

                return;
            }

        }catch (Exception e){

        }


        exNameMain.setVisibility(View.INVISIBLE);
        exDayMain.setVisibility(View.INVISIBLE);
        exDateMain.setVisibility(View.INVISIBLE);
        btnBackMain.setVisibility(View.INVISIBLE);
        recyclerViewMainHistory.setVisibility(View.INVISIBLE);
        lottieAnimationView.setVisibility(View.VISIBLE);


        fStore.collection(KeysFirebaseStore.EXERCISE_HISTORY_DATA).document(fAuth.getUid())
                .collection(exName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                System.out.println("Connect to History Firebase");
                List<ExerciseHistory> listExerciseHistory = new ArrayList<>();

                System.out.println("task.getResult().size() - " + task.getResult().size());

                for (int i = 0; i < task.getResult().size(); i++) {
                    Map<String, Object> data = task.getResult().getDocuments().get(i).getData();
                    System.out.println("Data ----- " + data);

                    System.out.println("Name Class ------ " + data.getClass().getSimpleName());

                    Object exerciseHistories = data.get("exerciseHistories");
                    System.out.println("OBJECT --------- " + exerciseHistories);

                    Gson gson = new Gson();
                    String row = gson.toJson(exerciseHistories);

                    System.out.println("JSON NEWWWWWWW ----- " + row);

                    try {
                        JSONArray jsonArray = new JSONArray(row);
                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(j);
                            String jsonDate = (String) jsonObject.get("date");

                            System.out.println(jsonDate);

                            JSONArray jsonExList = (JSONArray) jsonObject.get("exList");

                            List<ExersixeOneRawHistory> listExersixeOneRawHistory = new ArrayList<>();

                            for (int k = 0; k < jsonExList.length(); k++) {
                                JSONObject jsonObject1 = (JSONObject) jsonExList.get(k);

                                Integer jsonSet = (Integer) jsonObject1.get("set");

                                Double jsonKG = (Double) jsonObject1.get("kg");

                                Integer jsonRepit = (Integer) jsonObject1.get("repit");


                                ExersixeOneRawHistory exersixeOneRawHistoryJSON = new ExersixeOneRawHistory(jsonSet, jsonRepit, jsonKG);
                                listExersixeOneRawHistory.add(exersixeOneRawHistoryJSON);
                            }

                            ExerciseHistory exerciseHistoryJSON = new ExerciseHistory(jsonDate, listExersixeOneRawHistory);
                            listExerciseHistory.add(exerciseHistoryJSON);


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                listMutableLiveData.setValue(listExerciseHistory);

                SharedPreferences.Editor exHistoryEdit = sharedPreferences.edit();
                String exHistoryGSON = gson.toJson(listExerciseHistory);
                exHistoryEdit.putString(exName, exHistoryGSON);
                exHistoryEdit.apply();


//                exerciseHistoryRoot = listExerciseHistory;
//                counterHistoryBTN = exerciseHistoryRoot.size();
//                getCorrectHistory = exerciseHistoryRoot.size() - 1;
//                historyRecyclerView(exerciseHistoryRoot, getCorrectHistory);
            }

        });


    }


    @Override
    public void OnHistoryClick(int position) {
        if (position == lastPositionExThatClicked) return;

        System.out.println("allHistoryExName.getHistoryExNames().get(position).getExName() " + allHistoryExName.getHistoryExNames().get(position).getExName());
        getHistoryExFromFirebase(allHistoryExName.getHistoryExNames().get(position).getExName());
        exNameMain.setText(allHistoryExName.getHistoryExNames().get(position).getExName());

        ConstraintLayout constraintLayout = recyclerView.getChildAt(position).findViewById(R.id.inner_constrain_ecycler);
        constraintLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.background_history_clicked));
        constraintLayout.setElevation(0f);

        ConstraintLayout constraintLayoutBefor = recyclerView.getChildAt(lastPositionExThatClicked).findViewById(R.id.inner_constrain_ecycler);
        constraintLayoutBefor.setBackground(ContextCompat.getDrawable(this, R.drawable.background_main_fitness_day));
        constraintLayoutBefor.setElevation(16f);

        lastPositionExThatClicked = position;


    }
}
