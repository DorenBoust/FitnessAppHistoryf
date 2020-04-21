package com.example.fitnessapp.main;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.airbnb.lottie.LottieAnimationView;
import com.example.fitnessapp.R;
import com.example.fitnessapp.keys.KeysFirebaseStore;
import com.example.fitnessapp.keys.KeysIntents;
import com.example.fitnessapp.keys.KeysSharedPrefercence;
import com.example.fitnessapp.keys.KeysBundle;
import com.example.fitnessapp.models.CustomMethods;
import com.example.fitnessapp.user.Day;
import com.example.fitnessapp.user.Exercise;
import com.example.fitnessapp.user.ExerciseFullHistory;
import com.example.fitnessapp.user.ExerciseHistory;
import com.example.fitnessapp.user.ExersixeOneRawHistory;
import com.example.fitnessapp.user.HistoryExName;
import com.example.fitnessapp.user.ListExHistory;
import com.example.fitnessapp.user.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FitnessFragment extends Fragment {

    private FitnessViewModel mViewModel;
    private User user;
    private List<Day> days = new ArrayList<>();

    private TextView tvMainDayName;
    private TextView tvMainNumberOfEx;
    private TextView tvMainEsTime;
    private TextSwitcher switcherInnerExName;
    private TextView tvInnerExNameTEXTVIEW;
    private TextSwitcher switcherInnerExNumber;
    private TextView tvInnerExNumberTEXTVIEW;
    private ImageView ivInnerImage;
    private Button btnMainStart;
    private LottieAnimationView lottieAnimationLoadWorkout;

    //correct day
    private SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
    private Date date = new Date();
    private String correctDay;

    //layouts
    private ConstraintLayout mainDayLayout;
    private ConstraintLayout mainDayLayoutDayOff;
    private TextView dayOffName;
    private LottieAnimationView lottieAnimationDayOff;

    //resume Exercise
    private SeekBar exersiceProgressBar;

    //history
    private ImageView fitnessHistory;
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private MutableLiveData<AllHistoryExName> mutableLiveDataExersiceHistory = new MutableLiveData<>();




    public static FitnessFragment newInstance() {
        return new FitnessFragment();
    }



    @Override
    public void onResume() {
        super.onResume();
        sharedPreference();
        btnMainStart.setVisibility(View.VISIBLE);
        lottieAnimationLoadWorkout.setVisibility(View.INVISIBLE);

        if (sharedPreferenceFinish() == 99){
            dayOffName.setText(CustomMethods.convertDate(correctDay));
            lottieAnimationDayOff.setAnimation(R.raw.finish_workout);
            lottieAnimationDayOff.playAnimation();
            mainDayLayout.setVisibility(View.INVISIBLE);
            mainDayLayoutDayOff.setVisibility(View.VISIBLE);
            mainDayLayoutDayOff.setAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.enter_bottom_to_top));
            return;
        }


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fitness_fragment, container, false);

        fitnessHistory = v.findViewById(R.id.fitness_history_imageview);

        List<Exercise> exercises = (List<Exercise>) getArguments().getSerializable(KeysIntents.EX_LIST);
        System.out.println("exercises on fitness fragment - " + exercises);

        tvMainDayName = v.findViewById(R.id.tv_fitness_main_ex_day_name);
        tvMainNumberOfEx = v.findViewById(R.id.tv_fitness_main_ex_day_inner_exNumber);
        tvMainEsTime = v.findViewById(R.id.tv_fitness_main_ex_day_esTime);
        btnMainStart = v.findViewById(R.id.fitness_btn_start);
        lottieAnimationDayOff = v.findViewById(R.id.lottie_dayOff);
        lottieAnimationLoadWorkout = v.findViewById(R.id.lottie_animation_load_workout);
        switcherInnerExName = v.findViewById(R.id.tv_fitness_main_ex_day_exName);
        switcherInnerExNumber = v.findViewById(R.id.tv_fitness_main_ex_day_exNumber);
        ivInnerImage = v.findViewById(R.id.iv_fitness_main_ex_day_image);

        correctDay = sdf.format(date).toLowerCase();

        if (correctDay.contains("י")){
            String s = CustomMethods.convertDateToEnglish(correctDay);
            correctDay = s;
        }

        mainDayLayout = v.findViewById(R.id.fitness_main_ex_layout);
        mainDayLayoutDayOff = v.findViewById(R.id.fitness_main_ex_layout_dayOff);
        dayOffName = v.findViewById(R.id.fitness_tv_dayoff_dayName);
        lottieAnimationDayOff = v.findViewById(R.id.lottie_dayOff);

        exersiceProgressBar = v.findViewById(R.id.training_progress);

        //disable change progressBar by user
        exersiceProgressBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        System.out.println("CorrectDay = " + correctDay);

        //get Data and start
        user = (User) getArguments().getSerializable(KeysBundle.USER_DATA_TO_FRAGMENT);
        days = user.getDays();

        mainDayEx(days);


        //SharedPreferences from exercise Activity
        sharedPreference();

        //recyclerView
        final RecyclerView recyclerView = v.findViewById(R.id.fitness_recyclerView);
        FitnessDaysRecyclerAdapter adapter = new FitnessDaysRecyclerAdapter(days,getLayoutInflater());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        //start Button on main ex
        btnMainStart.setOnClickListener(btn->{

            btnMainStart.setVisibility(View.INVISIBLE);
            lottieAnimationLoadWorkout.setVisibility(View.VISIBLE);
            lottieAnimationLoadWorkout.playAnimation();


            List<Exercise> exercisesData = new ArrayList<>();
            String sendDayName = "";
            for (int i = 0; i < days.size() ; i++) {
                Day day = days.get(i);
                String dayName = day.getDayName();
                if (dayName.equals(correctDay)){
                    exercisesData = day.getExercises();
                    sendDayName = dayName;
                }
            }


            Intent intent = new Intent(getContext(), ExersiceActivity.class);
            intent.putExtra(KeysIntents.EX_LIST, (Serializable) exercisesData);
            intent.putExtra(KeysIntents.DAY_NAME, sendDayName);
            startActivity(intent);
        });

        //fitness history
        fitnessHistory.setOnClickListener(btn->{

            getFitnessHistory();


        });

//        mutableLiveDataExersiceHistory.observe(getActivity(), new Observer<List<ExerciseFullHistory>>() {
//            @Override
//            public void onChanged(List<ExerciseFullHistory> exerciseFullHistories) {
//
//                if (exerciseFullHistories != null) {
//                    ListExHistory listExHistory = new ListExHistory(exerciseFullHistories);
//                    Intent fitnessHistoryIntent = new Intent(getContext(), FitnessHistoryMainActivity.class);
//                    Gson gson = new Gson();
//                    String listExHistoryJson = gson.toJson(listExHistory);
//                    fitnessHistoryIntent.putExtra(KeysIntents.SEND_HISTORY_EXERCISE, listExHistoryJson);
//                    startActivity(fitnessHistoryIntent);
//                } else {
//                    Toast.makeText(getContext(), "לא נמצאה היסטוריה לתרגילים", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });

        mutableLiveDataExersiceHistory.observe(getActivity(), new Observer<AllHistoryExName>() {
            @Override
            public void onChanged(AllHistoryExName allHistoryExName) {

                Intent fitnessHistoryIntent = new Intent(getContext(), FitnessHistoryMainActivity.class);
                Gson gson = new Gson();
                String listExHistoryJson = gson.toJson(allHistoryExName);
                fitnessHistoryIntent.putExtra(KeysIntents.SEND_HISTORY_EXERCISE, listExHistoryJson);
                startActivity(fitnessHistoryIntent);
            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(FitnessViewModel.class);
        // TODO: Use the ViewModel
    }



    private void mainDayEx(List<Day> daysList){

        //if dosent have practice today, change main layout
        List<String> findDayName = new ArrayList<>();
        for (Day day : daysList) {
            findDayName.add(day.getDayName());
            System.out.println(day);
        }

        if (!findDayName.contains(correctDay)){
            dayOffName.setText(CustomMethods.convertDate(correctDay));
            lottieAnimationDayOff.setAnimation(R.raw.day_off);
            lottieAnimationDayOff.playAnimation();
            mainDayLayout.setVisibility(View.INVISIBLE);
            mainDayLayoutDayOff.setAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.enter_bottom_to_top));
            return;
        }


        //reset sharedpreferance when day over
        if (!getSharedPreferenceDay().equals(correctDay)){
            resetSharedPreferance();
        }

        //if user allready the workout today
        if (sharedPreferenceFinish() == 99){
            dayOffName.setText(CustomMethods.convertDate(correctDay));
            lottieAnimationDayOff.playAnimation();
            mainDayLayout.setVisibility(View.INVISIBLE);
            mainDayLayoutDayOff.setAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.enter_bottom_to_top));
            return;
        }


        //have practice
        for (Day day : daysList) {
            if (day.getDayName().equals(correctDay)){
                mainDayLayoutDayOff.setVisibility(View.INVISIBLE);
                mainDayLayout.setAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.enter_bottom_to_top));
                tvMainDayName.setText(CustomMethods.convertDate(day.getDayName()));
                getInnerExParameters(day);

                setSharedPreferenceDay();
            }
        }


    }

    private void getInnerExParameters(Day day){
        List<Exercise> exercises = day.getExercises();
        List<String> exNameList = new ArrayList<>();
        List<String> exImages = new ArrayList<>();
        List<String> exNumber = new ArrayList<>();
        long restTime = 0;
        for (int i = 0; i <exercises.size() ; i++) {
            exNameList.add(exercises.get(i).getExName());
            exImages.add(exercises.get(i).getImage());
            String exNumberString = "תרגיל " + (i+1);
            exNumber.add(exNumberString);
            restTime += (exercises.get(i).getRest());
            //evrey set add extra 1 min to the esTime
            restTime += (exercises.get(i).getSets()) * 60_000;
        }


        tvMainNumberOfEx.setText(String.valueOf(exercises.size()));

        //es time
        String esTime = CustomMethods.getEsTime(restTime);
        tvMainEsTime.setText(String.valueOf(esTime));


        CountDownTimer countDownTimer = new CountDownTimer(exercises.size() * 3_000, 3000) {
            int counter = 0;
            @Override
            public void onTick(long millisUntilFinished) {

                switcherInnerExName.setText(exNameList.get(counter % exNameList.size()));
                switcherInnerExNumber.setText(exNumber.get(counter % exNumber.size()));

                Picasso.get().load(exImages.get(counter % exImages.size())).into(ivInnerImage);
                counter++;
            }

            @Override
            public void onFinish() {
                counter = 0;
                this.start();

            }
        };

        switcherInnerExNumber.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                tvInnerExNumberTEXTVIEW = new TextView(getContext());
                tvInnerExNumberTEXTVIEW.setTextColor(getResources().getColor(R.color.lightGreen));
                tvInnerExNumberTEXTVIEW.setTextSize(12);
                tvInnerExNumberTEXTVIEW.setGravity(Gravity.CENTER_HORIZONTAL);
                return tvInnerExNumberTEXTVIEW;
            }
        });
        switcherInnerExName.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                tvInnerExNameTEXTVIEW = new TextView(getContext());
                tvInnerExNameTEXTVIEW.setTextColor(getResources().getColor(R.color.mainGreen));
                tvInnerExNameTEXTVIEW.setTextSize(14);
                tvInnerExNameTEXTVIEW.setGravity(Gravity.CENTER_HORIZONTAL);
                return tvInnerExNameTEXTVIEW;
            }
        });


        countDownTimer.start();

    }

    private void sharedPreference(){
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(KeysSharedPrefercence.USER_SHAREDPREFERCENCE_NAME, Context.MODE_PRIVATE);

        int spNumberOfExercises = sharedPreferences.getInt(KeysSharedPrefercence.NUMBER_OF_EXERCISES, 6);
        exersiceProgressBar.setMax(spNumberOfExercises-1);

        int spCorrectExercise = sharedPreferences.getInt(KeysSharedPrefercence.CORRECT_EXERCISE, 0);
        exersiceProgressBar.setProgress(spCorrectExercise);


        System.out.println("SHAREDPREFERCENCE  = " + spCorrectExercise);
        if (spCorrectExercise != 0){

            exersiceProgressBar.setVisibility(View.VISIBLE);
            mainDayLayout.setBackground(getResources().getDrawable(R.drawable.background_main_fitness_day_pause_exersice));
            tvMainDayName.setTextColor(getResources().getColor(R.color.mainGreen));
            tvMainEsTime.setTextColor(getResources().getColor(R.color.mainGreen));
            tvMainNumberOfEx.setTextColor(getResources().getColor(R.color.mainGreen));
            btnMainStart.setText("המשך אימון");
            btnMainStart.setBackground(getResources().getDrawable(R.drawable.btn_resume_workout));
            btnMainStart.setTextColor(getResources().getColor(R.color.mainRed));


        } else {
            exersiceProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private int sharedPreferenceFinish(){
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(KeysSharedPrefercence.USER_SHAREDPREFERCENCE_NAME, Context.MODE_PRIVATE);
        int spCorrectExercise = sharedPreferences.getInt(KeysSharedPrefercence.CORRECT_EXERCISE, 0);
        return spCorrectExercise;

    }

    private void setSharedPreferenceDay(){

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(KeysSharedPrefercence.USER_SHAREDPREFERCENCE_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KeysSharedPrefercence.EXERCISE_CORRECT_DAY, correctDay);

        editor.apply();

    }

    private String getSharedPreferenceDay(){
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(KeysSharedPrefercence.USER_SHAREDPREFERCENCE_NAME, Context.MODE_PRIVATE);

        String correctDay = sharedPreferences.getString(KeysSharedPrefercence.EXERCISE_CORRECT_DAY, "null");

        return correctDay;
    }

    private void resetSharedPreferance(){
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(KeysSharedPrefercence.USER_SHAREDPREFERCENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KeysSharedPrefercence.NUMBER_OF_EXERCISES, 6);
        editor.putInt(KeysSharedPrefercence.CORRECT_EXERCISE, 0);

        editor.apply();

    }

    private void getFitnessHistory(){

        fStore.collection(KeysFirebaseStore.EXERCISE_HISTORY_DATA).document(fAuth.getUid()).get().addOnCompleteListener(getActivity(), new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                List<HashMap<String, String>> allExName = (List<HashMap<String, String>>) task.getResult().getData().get(KeysFirebaseStore.NAMES_OF_ALL_EXNAMES_THE_USER_DONE);

                if(task.getResult().get(KeysFirebaseStore.NAMES_OF_ALL_EXNAMES_THE_USER_DONE) != null){

                    List<HistoryExName> historyExNameList = new ArrayList<>();

                    for (HashMap<String, String> stringStringHashMap : allExName) {

                        String exNameHash = stringStringHashMap.get("exName");
                        String exImageHash = stringStringHashMap.get("exImage");

                        historyExNameList.add(new HistoryExName(exNameHash, exImageHash));

                    }

                    AllHistoryExName allHistoryExName = new AllHistoryExName(historyExNameList);
                    mutableLiveDataExersiceHistory.setValue(allHistoryExName);


                }else {
                    Toast.makeText(getContext(), "לא נמצאה היסטוריית תרגילים", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }


}
