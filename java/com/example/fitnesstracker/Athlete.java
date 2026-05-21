package com.example.fitnesstracker;

public class Athlete {
    private String name;
    private int age;
    private String gender;
    private double weight;
    private double height;
    private String primarySport;

    public Athlete(String name, int age, String gender, double weight, double height, String primarySport) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.weight = weight;
        this.height = height;
        this.primarySport = primarySport;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public double getWeight() {
        return weight;
    }

    public double getHeight() {
        return height;
    }

    public String getPrimarySport() {
        return primarySport;
    }
}