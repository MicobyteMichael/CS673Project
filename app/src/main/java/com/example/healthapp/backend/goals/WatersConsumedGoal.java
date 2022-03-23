package com.example.healthapp.backend.goals;

import com.example.healthapp.backend.foodanddrink.RESTTaskGetWaters;

import java.util.function.Consumer;

public class WatersConsumedGoal extends Goal {
    public static final String TYPE = "Water Glasses";

    public static Goal create(String name, int waters) {
        return new WatersConsumedGoal(name, waters, true, null, null);
    }

    public static void register() { /* Call the static { ... } block below */ }
    static { Goal.registerGoalType(TYPE, WatersConsumedGoal.class); }

    public WatersConsumedGoal(String name, float waters, boolean active, String comparison, String parameter) { super(name, waters, active); }
    @Override public String getGoalType() { return TYPE; }
    @Override public String getGoalComparison() { return Goal.COMP_MINIMUM; }
    @Override public String getGoalParameter() { return null; }
    @Override public String getDescription() { return "Drink at least " + getThreshold() + " cups of water"; }

    @Override
    public void calculateIsMetToday(Consumer<Boolean> answerCallback, Consumer<String> failureMessageCallback) {
        Consumer<Integer> watersReceiver = waters  ->   answerCallback.accept(waters >= getThreshold());
        Consumer<String> failureReceiver = errMsg -> { failureMessageCallback.accept("Failed to calculate if waters goal \"" + getName() + "\" was completed!! Error is: " + errMsg); answerCallback.accept(false); };

        RESTTaskGetWaters.enqueue(watersReceiver, failureReceiver);
    }
}