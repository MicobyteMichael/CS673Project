package com.example.healthapp.backend.exercise;

public enum ExerciseType {

    Jogging("Running at a fast pace", "distance", "miles", false);

    private final String description;
    private final String measuredValueName, unitName;
    private final boolean isIntegerQuantity;

    private ExerciseType(String description, String measuredValueName, String unitName, boolean isIntegerQuantity) {
        this.description = description;
        this.measuredValueName = measuredValueName;
        this.unitName = unitName;
        this.isIntegerQuantity = isIntegerQuantity;
    }

    public String getActivityName() { return name(); }
    public String getDescription() { return description; }
    public String getMeasuredValueName() { return measuredValueName; }
    public String getUnitName() { return unitName; }
    public boolean isIntegerQuantity() { return isIntegerQuantity; }
}
