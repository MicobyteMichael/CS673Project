package com.example.healthapp.backend.exercise;

public enum ExerciseType {

    Jogging("Jogging at a fast pace", "distance", "miles");

    private final String description;
    private final String measuredValueName, unitName;

    private ExerciseType(String description, String measuredValueName, String unitName) {
        this.description = description;
        this.measuredValueName = measuredValueName;
        this.unitName = unitName;
    }

    public String getActivityName() { return name(); }
    public String getDescription() { return description; }
    public String getMeasuredValueName() { return measuredValueName; }
    public String getUnitName() { return unitName; }
}
