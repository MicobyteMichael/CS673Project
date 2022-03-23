package com.example.healthapp.backend.bodycomp;

import java.time.LocalDate;

public class BodyComposition {

    private final int weight, bodyFatPercentage, muscleWeight;
    private final LocalDate measuredOn;

    public BodyComposition(int weight, int bodyFatPercentage, int muscleWeight) { this(weight, bodyFatPercentage, muscleWeight, null); }

    public BodyComposition(int weight, int bodyFatPercentage, int muscleWeight, LocalDate measuredOn) {
        this.weight = weight;
        this.bodyFatPercentage = bodyFatPercentage;
        this.muscleWeight = muscleWeight;
        this.measuredOn = measuredOn;
    }

    public int getWeight() { return weight; }
    public int getBodyFatPercentage() { return bodyFatPercentage; }
    public int getMuscleWeight() { return muscleWeight; }
    public LocalDate getMeasuredOn() { return measuredOn; }

    public String getDateMeasuredOn() {
        if(measuredOn == null) return null;
        else return measuredOn.getMonth().getValue() + "/" + measuredOn.getDayOfMonth() + "/" + measuredOn.getYear();
    }

    public String getDescription() {
        return getWeight() + "lbs., " + getBodyFatPercentage() + "% body fat, " + getMuscleWeight() + "lbs. muscle";
    }

}
