package com.example.healthapp.backend.exercise;

import java.time.Instant;

public class ExerciseSession {

    private final String name, exerciseType;
    private final Instant start, end;
    private final int averageHeartRate, caloriesBurned;

    public ExerciseSession(String name, String exerciseType, long start, long end, int averageHeartRate, int caloriesBurned) {
        this(name, exerciseType, start == -1 ? null : Instant.ofEpochSecond(start), end == -1 ? null : Instant.ofEpochSecond(end), averageHeartRate, caloriesBurned);
    }

    public ExerciseSession(String name, String exerciseType, Instant start, Instant end, int averageHeartRate, int caloriesBurned) {
        this.name = name;
        this.exerciseType = exerciseType;
        this.start = start;
        this.end = end;
        this.averageHeartRate = averageHeartRate;
        this.caloriesBurned = caloriesBurned;
    }

    public String getName() { return name; }
    public String getExerciseType() { return exerciseType; }
    public int getAverageHeartRate() { return averageHeartRate; }
    public int getCaloriesBurned() { return caloriesBurned; }

    public Instant getStart() { return start; }
    public Instant getEnd() { return end; }

    public long getStartSecond() {
        if(start != null) return start.getEpochSecond();
        else return -1;
    }

    public long getEndSecond() {
        if(end != null) return end.getEpochSecond();
        else return -1;
    }
}
