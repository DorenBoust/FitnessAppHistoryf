package com.example.fitnessapp.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.ArraySet;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fitnessapp.R;
import com.example.fitnessapp.keys.KeysFirebaseStore;
import com.example.fitnessapp.keys.KeysIntents;
import com.example.fitnessapp.keys.KeysSharedPrefercence;
import com.example.fitnessapp.models.CustomMethods;
import com.example.fitnessapp.user.Exercise;
import com.example.fitnessapp.user.ExerciseHistory;
import com.example.fitnessapp.user.ExersixeOneRawHistory;
import com.example.fitnessapp.user.HistoryExName;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;


import static com.example.fitnessapp.models.AppNotification.CHANNEL_1_ID;

public class ExersiceActivity extends AppCompatActivity {

    //navigation ex btn
    private ImageView btnNext;
    private ImageView btnBack;
    private Button btnFinishEx;
    private boolean crash = true;

    private TextView tvSets;
    private TextView tvRepit;
    private TextView tvNote;
    private TextView tvNoteTitle;
    private TextView tvExName;
    private TextView tvExNumber;
    private TextView tvDay;
    private ImageView ivMainImage;
    private int counterEx = 0;


    //timer
    private TextView tvTimer;
    private Button btnTimerStop;
    private Button btnTimerStart;
    private Button btnTimerResume;
    private Button btnTimerClear;
    private long timerTime;
    private CountDownTimer timerCount;
    private boolean mTimerRunning;
    private long mTimerLeft;

    //notification
    private NotificationManagerCompat notificationManager;

    //recyclerview field details
    private RecyclerView recyclerViewComponent;
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private RecyclerView.Adapter adapter1;
    private int itemCount;
    private ConstraintLayout layoutOfRecyclerFieldDetails;


    //history
    private TextView btnHistory;
    private ConstraintLayout historyLayout;
    private boolean historyClicked = false;
    private TextView tvDateHistory;
    private TextView tvDayHistory;
    private int counterHistoryBTN = 0;
    int getCorrectHistory;
    private List<ExerciseHistory> exerciseHistoryRoot;
    private ImageView btnHistoryBack;
    private ImageView btnHistoryNext;
    private TextView recyclerTitle;
    private TextView noHistory;
    private RecyclerView recyclerView;
    private Gson gson = new Gson();


    //youtube
    private ImageView youtubeBTN;
    private ConstraintLayout youtubeLayout;
    private YouTubePlayerView youTubePlayerView;

    //stop workout
    private ImageView stopWorkoutBTN;
    private Button dialogYesBTN;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exersice);

        btnBack = findViewById(R.id.iv_ex_activity_title_backGreen);
        btnNext = findViewById(R.id.iv_ex_activity_title_nextGreen);
        btnFinishEx = findViewById(R.id.btn_finish_ex);
        tvDay = findViewById(R.id.tv_ex_activity_title_day);
        tvSets = findViewById(R.id.tv_ex_activity_details_set);
        tvRepit = findViewById(R.id.tv_ex_activity_details_repit);
        tvNote = findViewById(R.id.tv_ex_activity_details_notes);
        tvNoteTitle = findViewById(R.id.textView11);
        tvExName = findViewById(R.id.tv_ex_activity_title_exName);
        tvExNumber = findViewById(R.id.tv_ex_activity_title_exNumber);
        ivMainImage = findViewById(R.id.iv_ex_activity_details_exImage);

        tvTimer = findViewById(R.id.ex_activity_timer);
        btnTimerStart = findViewById(R.id.ex_activity_timerStart);
        btnTimerStop = findViewById(R.id.ex_activity_timerStop);
        btnTimerResume = findViewById(R.id.ex_activity_timerResume);
        btnTimerClear = findViewById(R.id.ex_activity_timerClear);
        notificationManager = NotificationManagerCompat.from(this);

        recyclerViewComponent = findViewById(R.id.tv_ex_activity_details_recycler);
        layoutOfRecyclerFieldDetails = findViewById(R.id.layout_field_details_for_history);

        btnHistory = findViewById(R.id.history_button);
        historyLayout = findViewById(R.id.layout_history);
        tvDateHistory = findViewById(R.id.date_history_title);
        tvDayHistory = findViewById(R.id.day_history_title);
        btnHistoryBack = findViewById(R.id.history_back);
        btnHistoryNext = findViewById(R.id.history_next);
        recyclerTitle = findViewById(R.id.tv_ex_activity_title_exName2);
        noHistory = findViewById(R.id.no_history);
        recyclerView = findViewById(R.id.history_recyclerview);

        youtubeBTN = findViewById(R.id.iv_ex_activity_details_playBTN);
        youtubeLayout = findViewById(R.id.youtubeLayout);
        youTubePlayerView = findViewById(R.id.youtubeVideoPlayer);

        stopWorkoutBTN = findViewById(R.id.stop_workout);




        SharedPreferences sharedPreferences = getSharedPreferences(KeysSharedPrefercence.USER_SHAREDPREFERCENCE_NAME, Context.MODE_PRIVATE);

        int spCorrectExersice = sharedPreferences.getInt(KeysSharedPrefercence.CORRECT_EXERCISE, 6);
        System.out.println("TEST SP 1 =" + spCorrectExersice);

        if (spCorrectExersice != 0){
            counterEx = spCorrectExersice;
        }




        //get data
        Intent intent = getIntent();
        List<Exercise> exercises = (List<Exercise>) intent.getSerializableExtra(KeysIntents.EX_LIST);
        String dayName = intent.getStringExtra(KeysIntents.DAY_NAME);

        timerTime = exercises.get(counterEx).getRest();
        mTimerLeft = timerTime;




        //recycler recyclerViewComponent
        Exercise exercise = exercises.get(counterEx);
        ExersiceFieldRecyclerAdapter adapter = new ExersiceFieldRecyclerAdapter(exercise,getLayoutInflater());
        System.out.println("number of ex " + exercise);
        recyclerViewComponent.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewComponent.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(this,R.anim.layout_ex_activity_recycler_field));
        recyclerViewComponent.setAdapter(adapter);

        //regular component
        regularComponents(exercises,dayName);
        getHistoryExFromFirebase();

        //youtube
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                String youtoubeID = youtubeID(exercises);
                youTubePlayer.loadVideo(youtoubeID,0);
                youTubePlayer.pause();
            }
        });


        //navigation arrow btn
        btnNext.setOnClickListener(v->{
            if (crash) {


                boolean dialogstatus = true;

                //get the recyclerview edit text and export to firebase store
                RecyclerView.Adapter adapter1 = recyclerViewComponent.getAdapter();
                int itemCount = adapter1.getItemCount();
                System.out.println(itemCount);

                List<ExersixeOneRawHistory> exersixeOneRawHistories = new ArrayList<>();
                for (int i = 0; i < itemCount ; i++) {
//                    RecyclerView.ViewHolder viewHolderForAdapterPosition = recyclerViewComponent.findViewHolderForAdapterPosition(i);
                    View view = recyclerViewComponent.getChildAt(i);
                    System.out.println("number of i" + i);

                    //TODO:CRASH ON 5 SETS!! Look why
                    EditText etRepit = (EditText) view.findViewById(R.id.ex_activity_recycler_repit);
                    System.out.println(i);
                    Integer repit = null;
                    if (!etRepit.getText().toString().equals("")) {
                        repit = Integer.parseInt(etRepit.getText().toString());
                    }

                    EditText etKG = (EditText) view.findViewById(R.id.ex_activity_recycler_kg);
                    Double kg = null;
                    if (!etKG.getText().toString().equals("")) {
                        kg = Double.parseDouble(etKG.getText().toString());
                    }

                    if (kg != null && repit != null) {
                        exersixeOneRawHistories.add(new ExersixeOneRawHistory((i + 1), repit, kg));
                        if (i == itemCount - 1){
                            dialogstatus = false;
                        }
                    }

                }

                if (dialogstatus){
                    showDialog();
                    return;
                }


                System.out.println(exersixeOneRawHistories);

                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                System.out.println();

                ExerciseHistory exerciseHistory = new ExerciseHistory(currentDate, exersixeOneRawHistories);
                List<ExerciseHistory> exerciseHistoryList = new ArrayList<>();
                exerciseHistoryList.add(exerciseHistory);

                ExerciseHistoryToFIreBase exerciseHistoryToFIreBase = new ExerciseHistoryToFIreBase(exerciseHistoryList);

                System.out.println(exerciseHistory);
                Task<Void> saveOnDB = fStore.collection(KeysFirebaseStore.EXERCISE_HISTORY_DATA).document(fAuth.getUid())
                        .collection(tvExName.getText().toString()).document(currentDate).set(exerciseHistoryToFIreBase);


                //to make activity history of all the exercise that user done this func save as firebase field list of all the exNames
                fireBaseHistoryExNames(tvExName.getText().toString(),exercises.get(counterEx).getImage());



                crash = false;

                //change recycler field data
                Exercise exerciseIN = exercises.get(++counterEx);
                ExersiceFieldRecyclerAdapter adapterIN = new ExersiceFieldRecyclerAdapter(exerciseIN, getLayoutInflater());
                recyclerViewComponent.setLayoutManager(new LinearLayoutManager(this));
                recyclerViewComponent.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(this,R.anim.layout_ex_activity_recycler_field));
                recyclerViewComponent.setAdapter(adapterIN);


                //regular Component
                regularComponents(exercises, dayName);

                //display arrow
                if (counterEx == exercises.size() - 1) {

                    btnNext.setAnimation(AnimationUtils.loadAnimation(this, R.anim.faidout));
                    btnNext.setVisibility(View.INVISIBLE);
                    btnFinishEx.setAnimation(AnimationUtils.loadAnimation(this,R.anim.faidin));
                    btnFinishEx.setVisibility(View.VISIBLE);
                } else {
                    btnNext.setAnimation(AnimationUtils.loadAnimation(this, R.anim.ex_activity_next_button));
                }

//                if (counterEx == 1){
//                    btnBack.setAnimation(AnimationUtils.loadAnimation(this, R.anim.faidin));
//                    btnBack.setVisibility(View.VISIBLE);
//                }

                btnNext.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        crash = true;
                    }
                }, 500);
            }


            //change recycler history
            getHistoryExFromFirebase();
            System.out.println("getHistory TURNON");

        });

        btnFinishEx.setOnClickListener(v->{
            boolean dialogstatus = true;

            //get the recyclerview edit text and export to firebase store
            RecyclerView.Adapter adapter1 = recyclerViewComponent.getAdapter();
            int itemCount = adapter1.getItemCount();
            System.out.println(itemCount);

            List<ExersixeOneRawHistory> exersixeOneRawHistories = new ArrayList<>();
            for (int i = 0; i < itemCount ; i++) {
                RecyclerView.ViewHolder viewHolderForAdapterPosition = recyclerViewComponent.findViewHolderForAdapterPosition(i);
                View view = recyclerViewComponent.getChildAt(i);

                EditText etRepit = (EditText) view.findViewById(R.id.ex_activity_recycler_repit);
                Integer repit = null;
                if (!etRepit.getText().toString().equals("")) {
                    repit = Integer.parseInt(etRepit.getText().toString());
                }

                EditText etKG = (EditText) view.findViewById(R.id.ex_activity_recycler_kg);
                Double kg = null;
                if (!etKG.getText().toString().equals("")) {
                    kg = Double.parseDouble(etKG.getText().toString());
                }

                if (kg != null && repit != null) {
                    exersixeOneRawHistories.add(new ExersixeOneRawHistory((i + 1), repit, kg));
                    if (i == itemCount - 1){
                        dialogstatus = false;
                    }
                }

            }

            if (dialogstatus){
                showDialog();
                return;
            }


            System.out.println(exersixeOneRawHistories);

            String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            System.out.println();

            ExerciseHistory exerciseHistory = new ExerciseHistory(currentDate, exersixeOneRawHistories);
            List<ExerciseHistory> exerciseHistoryList = new ArrayList<>();
            exerciseHistoryList.add(exerciseHistory);

            ExerciseHistoryToFIreBase exerciseHistoryToFIreBase = new ExerciseHistoryToFIreBase(exerciseHistoryList);

            System.out.println(exerciseHistory);
            Task<Void> saveOnDB = fStore.collection(KeysFirebaseStore.EXERCISE_HISTORY_DATA).document(fAuth.getUid())
                    .collection(tvExName.getText().toString()).document(currentDate).set(exerciseHistoryToFIreBase);


            editSharedPreferance(99);

            finish();

        });

        //timer
        btnTimerStart.setOnClickListener(btn->{
            if (mTimerRunning){
                stopTimer();
            }else {
                startTimer();
            }
        });
        btnTimerStop.setOnClickListener(btn->{
            stopTimer();
        });
        btnTimerClear.setOnClickListener(btn->{
            clearTimer();
        });
        btnTimerResume.setOnClickListener(btn->{
            startTimer();
            btnTimerClear.setVisibility(View.INVISIBLE);
            btnTimerResume.setVisibility(View.INVISIBLE);
        });
        updateTimerText();


        //history Layout

        btnHistory.setOnClickListener(v->{
            if (!historyClicked) {
                historyLayout.setVisibility(View.VISIBLE);
                historyLayout.setAnimation(AnimationUtils.loadAnimation(this, R.anim.faidin));

                layoutOfRecyclerFieldDetails.setVisibility(View.INVISIBLE);
                layoutOfRecyclerFieldDetails.setAnimation(AnimationUtils.loadAnimation(this, R.anim.faidout));

                recyclerTitle.setText("היסטוריה");
                btnHistory.setText("מלא נתונים");
                historyClicked = true;

            } else {
                layoutOfRecyclerFieldDetails.setVisibility(View.VISIBLE);
                layoutOfRecyclerFieldDetails.setAnimation(AnimationUtils.loadAnimation(this, R.anim.faidin));


                historyLayout.setVisibility(View.INVISIBLE);
                historyLayout.setAnimation(AnimationUtils.loadAnimation(this,R.anim.faidout));

                recyclerTitle.setText("מלא נתונים");
                btnHistory.setText("הצג היסטוריה");

                historyClicked = false;

            }

        });

        btnHistoryBack.setOnClickListener(v->{


              if (getCorrectHistory != 0) {
                    historyRecyclerView(exerciseHistoryRoot, --getCorrectHistory);
                    if (getCorrectHistory == 0){
                        btnHistoryBack.setVisibility(View.INVISIBLE);
                    }
              }

            btnHistoryNext.setVisibility(View.VISIBLE);

        });

        btnHistoryNext.setOnClickListener(v->{

            if (getCorrectHistory != exerciseHistoryRoot.size() - 1) {
                historyRecyclerView(exerciseHistoryRoot, ++getCorrectHistory);


                if (getCorrectHistory == exerciseHistoryRoot.size() - 1){
                    btnHistoryNext.setVisibility(View.INVISIBLE);
                }

            }

            btnHistoryBack.setVisibility(View.VISIBLE);

        });


        //youtube
        youtubeBTN.setOnClickListener(v->{
            youtubeLayout.setVisibility(View.VISIBLE);
            youtubeLayout.setAnimation(AnimationUtils.loadAnimation(this,R.anim.faidin));


        });

        youtubeLayout.setOnClickListener(v->{
            youtubeLayout.setVisibility(View.INVISIBLE);
            youtubeLayout.setAnimation(AnimationUtils.loadAnimation(this,R.anim.faidout));
        });


        //stop workout
        stopWorkoutBTN.setOnClickListener(v->{
            showQuitDialog(exercises);
        });


    }



    //mthods
    private void startTimer(){
        timerCount = new CountDownTimer(mTimerLeft,1_000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimerLeft = millisUntilFinished;
                updateTimerText();
                btnTimerStart.setVisibility(View.INVISIBLE);
                btnTimerStop.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                btnTimerStart.setVisibility(View.VISIBLE);
                btnTimerStop.setVisibility(View.INVISIBLE);
                clearTimer();
                sendTimerNotification();

            }
        }.start();

        mTimerRunning = true;
        btnTimerStart.setVisibility(View.INVISIBLE);
        btnTimerStop.setVisibility(View.VISIBLE);
    }
    private void stopTimer(){
        timerCount.cancel();
        mTimerRunning = false;
        btnTimerStop.setVisibility(View.INVISIBLE);
        btnTimerResume.setVisibility(View.VISIBLE);
        btnTimerClear.setVisibility(View.VISIBLE);



    }
    private void clearTimer(){
        mTimerLeft = timerTime;
        updateTimerText();
        btnTimerResume.setVisibility(View.INVISIBLE);
        btnTimerClear.setVisibility(View.INVISIBLE);
        btnTimerStart.setVisibility(View.VISIBLE);
    }
    private void updateTimerText(){
        int minute = (int) (mTimerLeft / 1000) / 60;
        int seconds = (int) (mTimerLeft / 1000) % 60;

        String timerLeftFprmat = String.format(Locale.getDefault(),"%02d:%02d", minute,seconds);
        tvTimer.setText(timerLeftFprmat);
    }
    public void  sendTimerNotification(){


        Drawable drawable = ivMainImage.getDrawable();
        Bitmap picNotificatio = ((BitmapDrawable)drawable).getBitmap();

//        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.setPackage("com.example.fitnessapp");
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent,0);


        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_notification_ex_timer)
                .setContentTitle(tvExName.getText())
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(picNotificatio)
                        .bigLargeIcon(picNotificatio))
                .setContentText("המנוחה נגמרה, הגיע הזמן לחזור להתאמן!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(getResources().getColor(R.color.mainGreen))
                .setAutoCancel(true)
//                .setContentIntent(contentIntent)
                .build();

        notificationManager.notify(1,notification);

    }

    private void regularComponents(List<Exercise> exercises, String dayName){



        Picasso.get().load(exercises.get(counterEx).getImage()).into(ivMainImage);
        tvDay.setText(CustomMethods.convertDate(dayName));
        tvSets.setText(String.valueOf(exercises.get(counterEx).getSets()));
        tvRepit.setText(String.valueOf(exercises.get(counterEx).getRepitition()));
        tvExName.setText(String.valueOf(exercises.get(counterEx).getExName()));
        String tvExNumberString = "תרגיל " + (counterEx + 1) + "/" + exercises.size();
        tvExNumber.setText(tvExNumberString);
        tvNote.setText(String.valueOf(exercises.get(counterEx).getNotes()));
        noteColor(exercises,counterEx);

        //timer
        if (mTimerRunning) {
            timerCount.cancel();
        }
        timerTime = exercises.get(counterEx).getRest();
        mTimerLeft = timerTime;
        mTimerRunning = false;
        btnTimerStop.setVisibility(View.INVISIBLE);
        btnTimerStart.setVisibility(View.VISIBLE);
        updateTimerText();


    }
    private void noteColor(List<Exercise> exercises, int counterEx){
        if (!exercises.get(counterEx).getNotes().equals("אין")){
            tvNote.setTextColor(getResources().getColor(R.color.mainRed));
            tvNoteTitle.setTextColor(getResources().getColor(R.color.mainRed));
        } else {
            tvNote.setTextColor(getResources().getColor(R.color.waite));
            tvNoteTitle.setTextColor(getResources().getColor(R.color.waite));

        }

    }

    private void showDialog(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.finish_ex_alert_dialog, null);

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(v).create();

        alertDialog.show();
    }

    private void getHistoryExFromFirebase(){

            fStore.collection(KeysFirebaseStore.EXERCISE_HISTORY_DATA).document(fAuth.getUid())
                    .collection(tvExName.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                    exerciseHistoryRoot = listExerciseHistory;
                    counterHistoryBTN = exerciseHistoryRoot.size();
                    getCorrectHistory = exerciseHistoryRoot.size() - 1;
                    historyRecyclerView(exerciseHistoryRoot, getCorrectHistory);
                }

            });


    }

    private void historyRecyclerView(List<ExerciseHistory> exerciseHistories, int dateCounter) {


        if (exerciseHistories.size() != 0) {


            noHistory.setVisibility(View.INVISIBLE);
            btnHistoryNext.setVisibility(View.VISIBLE);
            btnHistoryBack.setVisibility(View.VISIBLE);
            tvDateHistory.setVisibility(View.VISIBLE);
            tvDayHistory.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);

            if (exerciseHistories.size() == 1){

                btnHistoryNext.setVisibility(View.INVISIBLE);
                btnHistoryBack.setVisibility(View.INVISIBLE);

            } else {

                btnHistoryNext.setVisibility(View.VISIBLE);
                btnHistoryBack.setVisibility(View.VISIBLE);

            }

            btnHistoryNext.setVisibility(View.INVISIBLE);

//            RecyclerView recyclerView = findViewById(R.id.history_recyclerview);
            ExsercieHistoryRecyclerAdapter adapter = new ExsercieHistoryRecyclerAdapter(exerciseHistories, dateCounter, getLayoutInflater());
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
            System.out.println("exerciseHistories.size - " + exerciseHistories.size());
            tvDateHistory.setText(exerciseHistories.get(dateCounter).getDate());

            SimpleDateFormat dayNameFormat = new SimpleDateFormat("dd-MM-yyyy");
            try {
                Date dayName = dayNameFormat.parse(tvDateHistory.getText().toString());
                SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
                String dayNameString = outFormat.format(dayName);

                if (!dayNameString.contains("י")) {
                    tvDayHistory.setText(CustomMethods.convertDate(dayNameString));
                    return;
                }

                tvDayHistory.setText(dayNameString);
            } catch (ParseException e) {
                e.printStackTrace();
            }


        } else {

            noHistory.setVisibility(View.VISIBLE);
            btnHistoryNext.setVisibility(View.INVISIBLE);
            btnHistoryBack.setVisibility(View.INVISIBLE);
            tvDateHistory.setVisibility(View.INVISIBLE);
            tvDayHistory.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);


        }
    }



    private String youtubeID(List<Exercise> exercises){
        String videoLink = exercises.get(counterEx).getVideoLink();
        String[] split = videoLink.split("=");
        String youtubeID = split[1];

        System.out.println("YOUTUBE ID = " + youtubeID);


        return youtubeID;

    }

    private void showQuitDialog(List<Exercise> exercises){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.stop_workcout_dialog, null);
        Button btnYes = mView.findViewById(R.id.btn_stopWorkout_dialog);

        btnYes.setOnClickListener(v->{
            System.out.println("Click Yess!!!@#%&$!");

            editSharedPreferance(exercises);

            finish();
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }

    private void editSharedPreferance(List<Exercise> exercises){
        SharedPreferences sharedPreferences = getSharedPreferences(KeysSharedPrefercence.USER_SHAREDPREFERCENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KeysSharedPrefercence.NUMBER_OF_EXERCISES, exercises.size());
        editor.putInt(KeysSharedPrefercence.CORRECT_EXERCISE, counterEx);

        editor.apply();

    }

    private void editSharedPreferance(int finishWorkout){
        SharedPreferences sharedPreferences = getSharedPreferences(KeysSharedPrefercence.USER_SHAREDPREFERCENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KeysSharedPrefercence.CORRECT_EXERCISE, finishWorkout);

        editor.apply();

    }

    private void fireBaseHistoryExNames(String exName, String exImage){

        fStore.collection(KeysFirebaseStore.EXERCISE_HISTORY_DATA).document(fAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {


                List<HashMap<String, String>> allExName = (List<HashMap<String, String>>) task.getResult().getData().get(KeysFirebaseStore.NAMES_OF_ALL_EXNAMES_THE_USER_DONE);
                System.out.println("sfsgsg " + allExName);



                if(task.getResult().get(KeysFirebaseStore.NAMES_OF_ALL_EXNAMES_THE_USER_DONE) != null){

                    List<HistoryExName> historyExNameList = new ArrayList<>();

                    System.out.println("rrrrrrr" + allExName.get(0));

                    for (HashMap<String, String> stringStringHashMap : allExName) {
                        String exNameHash = stringStringHashMap.get("exName");
                        String exImageHash = stringStringHashMap.get("exImage");

                        if (exNameHash.equals(exName)){
                            return;
                        }

                        historyExNameList.add(new HistoryExName(exNameHash, exImageHash));

                    }

                    historyExNameList.add(new HistoryExName(exName, exImage));
                    AllHistoryExName allHistoryExName = new AllHistoryExName(historyExNameList);
                    fStore.collection(KeysFirebaseStore.EXERCISE_HISTORY_DATA).document(fAuth.getUid()).set(allHistoryExName);


                }else {

                    List<HistoryExName> historyExNames = new ArrayList<>();
                    historyExNames.add(new HistoryExName(exName, exImage));
                    AllHistoryExName allHistoryExName = new AllHistoryExName(historyExNames);
                    fStore.collection(KeysFirebaseStore.EXERCISE_HISTORY_DATA).document(fAuth.getUid()).set(allHistoryExName);


                }
            }
        });


    }


}
