package com.example.fitnessapp.user;

import java.io.Serializable;
import java.util.List;

public class DietProcessTab implements Serializable {
    private int meetNumber;
    private List<DietProcessRaw> ProcessTable;

    public DietProcessTab(List<DietProcessRaw> processTable, int meetNumber) {
        this.ProcessTable = processTable;
        this.meetNumber = meetNumber;

    }

    public int getMeetNumber() {
        return meetNumber;
    }

    public void setMeetNumber(int meetNumber) {
        this.meetNumber = meetNumber;
    }

    public List<DietProcessRaw> getProcessTable() {
        return ProcessTable;
    }

    public void setProcessTable(List<DietProcessRaw> processTable) {
        ProcessTable = processTable;
    }

    @Override
    public String toString() {
        return "DietProcessTab{" +
                "ProcessTable=" + ProcessTable +
                '}';
    }
}
