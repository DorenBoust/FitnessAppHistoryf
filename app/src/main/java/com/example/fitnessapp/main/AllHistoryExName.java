package com.example.fitnessapp.main;

import com.example.fitnessapp.user.HistoryExName;

import java.util.List;

public class AllHistoryExName {
    private List<HistoryExName> historyExNames;

    public AllHistoryExName(List<HistoryExName> historyExNames) {
        this.historyExNames = historyExNames;
    }

    public List<HistoryExName> getHistoryExNames() {
        return historyExNames;
    }

    public void setHistoryExNames(List<HistoryExName> historyExNames) {
        this.historyExNames = historyExNames;
    }

    @Override
    public String toString() {
        return "AllHistoryExName{" +
                "historyExNames=" + historyExNames +
                '}';
    }
}
