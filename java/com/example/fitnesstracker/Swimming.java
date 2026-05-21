package com.example.fitnesstracker;

public class Swimming extends Activity {

    private int laps;

    public Swimming(String date, int duration, int calories, int laps) {
        super("Swimming", date, duration, calories);
        this.laps = laps;
    }

    @Override
    public String getSummary() {
        return "🏊 Swimming | " + date +
                " | " + duration + " min | " +
                laps + " laps | " +
                calories + " kcal";
    }
}