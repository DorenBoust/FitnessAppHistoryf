package com.example.fitnessapp.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.fitnessapp.R;
import com.example.fitnessapp.keys.KeysFirebaseStore;
import com.example.fitnessapp.keys.KeysIntents;
import com.example.fitnessapp.models.CustomMethods;
import com.example.fitnessapp.user.ExerciseFullHistory;
import com.example.fitnessapp.user.ExerciseHistory;
import com.example.fitnessapp.user.ExersixeOneRawHistory;
import com.example.fitnessapp.user.ListExHistory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.ramotion.foldingcell.FoldingCell;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class FitnessHistoryMainActivity extends AppCompatActivity {

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


        lottieAnimationView.playAnimation();

        Intent intent = getIntent();
        Gson gson = new Gson();
        String stringExtra = intent.getStringExtra(KeysIntents.SEND_HISTORY_EXERCISE);
        allHistoryExName = gson.fromJson(stringExtra, AllHistoryExName.class);

        System.out.println("listOFExerciseFullHistory " + allHistoryExName);


        FitnessHistoryReciclerAdapter adapter = new FitnessHistoryReciclerAdapter(getLayoutInflater(),allHistoryExName);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        //start
        getHistoryExFromFirebase(allHistoryExName.getHistoryExNames().get(0).getExName());
        listMutableLiveData.observe(this, new Observer<List<ExerciseHistory>>() {
            @Override
            public void onChanged(List<ExerciseHistory> exerciseHistories) {
                lottieAnimationView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dateCounter = 0;
                        exerciseHistoriesRoot = exerciseHistories;
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
//                exerciseHistoryRoot = listExerciseHistory;
//                counterHistoryBTN = exerciseHistoryRoot.size();
//                getCorrectHistory = exerciseHistoryRoot.size() - 1;
//                historyRecyclerView(exerciseHistoryRoot, getCorrectHistory);
            }

        });


    }


}
