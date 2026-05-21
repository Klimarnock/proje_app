package com.example.fitnesstracker;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnLogin, btnRegister;
    Button btnGoAddActivity, btnGoHistory, btnGoProfile;
    Button btnSaveAthlete, btnCancelAthlete;
    Button btnSaveActivity;
    Button btnLogout;
    Button btnGlobalFeed;

    String selectedActivityType = "Running";
    String currentAthleteName = "";

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseHelper = new DatabaseHelper(this);

        showLoginScreen();
    }

    private void showLoginScreen() {
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(v -> {
            EditText etLoginName = findViewById(R.id.etLoginName);

            String loginName = etLoginName.getText().toString().trim();

            if (loginName.isEmpty()) {
                Toast.makeText(this, "Please enter athlete name", Toast.LENGTH_SHORT).show();
                return;
            }

            if (databaseHelper.athleteExists(loginName)) {
                currentAthleteName = loginName;
                Toast.makeText(this, "Welcome " + loginName, Toast.LENGTH_SHORT).show();
                showDashboardScreen();
            } else {
                Toast.makeText(this, "Athlete profile not found. Please register first.", Toast.LENGTH_LONG).show();
            }
        });

        btnRegister.setOnClickListener(v -> showAddAthleteScreen());
    }

    private void showAddAthleteScreen() {
        setContentView(R.layout.activity_add_athlete);

        btnSaveAthlete = findViewById(R.id.btnSaveAthlete);
        btnCancelAthlete = findViewById(R.id.btnCancelAthlete);

        Spinner spinnerSport = findViewById(R.id.spinnerPrimarySport);

        String[] sports = {"Running", "Swimming", "Cycling"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                sports
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSport.setAdapter(adapter);

        btnSaveAthlete.setOnClickListener(v -> {

            EditText etName = findViewById(R.id.etAthleteName);
            EditText etAge = findViewById(R.id.etAge);
            EditText etWeight = findViewById(R.id.etWeight);
            EditText etHeight = findViewById(R.id.etHeight);

            RadioButton rbMale = findViewById(R.id.rbMale);
            RadioButton rbFemale = findViewById(R.id.rbFemale);

            String name = etName.getText().toString().trim();
            String ageText = etAge.getText().toString().trim();
            String weightText = etWeight.getText().toString().trim();
            String heightText = etHeight.getText().toString().trim();

            if (name.isEmpty() || ageText.isEmpty() || weightText.isEmpty() || heightText.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int age;
            double weight;
            double height;

            try {
                age = Integer.parseInt(ageText);
                weight = Double.parseDouble(weightText);
                height = Double.parseDouble(heightText);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
                return;
            }

            String gender;

            if (rbMale.isChecked()) {
                gender = "Male";
            } else if (rbFemale.isChecked()) {
                gender = "Female";
            } else {
                gender = "Other";
            }

            String primarySport = spinnerSport.getSelectedItem().toString();

            Athlete athlete = new Athlete(
                    name,
                    age,
                    gender,
                    weight,
                    height,
                    primarySport
            );

            boolean saved = databaseHelper.insertAthlete(
                    athlete.getName(),
                    athlete.getAge(),
                    athlete.getGender(),
                    athlete.getWeight(),
                    athlete.getHeight(),
                    athlete.getPrimarySport()
            );

            if (saved) {
                currentAthleteName = athlete.getName();

                Toast.makeText(this, athlete.getName() + " profile saved", Toast.LENGTH_LONG).show();

                showDashboardScreen();
            } else {
                Toast.makeText(this, "Athlete could not be saved", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancelAthlete.setOnClickListener(v -> showLoginScreen());
    }

    private void showDashboardScreen() {
        setContentView(R.layout.activity_dashboard);

        TextView tvTotalWorkouts = findViewById(R.id.tvTotalWorkouts);
        TextView tvTotalCalories = findViewById(R.id.tvTotalCalories);
        TextView tvTotalDuration = findViewById(R.id.tvTotalDuration);
        ProgressBar progressCalories =
                findViewById(R.id.progressCalories);

        ProgressBar progressDuration =
                findViewById(R.id.progressDuration);

        int totalWorkouts = databaseHelper.getTotalWorkoutCount(currentAthleteName);
        int totalCalories = databaseHelper.getTotalCalories(currentAthleteName);
        int totalDuration = databaseHelper.getTotalDuration(currentAthleteName);
        progressCalories.setProgress(totalCalories);
        progressDuration.setProgress(totalDuration);

        tvTotalWorkouts.setText("Workouts: " + totalWorkouts);
        tvTotalCalories.setText("Calories: " + totalCalories + " kcal");
        tvTotalDuration.setText("Duration: " + totalDuration + " min");

        btnGoAddActivity = findViewById(R.id.btnGoAddActivity);
        btnGoHistory = findViewById(R.id.btnGoHistory);
        btnGoProfile = findViewById(R.id.btnGoProfile);
        btnGlobalFeed = findViewById(R.id.btnGlobalFeed);
        btnLogout = findViewById(R.id.btnLogout);

        btnGoAddActivity.setOnClickListener(v -> showAddActivityScreen());
        btnGoHistory.setOnClickListener(v -> showWorkoutHistoryScreen());
        btnGlobalFeed.setOnClickListener(v -> showGlobalFeedScreen());
        btnGoProfile.setOnClickListener(v -> showAddAthleteScreen());

        btnLogout.setOnClickListener(v -> {
            currentAthleteName = "";
            showLoginScreen();
        });
    }

    private void showAddActivityScreen() {
        setContentView(R.layout.activity_add_activity);

        Button btnBackFromAddActivity = findViewById(R.id.btnBackFromAddActivity);
        btnBackFromAddActivity.setOnClickListener(v -> showDashboardScreen());

        selectedActivityType = "Running";

        TextView tvRunning = findViewById(R.id.tvRunning);
        TextView tvSwimming = findViewById(R.id.tvSwimming);
        TextView tvCycling = findViewById(R.id.tvCycling);

        btnSaveActivity = findViewById(R.id.btnSaveActivity);

        tvRunning.setOnClickListener(v -> {
            selectedActivityType = "Running";
            tvRunning.setTextColor(0xFF39FF5A);
            tvSwimming.setTextColor(0xFF777777);
            tvCycling.setTextColor(0xFF777777);
        });

        tvSwimming.setOnClickListener(v -> {
            selectedActivityType = "Swimming";
            tvRunning.setTextColor(0xFF777777);
            tvSwimming.setTextColor(0xFF39FF5A);
            tvCycling.setTextColor(0xFF777777);
        });

        tvCycling.setOnClickListener(v -> {
            selectedActivityType = "Cycling";
            tvRunning.setTextColor(0xFF777777);
            tvSwimming.setTextColor(0xFF777777);
            tvCycling.setTextColor(0xFF39FF5A);
        });

        btnSaveActivity.setOnClickListener(v -> {

            EditText etDate = findViewById(R.id.etDate);
            EditText etDuration = findViewById(R.id.etDuration);
            EditText etDistance = findViewById(R.id.etDistance);
            EditText etCalories = findViewById(R.id.etCalories);
            EditText etAvgSpeed = findViewById(R.id.etAvgSpeed);

            String date = etDate.getText().toString().trim();
            String durationText = etDuration.getText().toString().trim();
            String distanceText = etDistance.getText().toString().trim();
            String caloriesText = etCalories.getText().toString().trim();
            String avgSpeedText = etAvgSpeed.getText().toString().trim();

            if (currentAthleteName.isEmpty()) {
                Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
                showLoginScreen();
                return;
            }

            if (date.isEmpty() || durationText.isEmpty() || caloriesText.isEmpty()) {
                Toast.makeText(this, "Please fill date, duration and calories", Toast.LENGTH_SHORT).show();
                return;
            }

            int duration;
            int calories;

            try {
                duration = Integer.parseInt(durationText);
                calories = Integer.parseInt(caloriesText);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Please enter valid duration and calories", Toast.LENGTH_SHORT).show();
                return;
            }

            Activity activity;
            boolean saved;

            if (selectedActivityType.equals("Running")) {

                if (distanceText.isEmpty()) {
                    Toast.makeText(this, "Please enter distance for running", Toast.LENGTH_SHORT).show();
                    return;
                }

                double distance;

                try {
                    distance = Double.parseDouble(distanceText);
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Please enter a valid distance", Toast.LENGTH_SHORT).show();
                    return;
                }

                activity = new Running(date, duration, calories, distance);

                saved = databaseHelper.insertActivity(
                        currentAthleteName,
                        "Running",
                        date,
                        duration,
                        calories,
                        distance,
                        0,
                        0
                );

            } else if (selectedActivityType.equals("Swimming")) {

                if (distanceText.isEmpty()) {
                    Toast.makeText(this, "Please enter laps in distance field", Toast.LENGTH_SHORT).show();
                    return;
                }

                int laps;

                try {
                    laps = Integer.parseInt(distanceText);
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Please enter valid laps", Toast.LENGTH_SHORT).show();
                    return;
                }

                activity = new Swimming(date, duration, calories, laps);

                saved = databaseHelper.insertActivity(
                        currentAthleteName,
                        "Swimming",
                        date,
                        duration,
                        calories,
                        0,
                        laps,
                        0
                );

            } else {

                if (distanceText.isEmpty() || avgSpeedText.isEmpty()) {
                    Toast.makeText(this, "Please enter distance and average speed", Toast.LENGTH_SHORT).show();
                    return;
                }

                double distance;
                double avgSpeed;

                try {
                    distance = Double.parseDouble(distanceText);
                    avgSpeed = Double.parseDouble(avgSpeedText);
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Please enter valid distance and speed", Toast.LENGTH_SHORT).show();
                    return;
                }

                activity = new Cycling(date, duration, calories, distance, avgSpeed);

                saved = databaseHelper.insertActivity(
                        currentAthleteName,
                        "Cycling",
                        date,
                        duration,
                        calories,
                        distance,
                        0,
                        avgSpeed
                );
            }

            if (saved) {
                Toast.makeText(this, activity.getSummary(), Toast.LENGTH_LONG).show();
                showDashboardScreen();
            } else {
                Toast.makeText(this, "Activity could not be saved", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showWorkoutHistoryScreen() {
        setContentView(R.layout.activity_workout_history);

        Button btnBackFromHistory = findViewById(R.id.btnBackFromHistory);
        btnBackFromHistory.setOnClickListener(v -> showDashboardScreen());

        Button btnClearHistory = findViewById(R.id.btnClearHistory);

        btnClearHistory.setOnClickListener(v -> {
            databaseHelper.deleteActivitiesByAthlete(currentAthleteName);
            Toast.makeText(this, "Workout history cleared", Toast.LENGTH_SHORT).show();
            showWorkoutHistoryScreen();
        });

        TextView tvWorkoutList = findViewById(R.id.tvWorkoutList);

        Cursor cursor = databaseHelper.getActivitiesByAthlete(currentAthleteName);

        if (cursor.getCount() == 0) {
            tvWorkoutList.setText("No activities saved yet.");
            cursor.close();
            return;
        }

        StringBuilder builder = new StringBuilder();

        while (cursor.moveToNext()) {
            String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
            String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
            int duration = cursor.getInt(cursor.getColumnIndexOrThrow("duration"));
            int calories = cursor.getInt(cursor.getColumnIndexOrThrow("calories"));
            double distance = cursor.getDouble(cursor.getColumnIndexOrThrow("distance"));
            int laps = cursor.getInt(cursor.getColumnIndexOrThrow("laps"));
            double avgSpeed = cursor.getDouble(cursor.getColumnIndexOrThrow("avg_speed"));

            builder.append("🏅 ").append(type).append("\n");
            builder.append("Athlete: ").append(currentAthleteName).append("\n");
            builder.append("Date: ").append(date).append("\n");
            builder.append("Duration: ").append(duration).append(" min\n");
            builder.append("Calories: ").append(calories).append(" kcal\n");

            if (type.equals("Running")) {
                builder.append("Distance: ").append(distance).append(" km\n");
            } else if (type.equals("Swimming")) {
                builder.append("Laps: ").append(laps).append("\n");
            } else if (type.equals("Cycling")) {
                builder.append("Distance: ").append(distance).append(" km\n");
                builder.append("Average Speed: ").append(avgSpeed).append(" km/h\n");
            }

            builder.append("\n-------------------------\n\n");
        }

        cursor.close();
        tvWorkoutList.setText(builder.toString());
    }
    private void showGlobalFeedScreen() {

        setContentView(R.layout.activity_global_feed);

        Button btnBackFromGlobalFeed =
                findViewById(R.id.btnBackFromGlobalFeed);

        btnBackFromGlobalFeed.setOnClickListener(
                v -> showDashboardScreen()
        );

        TextView tvGlobalFeed =
                findViewById(R.id.tvGlobalFeed);

        Cursor cursor =
                databaseHelper.getGlobalActivityFeed();

        if (cursor.getCount() == 0) {

            tvGlobalFeed.setText(
                    "No global activities found."
            );

            cursor.close();

            return;
        }

        StringBuilder builder = new StringBuilder();

        while (cursor.moveToNext()) {

            String athleteName =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "athlete_name"
                            )
                    );

            String type =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "type"
                            )
                    );

            String date =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "date"
                            )
                    );

            int duration =
                    cursor.getInt(
                            cursor.getColumnIndexOrThrow(
                                    "duration"
                            )
                    );

            int calories =
                    cursor.getInt(
                            cursor.getColumnIndexOrThrow(
                                    "calories"
                            )
                    );

            builder.append("👤 ")
                    .append(athleteName)
                    .append("\n");

            builder.append("🏅 ")
                    .append(type)
                    .append("\n");

            builder.append("📅 ")
                    .append(date)
                    .append("\n");

            builder.append("⏱ ")
                    .append(duration)
                    .append(" min\n");

            builder.append("🔥 ")
                    .append(calories)
                    .append(" kcal\n");

            builder.append("\n----------------------\n\n");
        }

        cursor.close();

        tvGlobalFeed.setText(
                builder.toString()
        );
    }
}