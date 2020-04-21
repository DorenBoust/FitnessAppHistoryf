package com.example.fitnessapp.main;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.fitnessapp.R;
import com.example.fitnessapp.keys.KeysBundle;
import com.example.fitnessapp.user.DietProcessRaw;
import com.example.fitnessapp.user.DietProcessTab;
import com.example.fitnessapp.user.User;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.MarkerImage;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StatusFragment extends Fragment implements Serializable {

    private StatusViewModel mViewModel;
    private User user;
    private TextView nextMeet;
    private Button btnCalendar;
    private List<Date> chartDate = new ArrayList<>();
    private List<String> chartDateString = new ArrayList<>();
    private List<Float> chartWeight = new ArrayList<>();
    private List<Float> chartAbdominal = new ArrayList<>();
    private List<Float> chartArm = new ArrayList<>();
    private List<Float> chartBodyFat = new ArrayList<>();

    private TextView tvStartWeight;
    private TextView tvStartArm;
    private TextView tvStartBodyFat;
    private TextView tvStartAbdominal;
    private TextView tvStartDate;
    private TextView tvCurrectGoal;

    private LineChart graph;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.status_fragment, container, false);


        user = (User) getArguments().getSerializable(KeysBundle.USER_DATA_TO_FRAGMENT);
        System.out.println("USER ON STATUS - " + user);
        extractChart(user);

        nextMeet = v.findViewById(R.id.next_metting_tv);
        btnCalendar = v.findViewById(R.id.btn_calendar);
        tvStartWeight = v.findViewById(R.id.statusFragment_tv__startData_weight);
        tvStartArm = v.findViewById(R.id.statusFragment_tv__startData_arm);
        tvStartBodyFat = v.findViewById(R.id.statusFragment_tv_Abdominal);
        tvStartAbdominal = v.findViewById(R.id.statusFragment_tv_bodyFat);
        tvStartDate = v.findViewById(R.id.statusFragment_tv_startData2);
        tvCurrectGoal = v.findViewById(R.id.statusFragment_tv_goal);

        recentData();


        btnCalendar.setOnClickListener(btn->{



            String[] s = user.getNextMeet().split(" ");
            String date = s[0];
            String time = s[1];

            String[] dateSplit = date.split("/");
            int day = Integer.parseInt(dateSplit[0]);
            int month = Integer.parseInt(dateSplit[1]);
            int year = Integer.parseInt(dateSplit[2]);

            String[] timeSplit = time.split(":");
            int hour = Integer.parseInt(timeSplit[0]);
            int min = Integer.parseInt(timeSplit[1]);

            int meetNumber = user.getDietTable().getMeetNumber();
            String title = "פגישת סטטוס תזונאי ומאמן כושר - " + meetNumber;

            Calendar beginTime = Calendar.getInstance();
            beginTime.set(year, month, day, hour, min);
            Calendar endTime = Calendar.getInstance();
            endTime.set(year, month, day, hour, min + 30);
            Intent intent = new Intent(Intent.ACTION_INSERT)
                    .setData(CalendarContract.Events.CONTENT_URI)
                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                    .putExtra(CalendarContract.Events.TITLE, title);
//                    .putExtra(CalendarContract.Events.EVENT_LOCATION, "The gym")
//                    .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
//                    .putExtra(Intent.EXTRA_EMAIL, user.getEmail());
            startActivity(intent);



        });

        graph = v.findViewById(R.id.statusFragment_graph);

        graph.setDragEnabled(true);
        graph.setScaleEnabled(false);
        dataGraph(chartWeight, tvStartWeight);


        tvStartWeight.setOnClickListener(textview->{
            dataGraph(chartWeight, tvStartWeight);
        });

        tvStartArm.setOnClickListener(textview->{
            dataGraph(chartArm, tvStartArm);
        });

        tvStartBodyFat.setOnClickListener(textview->{
            dataGraph(chartBodyFat, tvStartBodyFat);
        });

        tvStartAbdominal.setOnClickListener(textview->{
            dataGraph(chartAbdominal, tvStartAbdominal);
        });




        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(StatusViewModel.class);
    }

    private void extractChart(User user){
        DietProcessTab dietTable = user.getDietTable();
        List<DietProcessRaw> processTable = dietTable.getProcessTable();
        for (DietProcessRaw dietProcessRaw : processTable) {
            chartWeight.add(dietProcessRaw.getWeight());
            chartAbdominal.add(dietProcessRaw.getAbdominal());
            chartArm.add(dietProcessRaw.getArm());
            chartBodyFat.add(dietProcessRaw.getBodyFat());
            chartDate.add(dietProcessRaw.getDate());
            chartDateString.add(dietProcessRaw.getDateString());
        }

    }

    private void dataGraph(List<Float> mainDataList, TextView textView){

        List<TextView> textViewsList = new ArrayList<>();
        textViewsList.add(tvStartWeight);
        textViewsList.add(tvStartArm);
        textViewsList.add(tvStartBodyFat);
        textViewsList.add(tvStartAbdominal);

        for (int i = 0; i < textViewsList.size() ; i++) {
            if (textView.equals(textViewsList.get(i))){
                textViewsList.get(i).setTextColor(getResources().getColor(R.color.mainRed));
            } else {
                textViewsList.get(i).setTextColor(getResources().getColor(R.color.waite));
            }
        }
        ArrayList<Entry> dataGraph = new ArrayList<>();

        if (mainDataList.size() > 5) {
            for (int i = 0; i < 5; i++) {
                dataGraph.add(new Entry(i, mainDataList.get((mainDataList.size() - 5) + i)));
            }

        } else {
            for (int i = 0; i < mainDataList.size(); i++) {
                dataGraph.add(new Entry(i, mainDataList.get(i)));
            }
        }



        LineDataSet set1 = new LineDataSet(dataGraph, "");
        set1.setFillAlpha(110);
        set1.setLineWidth(3f);
        set1.setColor(getResources().getColor(R.color.waite));
        set1.setDrawFilled(true);
        set1.setFillColor(getResources().getColor(R.color.lightGreen));
        set1.setDrawValues(false);



        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        LineData data = new LineData(dataSets);

        graph.setData(data);


        //Background
        graph.setBackgroundColor(getResources().getColor(R.color.mainGreen));
        //marker icon
        MarkerImage markerImage = new MarkerImage(getContext(),R.drawable.graph_mark_image);
        markerImage.setOffset(-18f,-18f);
        graph.setMarker(markerImage);




        //hide X and right Y
        graph.getXAxis().setEnabled(false);
        graph.getAxisRight().setEnabled(false);
        graph.getAxisRight().setDrawGridLines(false);
        graph.getXAxis().setDrawGridLines(false);


        //hide description and map
        graph.getLegend().setEnabled(false);
        graph.getDescription().setEnabled(false);

        //grid color
        graph.getAxisLeft().setAxisLineColor(getResources().getColor(R.color.lightGreen));
        graph.getAxisLeft().setTextColor(getResources().getColor(R.color.lightGreen));
        graph.setGridBackgroundColor(getResources().getColor(R.color.lightGreen));



        graph.invalidate();
    }

    private void recentData(){

        nextMeet.setText(user.getNextMeet());
        tvStartWeight.setText(chartWeight.get(chartWeight.size()-1).toString());
        tvStartArm.setText(chartArm.get(chartArm.size()-1).toString());
        tvStartBodyFat.setText(chartBodyFat.get(chartBodyFat.size()-1).toString());
        tvStartAbdominal.setText(chartAbdominal.get(chartAbdominal.size()-1).toString());
        tvStartDate.setText(chartDateString.get(chartDate.size()-1));
        tvCurrectGoal.setText(user.getGoal());
    }
}
