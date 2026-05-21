package com.example.fitnesstracker;

public class Activity {
    protected String type;
    protected String date;
    protected int duration;
    protected int calories;

    public Activity(String type, String date, int duration, int calories) {
        this.type = type;
        this.date = date;
        this.duration = duration;
        this.calories = calories;
    }

    public String getSummary() {
        return type + " - " + date + " - " + duration + " min - " + calories + " kcal";
    }
}