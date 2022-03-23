package com.example.healthapp.backend.goals;

import com.example.healthapp.backend.foodanddrink.Meal;
import com.example.healthapp.backend.foodanddrink.RESTTaskGetMeals;
import com.example.healthapp.backend.sleeptracking.RESTTaskGetSleep;
import com.example.healthapp.backend.sleeptracking.SleepSession;

import java.time.Duration;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class SleepGoal extends Goal {
    public static final String TYPE = "Hours Slept";

    public static Goal create(String name, float hours, boolean minOrMax) {
        return new SleepGoal(name, hours, true, minOrMax ? Goal.COMP_MINIMUM : Goal.COMP_MAXIMUM, null);
    }

    public static void register() { /* Call the static { ... } block below */ }
    static { Goal.registerGoalType(TYPE, SleepGoal.class); }

    private final String comparison;

    public SleepGoal(String name, float hours, boolean active, String comparison, String parameter) { super(name, hours, active); this.comparison = comparison; }
    @Override public String getGoalType() { return TYPE; }
    @Override public String getGoalComparison() { return comparison; }
    @Override public String getGoalParameter() { return null; }

    @Override
    public void calculateIsMetToday(Consumer<Boolean> answerCallback, Consumer<String> failureMessageCallback) {
        BiFunction<Float, Float, Boolean> comp;
        if(getGoalComparison().equals(Goal.COMP_MINIMUM)) comp = (i, j) -> i >= j;
        else comp = (i, j) -> i <= j;

        Consumer<SleepSession[]> sleepsReceiver = sleeps -> {
            Duration totalSleep = Duration.ZERO;
            for(SleepSession s : sleeps) {
                Duration d = s.getDuration();
                if(d != null) totalSleep = totalSleep.plus(d);
            }

            float hours = totalSleep.getSeconds() / 60F / 60F;
            answerCallback.accept(comp.apply(hours, getThreshold()));
        };

        Consumer<String> failureReceiver = errMsg -> {
            failureMessageCallback.accept("Failed to calculate if sleep goal \"" + getName() + "\" was completed!! Error is: " + errMsg);
            answerCallback.accept(false);
        };

        RESTTaskGetSleep.enqueue(sleepsReceiver, failureReceiver);
    }
}