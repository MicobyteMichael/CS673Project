package com.example.healthapp.backend.bodycomp;

public class BodyComposition {

    private final int weight, bodyFatPercentage, muscleWeight;

    public BodyComposition(int weight, int bodyFatPercentage, int muscleWeight) {
        this.weight = weight;
        this.bodyFatPercentage = bodyFatPercentage;
        this.muscleWeight = muscleWeight;
    }

    public int getWeight() { return weight; }
    public int getBodyFatPercentage() { return bodyFatPercentage; }
    public int getMuscleWeight() { return muscleWeight; }
}
