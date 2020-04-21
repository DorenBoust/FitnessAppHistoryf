package com.example.fitnessapp.user;

import java.io.Serializable;
import java.util.List;

public class ExerciseFullHistory implements Serializable {

    private String exName;
    private String lastDateUpdate;
    private List<ExerciseHistory> listExerciseHistory;

    public ExerciseFullHistory(String exName, String lastDateUpdate, List<ExerciseHistory> listExerciseHistory) {
        this.exName = exName;
        this.lastDateUpdate = lastDateUpdate;
        this.listExerciseHistory = listExerciseHistory;
    }

    public String getExName() {
        return exName;
    }

    public void setExName(String exName) {
        this.exName = exName;
    }


    public String getLastDateUpdate() {
        return lastDateUpdate;
    }

    public void setLastDateUpdate(String lastDateUpdate) {
        this.lastDateUpdate = lastDateUpdate;
    }

    public List<ExerciseHistory> getListExerciseHistory() {
        return listExerciseHistory;
    }

    public void setListExerciseHistory(List<ExerciseHistory> listExerciseHistory) {
        this.listExerciseHistory = listExerciseHistory;
    }

    @Override
    public String toString() {
        return "ExerciseFullHistory{" +
                "exName='" + exName + '\'' +
                ", lastDateUpdate='" + lastDateUpdate + '\'' +
                ", listExerciseHistory=" + listExerciseHistory +
                '}';
    }

}
