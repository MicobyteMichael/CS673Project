package com.example.healthapp.backend.goals;

import com.example.healthapp.backend.exercise.RESTTaskGetSteps;

import java.util.function.Consumer;

public class StepsGoal extends Goal {
    public static final String TYPE = "Steps Walked";

    public static enum StepGoalType {
        // Stride length info: https://livehealthy.chron.com/average-walking-stride-length-7494.html and https://www.chparks.com/411/How-To-Measure-Steps
        Steps(1), Miles((5280 /* Feet per mile */) / (2.35F /* Av. stride length */));

        private final int coeff;
        private StepGoalType(float coeff) { this.coeff = Math.round(coeff); }
        public int getDistanceCoefficient() { return coeff; }
    }

    private final StepGoalType type;

    public static void register() { /* Call the static { ... } block below */ }
    static { Goal.registerGoalType(TYPE, StepsGoal.class); }

    public StepsGoal(String name, int numSteps, boolean active, String comparison, String stepsOrDistance) {
        super(name, numSteps, active);
        type = StepGoalType.valueOf(stepsOrDistance);
    }

    @Override public String getGoalType() { return TYPE; }
    @Override public String getGoalComparison() { return Goal.COMP_MINIMUM; }
    @Override public String getGoalParameter() { return type.name(); }

    @Override
    public void calculateIsMetToday(Consumer<Boolean> answerCallback, Consumer<String> failureMessageCallback) {
        Consumer<Integer> stepsReceiver  = steps  ->   answerCallback.accept(steps >= (getThreshold() * type.getDistanceCoefficient()));
        Consumer<String> failureReceiver = errMsg -> { failureMessageCallback.accept("Failed to calculate if steps goal \"" + getName() + "\" was completed!! Error is: " + errMsg); answerCallback.accept(false); };

        RESTTaskGetSteps.enqueue(stepsReceiver, failureReceiver);
    }
}