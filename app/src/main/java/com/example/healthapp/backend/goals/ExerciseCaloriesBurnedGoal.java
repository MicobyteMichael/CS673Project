package com.example.healthapp.backend.goals;

import com.example.healthapp.backend.exercise.ExerciseSession;
import com.example.healthapp.backend.exercise.ExerciseType;
import com.example.healthapp.backend.exercise.RESTTaskGetExerciseSessions;

import java.util.function.Consumer;

public class ExerciseCaloriesBurnedGoal extends Goal {
    public static final String TYPE = "Calories Burned";

    public static Goal create(String name, int calories) {
        return new ExerciseCaloriesBurnedGoal(name, calories, true, null, null);
    }

    public static void register() { /* Call the static { ... } block below */ }
    static { Goal.registerGoalType(TYPE, ExerciseCaloriesBurnedGoal.class); }

    public ExerciseCaloriesBurnedGoal(String name, float numCalories, boolean active, String comparison, String parameter) { super(name, numCalories, active); }
    @Override public String getGoalType() { return TYPE; }
    @Override public String getGoalComparison() { return Goal.COMP_MINIMUM; }
    @Override public String getGoalParameter() { return null; }

    @Override
    public void calculateIsMetToday(Consumer<Boolean> answerCallback, Consumer<String> failureMessageCallback) {
        Consumer<ExerciseSession[]> exersReceiver = exers -> {
            int totalCalories = 0;
            for(ExerciseSession es : exers) totalCalories += es.getCaloriesBurned();
            answerCallback.accept(totalCalories >= getThreshold());
        };

        Consumer<String> failureReceiver = errMsg -> {
            failureMessageCallback.accept("Failed to calculate if exercise calories goal \"" + getName() + "\" was completed!! Error is: " + errMsg);
            answerCallback.accept(false);
        };

        RESTTaskGetExerciseSessions.enqueue(exersReceiver, failureReceiver);
    }
}