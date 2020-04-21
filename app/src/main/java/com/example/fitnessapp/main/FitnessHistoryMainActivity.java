package com.example.fitnessapp.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.fitnessapp.R;
import com.example.fitnessapp.keys.KeysIntents;
import com.example.fitnessapp.user.ExerciseFullHistory;
import com.example.fitnessapp.user.ListExHistory;
import com.google.gson.Gson;
import com.ramotion.foldingcell.FoldingCell;

public class FitnessHistoryMainActivity extends AppCompatActivity {

    private AllHistoryExName allHistoryExName;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness_history_main);

        Intent intent = getIntent();
        Gson gson = new Gson();
        String stringExtra = intent.getStringExtra(KeysIntents.SEND_HISTORY_EXERCISE);
        allHistoryExName = gson.fromJson(stringExtra, AllHistoryExName.class);

        System.out.println("listOFExerciseFullHistory " + allHistoryExName);

        recyclerView = findViewById(R.id.recyclerview_fitness_history);
        FitnessHistoryReciclerAdapter adapter = new FitnessHistoryReciclerAdapter(getLayoutInflater(),allHistoryExName);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


    }


}
