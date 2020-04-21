package com.example.fitnessapp.articals;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class RecpieSteps implements Serializable {

    private int stepNumber;
    private List<String> steps;

    public RecpieSteps(int stepNumber, List<String> steps) {
        this.stepNumber = stepNumber;
        this.steps = steps;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    @Override
    public String toString() {
        return "RecpieSteps{" +
                "stepNumber=" + stepNumber +
                ", steps=" + steps +
                '}';
    }
}
