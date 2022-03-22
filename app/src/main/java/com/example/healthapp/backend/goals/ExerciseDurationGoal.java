package com.example.healthapp.backend.goals;

import com.example.healthapp.backend.exercise.ExerciseSession;
import com.example.healthapp.backend.exercise.ExerciseType;
import com.example.healthapp.backend.exercise.RESTTaskGetExerciseSessions;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.function.Consumer;

public class ExerciseDurationGoal extends Goal {
    public static final String TYPE = "Exercise Duration";

    private final ExerciseType exerciseType;

    public static void register() { /* Call the static { ... } block below */ }
    static { Goal.registerGoalType(TYPE, ExerciseDurationGoal.class); }

    public ExerciseDurationGoal(String name, int numCalories, boolean active, String comparison, String parameter) { super(name, numCalories, active); exerciseType = ExerciseType.valueOf(parameter); }
    @Override public String getGoalType() { return TYPE; }
    @Override public String getGoalComparison() { return Goal.COMP_MINIMUM; }
    @Override public String getGoalParameter() { return exerciseType.name(); }
    @Override public String getDescription() { return getGoalType() + ": " + getThreshold() + " hours (" + (exerciseType == null ? "Any Exercise" : exerciseType) + ")"; }

    @Override
    public void calculateIsMetToday(Consumer<Boolean> answerCallback, Consumer<String> failureMessageCallback) {
        Consumer<ExerciseSession[]> exersReceiver = exers -> {
            Duration d = Duration.ZERO;

            for(ExerciseSession es : exers) if(exerciseType == null || es.getExerciseType() == exerciseType) {
                Duration d2 = es.getDuration();
                if (d2 != null) d = d.plus(d2);
            }

            answerCallback.accept(d.getSeconds() / 60F / 60F >= getThreshold());
        };

        Consumer<String> failureReceiver = errMsg -> {
            failureMessageCallback.accept("Failed to calculate if exercise goal \"" + getName() + "\" was completed!! Error is: " + errMsg);
            answerCallback.accept(false);
        };

        RESTTaskGetExerciseSessions.enqueue(exersReceiver, failureReceiver);
    }
}