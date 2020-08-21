package com.example.cookdi.detail.StepRecyclerView;

public class StepModel {
    private String context;
    private int number;

    public StepModel(int number, String context) {
        this.context = context;
        this.number = number;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
