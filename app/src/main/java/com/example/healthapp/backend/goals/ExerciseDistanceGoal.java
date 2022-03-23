package com.example.healthapp.backend.goals;

import com.example.healthapp.backend.exercise.ExerciseSession;
import com.example.healthapp.backend.exercise.ExerciseType;
import com.example.healthapp.backend.exercise.RESTTaskGetExerciseSessions;

import java.util.function.Consumer;

public class ExerciseDistanceGoal extends Goal {
    public static final String TYPE = "Perform Exercise";

    public static Goal create(String name, float distance, ExerciseType filter) {
        return new ExerciseDistanceGoal(name, distance, true, null, filter.name());
    }

    private final ExerciseType exerciseType;

    public static void register() { /* Call the static { ... } block below */ }
    static { Goal.registerGoalType(TYPE, ExerciseDistanceGoal.class); }

    public ExerciseDistanceGoal(String name, float distance, boolean active, String comparison, String parameter) { super(name, distance, active); exerciseType = ExerciseType.valueOf(parameter); }
    @Override public String getGoalType() { return TYPE; }
    @Override public String getGoalComparison() { return Goal.COMP_MINIMUM; }
    @Override public String getGoalParameter() { return exerciseType.name(); }

    @Override
    public String getDescription() {
        String measuredThing = exerciseType.getMeasuredValueName();
        String unit          = exerciseType.getUnitName();
        String unitSep       = unit.length() == 0 ? "" : " of";

        return getGoalType() + ": " + getThreshold() + " " + unit + unitSep + " " + measuredThing + " (" + exerciseType + ")";
    }

    @Override
    public void calculateIsMetToday(Consumer<Boolean> answerCallback, Consumer<String> failureMessageCallback) {
        Consumer<ExerciseSession[]> exersReceiver = exers -> {
            float totalThing = 0;
            for(ExerciseSession es : exers) if(es.getExerciseType() == exerciseType) totalThing += es.getMeasuredValue();

            answerCallback.accept(totalThing >= getThreshold());
        };

        Consumer<String> failureReceiver = errMsg -> {
            failureMessageCallback.accept("Failed to calculate if exercise goal \"" + getName() + "\" was completed!! Error is: " + errMsg);
            answerCallback.accept(false);
        };

        RESTTaskGetExerciseSessions.enqueue(exersReceiver, failureReceiver);
    }
}