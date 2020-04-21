package com.example.fitnessapp.user;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class HistoryExName implements Serializable {
    private String exName;
    private String exImage;

    public HistoryExName(String exName, String exImage) {
        this.exName = exName;
        this.exImage = exImage;
    }

    public String getExName() {
        return exName;
    }

    public void setExName(String exName) {
        this.exName = exName;
    }

    public String getExImage() {
        return exImage;
    }

    public void setExImage(String exImage) {
        this.exImage = exImage;
    }

    @Override
    public String toString() {
        return "NameOfAllExercieOnHistory{" +
                "exName='" + exName + '\'' +
                ", exImage='" + exImage + '\'' +
                '}';
    }

}
