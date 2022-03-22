package com.example.healthapp;

import com.example.healthapp.backend.bodycomp.*;
import com.example.healthapp.backend.exercise.*;
import com.example.healthapp.backend.foodanddrink.*;
import com.example.healthapp.backend.sleeptracking.*;

import java.util.function.Consumer;

public class APIDemos {

    public static void watersAPIDemo(Consumer<String> msgGenerator) {
        RESTTaskGetWaters.enqueue(waters -> {
            System.out.println(waters + " waters!!");

            RESTTaskSetWaters.enqueue(5, () -> {
                System.out.println("updated!!");

                RESTTaskGetWaters.enqueue(waters2 -> System.out.println(waters2 + " waters!!"), msgGenerator);
            }, msgGenerator);
        }, msgGenerator);
    }

    public static void mealsAPIDemo(Consumer<String> msgGenerator) {
        RESTTaskGetMeals.enqueue(meals -> {
            System.out.println("Found " + meals.length + " meal(s)!");
            for(Meal m : meals) {
                System.out.println("Meal \"" + m.getName() + "\": " + m.getCalories() + " cals");
            }

            RESTTaskSubmitMeal.enqueue(new Meal("test meal " + System.currentTimeMillis(), (int)System.currentTimeMillis()), () -> {
                System.out.println("Submitted!!");

                RESTTaskGetMeals.enqueue(meals2 -> {
                    System.out.println("Found " + meals2.length + " meal(s)!");
                    for (Meal m : meals2) {
                        System.out.println("Meal \"" + m.getName() + "\": " + m.getCalories() + " cals");
                    }
                }, msgGenerator);
            }, msgGenerator);
        }, msgGenerator);
    }

    public static void sleepTrackingAPIDemo(Consumer<String> msgGenerator) {
        RESTTaskGetSleep.enqueue(sleeps -> {
            System.out.println("Found " + sleeps.length + " sleep(s)!");
            for(SleepSession s : sleeps) {
                System.out.println("Sleep session \"" + s.getName() + "\": started at " + s.getStart() + ", ended at " + s.getEnd());
            }

            String sleepName = "test sleep" + System.currentTimeMillis();
            RESTTaskStartSleepSession.enqueue(sleepName, () -> {
                System.out.println("Submitted start!!");

                RESTTaskGetSleep.enqueue(sleeps2 -> {
                    System.out.println("Found " + sleeps2.length + " sleep(s)!");
                    for(SleepSession s : sleeps2) {
                        System.out.println("Sleep session \"" + s.getName() + "\": started at " + s.getStart() + ", ended at " + s.getEnd());
                    }

                    RESTTaskEndSleepSession.enqueue(sleepName, () -> {
                        System.out.println("Submitted end!!");

                        RESTTaskGetSleep.enqueue(sleeps3 -> {
                            System.out.println("Found " + sleeps3.length + " sleep(s)!");
                            for (SleepSession s : sleeps3) {
                                System.out.println("Sleep session \"" + s.getName() + "\": started at " + s.getStart() + ", ended at " + s.getEnd());
                            }
                        }, msgGenerator);
                    }, msgGenerator);
                }, msgGenerator);
            }, msgGenerator);
        }, msgGenerator);
    }

    public static void bodyCompositionTrackingAPIDemo(Consumer<String> msgGenerator) {
        RESTTaskSubmitBodyComposition.enqueue(new BodyComposition(180, 50, 60), () -> {
            System.out.println("updated!!");

            RESTTaskGetBodyComposition.enqueue(comp2 -> {
                System.out.println("Body composition: " + comp2.getWeight() + "lbs., " + comp2.getBodyFatPercentage() + "% body fat, " + comp2.getMuscleWeight() + "lbs. muscle");
            }, msgGenerator);
        }, msgGenerator);
    }

    public static void stepTrackingAPIDemo(Consumer<String> msgGenerator) {
        RESTTaskGetSteps.enqueue(steps -> {
            System.out.println(steps + " steps!!");

            RESTTaskSetSteps.enqueue(5, () -> {
                System.out.println("updated!!");

                RESTTaskGetSteps.enqueue(steps2 -> System.out.println(steps2 + " steps!!"), msgGenerator);
            }, msgGenerator);
        }, msgGenerator);
    }

    public static void exerciseSessionTrackingDemo(Consumer<String> msgGenerator) {
        RESTTaskGetExerciseSessions.enqueue(exers -> {
            System.out.println("Found " + exers.length + " exercise session(s)!");
            for(ExerciseSession s : exers) {
                System.out.println("Exercise session \"" + s.getName() + "\": started at " + s.getStart() + ", ended at " + s.getEnd() + ", exercise type is " + s.getExerciseType() + ", burned " + s.getCaloriesBurned() + " calories, with an average heart rate of " + s.getAverageHeartRate());
            }

            String exName = "test exercise " + System.currentTimeMillis();
            RESTTaskStartExerciseSession.enqueue(exName, "Jogging", () -> {
                System.out.println("Submitted start!!");

                RESTTaskGetExerciseSessions.enqueue(exers2 -> {
                    System.out.println("Found " + exers2.length + " exercise session(s)!");
                    for(ExerciseSession s : exers2) {
                        System.out.println("Exercise session \"" + s.getName() + "\": started at " + s.getStart() + ", ended at " + s.getEnd() + ", exercise type is " + s.getExerciseType() + ", burned " + s.getCaloriesBurned() + " calories, with an average heart rate of " + s.getAverageHeartRate());
                    }

                    RESTTaskEndExerciseSession.enqueue(exName, 50, 150, () -> {
                        System.out.println("Submitted end!!");

                        RESTTaskGetExerciseSessions.enqueue(exers3 -> {
                            System.out.println("Found " + exers3.length + " exercise session(s)!");
                            for(ExerciseSession s : exers3) {
                                System.out.println("Exercise session \"" + s.getName() + "\": started at " + s.getStart() + ", ended at " + s.getEnd() + ", exercise type is " + s.getExerciseType() + ", burned " + s.getCaloriesBurned() + " calories, with an average heart rate of " + s.getAverageHeartRate());
                            }
                        }, msgGenerator);
                    }, msgGenerator);
                }, msgGenerator);
            }, msgGenerator);
        }, msgGenerator);
    }
}
