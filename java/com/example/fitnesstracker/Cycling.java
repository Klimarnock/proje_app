package com.example.fitnesstracker;

public class Cycling extends Activity {

    private double distance;
    private double averageSpeed;

    public Cycling(String date, int duration, int calories,
                   double distance, double averageSpeed) {

        super("Cycling", date, duration, calories);

        this.distance = distance;
        this.averageSpeed = averageSpeed;
    }

    @Override
    public String getSummary() {
        return "🚴 Cycling | " + date +
                " | " + duration + " min | " +
                distance + " km | " +
                averageSpeed + " km/h | " +
                calories + " kcal";
    }
}