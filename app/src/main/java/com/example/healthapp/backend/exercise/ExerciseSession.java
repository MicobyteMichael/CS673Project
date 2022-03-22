package com.example.healthapp.backend.exercise;

import java.time.Duration;
import java.time.Instant;

public class ExerciseSession {
    private final String name;
    private final ExerciseType exerciseType;
    private final Instant start, end;
    private final int averageHeartRate, caloriesBurned;
    private final float measuredValue;

    public ExerciseSession(String name, ExerciseType exerciseType, long start, long end, int averageHeartRate, int caloriesBurned, float measuredValue) {
        this(name, exerciseType, start == -1 ? null : Instant.ofEpochSecond(start), end == -1 ? null : Instant.ofEpochSecond(end), averageHeartRate, caloriesBurned, measuredValue);
    }

    public ExerciseSession(String name, ExerciseType exerciseType, Instant start, Instant end, int averageHeartRate, int caloriesBurned, float measuredValue) {
        this.name = name;
        this.exerciseType = exerciseType;
        this.start = start;
        this.end = end;
        this.averageHeartRate = averageHeartRate;
        this.caloriesBurned = caloriesBurned;
        this.measuredValue = measuredValue;
    }

    public String getName() { return name; }
    public ExerciseType getExerciseType() { return exerciseType; }
    public int getAverageHeartRate() { return averageHeartRate; }
    public int getCaloriesBurned() { return caloriesBurned; }
    public float getMeasuredValue() { return measuredValue; }

    public Instant getStart() { return start; }
    public Instant getEnd() { return end; }

    public Duration getDuration() {
        Instant start = getStart(), end = getEnd();
        if(start != null && end != null) return Duration.between(start, end);
        else return null;
    }

    public long getStartSecond() {
        if(start != null) return start.getEpochSecond();
        else return -1;
    }

    public long getEndSecond() {
        if(end != null) return end.getEpochSecond();
        else return -1;
    }
}
