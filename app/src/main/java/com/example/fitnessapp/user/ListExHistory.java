package com.example.fitnessapp.user;

import java.io.Serializable;
import java.util.List;

public class ListExHistory implements Serializable {

    private List<ExerciseFullHistory> nameOfAllExercieOnHistories;

    public ListExHistory(List<ExerciseFullHistory> nameOfAllExercieOnHistories) {
        this.nameOfAllExercieOnHistories = nameOfAllExercieOnHistories;
    }

    public List<ExerciseFullHistory> getNameOfAllExercieOnHistories() {
        return nameOfAllExercieOnHistories;
    }

    public void setNameOfAllExercieOnHistories(List<ExerciseFullHistory> nameOfAllExercieOnHistories) {
        this.nameOfAllExercieOnHistories = nameOfAllExercieOnHistories;
    }

    @Override
    public String toString() {
        return "NamesOfAllExercieOnHistoryToIntent{" +
                "nameOfAllExercieOnHistories=" + nameOfAllExercieOnHistories +
                '}';
    }
}
