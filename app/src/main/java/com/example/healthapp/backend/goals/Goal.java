package com.example.healthapp.backend.goals;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.function.Consumer;

public abstract class Goal {
    public static final String COMP_MINIMUM = "At least";
    public static final String COMP_MAXIMUM = "At most";

    private static HashMap<String, Class<? extends Goal>> registeredGoalTypes = new HashMap<>();
    public static void registerGoalType(String type, Class<? extends Goal> goalClass) { registeredGoalTypes.put(type, goalClass); }

    private static void registerDefaultGoalTypes() {
        // Safe to call these multiple times since these methods just trigger the static { ... } blocks in their classes
        StepsGoal                 .register();
        ExerciseCaloriesBurnedGoal.register();
        ExerciseDistanceGoal      .register();
        ExerciseDurationGoal      .register();
        CaloriesConsumedGoal      .register();
        WatersConsumedGoal        .register();
        SleepGoal                 .register();
    }

    public static Goal parseGoal(String name, String goalType, String goalComparison, float threshold, String parameter, boolean active) {
        registerDefaultGoalTypes();

        for(String registeredType : registeredGoalTypes.keySet()) {
            if(goalType.equals(registeredType)) {
                Constructor<?>[] cons = registeredGoalTypes.get(goalType).getDeclaredConstructors();

                try {
                    if(cons.length != 1) throw new IllegalArgumentException();
                    return (Goal)cons[0].newInstance(name, threshold, active, goalComparison, parameter);
                } catch(Exception e) {
                    throw new RuntimeException("Each goal class must have one constructor which accepts the name, threshold, active status, comparison, and parameter, in that order!", e);
                }
            }
        }

        System.err.println("Unknown goal type \"" + goalType + "\"!!");
        return null;
    }

    private String name;
    private float threshold;
    private boolean active;

    public Goal(String name, float threshold, boolean active) {
        this.name = name;
        this.threshold = threshold;
        this.active = active;
    }

    public String getName() { return name; }
    public float getThreshold() { return threshold; }
    public boolean isActive() { return active; }

    public void setActive(boolean active) { this.active = active; }
    public void setName(String name) { this.name = name; }

    public String getDescription() {
        String param = getGoalParameter();
        if(param == null) param = "";
        else param += " ";

        return getGoalComparison() + " " + getThreshold() + " " + param + getGoalType();
    }

    public abstract String getGoalType();
    public abstract String getGoalComparison();
    public abstract String getGoalParameter();

    public abstract void calculateIsMetToday(Consumer<Boolean> answerCallback, Consumer<String> failureMessageCallback);
}
