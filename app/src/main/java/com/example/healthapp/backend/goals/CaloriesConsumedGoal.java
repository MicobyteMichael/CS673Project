package com.example.healthapp.backend.goals;

import com.example.healthapp.backend.foodanddrink.RESTTaskGetMeals;
import com.example.healthapp.backend.foodanddrink.Meal;

import java.util.function.BiFunction;
import java.util.function.Consumer;

public class CaloriesConsumedGoal extends Goal {
    public static final String TYPE = "Calories Consumed";

    public static Goal create(String name, int calories, boolean minOrMax) {
        return new CaloriesConsumedGoal(name, calories, true, minOrMax ? Goal.COMP_MINIMUM : Goal.COMP_MAXIMUM, null);
    }

    public static void register() { /* Call the static { ... } block below */ }
    static { Goal.registerGoalType(TYPE, CaloriesConsumedGoal.class); }

    private final String comparison;

    public CaloriesConsumedGoal(String name, float numCalories, boolean active, String comparison, String parameter) { super(name, numCalories, active); this.comparison = comparison; }
    @Override public String getGoalType() { return TYPE; }
    @Override public String getGoalComparison() { return comparison; }
    @Override public String getGoalParameter() { return null; }

    @Override
    public void calculateIsMetToday(Consumer<Boolean> answerCallback, Consumer<String> failureMessageCallback) {
        BiFunction<Integer, Integer, Boolean> comp;
        if(getGoalComparison().equals(Goal.COMP_MINIMUM)) comp = (i, j) -> i >= j;
        else comp = (i, j) -> i <= j;

        Consumer<Meal[]> mealsReceiver = meals -> {
            int totalCalories = 0;
            for(Meal m : meals) totalCalories += m.getCalories();
            answerCallback.accept(comp.apply(totalCalories, (int)getThreshold()));
        };

        Consumer<String> failureReceiver = errMsg -> {
            failureMessageCallback.accept("Failed to calculate if calories goal \"" + getName() + "\" was completed!! Error is: " + errMsg);
            answerCallback.accept(false);
        };

        RESTTaskGetMeals.enqueue(mealsReceiver, failureReceiver);
    }
}