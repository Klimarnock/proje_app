package com.example.fitnesstracker;

public class Running extends Activity {

    private double distance;

    public Running(String date, int duration, int calories, double distance) {
        super("Running", date, duration, calories);
        this.distance = distance;
    }

    @Override
    public String getSummary() {
        return "🏃 Running | " + date +
                " | " + duration + " min | " +
                distance + " km | " +
                calories + " kcal";
    }
}