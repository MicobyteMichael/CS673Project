package com.example.healthapp.backend.foodanddrink;

public class Meal {

    private final String name;
    private final int calories;

    public Meal(String name, int calories) {
        this.name = name;
        this.calories = calories;
    }

    public String getName() { return name; }
    public int getCalories() { return calories; }
}
