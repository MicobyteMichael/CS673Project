package com.example.healthapp.backend.exercise;

public enum ExerciseType {

    Walking     ("Just walking, at a normal, steady pace.",                    "distance", "miles", false, DifficultyLevel.Moderate),
    Jogging     ("Jogging at a moderate pace",                                 "distance", "miles", false, DifficultyLevel.Strenuous),
    Running     ("Running at a very fast pace",                                "distance", "miles", false, DifficultyLevel.VeryStrenuous),
    Swimming    ("Doing laps in a standard-size pool",                         "laps",     "",      true,  DifficultyLevel.Strenuous),
    Cycling     ("Riding with a mix of inclines, declines, and flat sections", "distance", "miles", false, DifficultyLevel.Moderate),
    JumpRope    ("Jumping rope at a moderate pace",                            "jumps",    "",      true,  DifficultyLevel.Moderate),
    Squats      ("Squatting from a standing position, then standing back up",  "squats",   "",      true,  DifficultyLevel.Moderate),
    JumpingJacks("Jumping up and simultaneously moving your arms",             "jumps",    "",      true,  DifficultyLevel.Strenuous),
    PushUps     ("Doing standard pushups and keeping your back straight",      "push-ups", "",      true,  DifficultyLevel.Strenuous),
    SitUps      ("Laying on your back and moving into a sitting position",     "sit-ups",  "",      true,  DifficultyLevel.Moderate),
    PullUps     ("Pulling yourself up by your arms and clearing the bar",      "pull-ups", "",      true,  DifficultyLevel.VeryStrenuous);

    // Calories per hour based on https://www.onhealth.com/content/1/calories_burned_during_fitness
    // Heart rate based on https://www.mayoclinic.org/healthy-lifestyle/fitness/in-depth/exercise-intensity/art-20046887 and https://www.mayoclinic.org/healthy-lifestyle/fitness/expert-answers/heart-rate/faq-20057979
    public static enum DifficultyLevel {
        Moderate(415, 152), Strenuous(655, 174), VeryStrenuous(830, 188);

        private final int calsPerHour, heart;
        private DifficultyLevel(int calsPerHour, int heart) { this.calsPerHour = calsPerHour; this.heart = heart; }
    }

    private final String description;
    private final String measuredValueName, unitName;
    private final boolean isIntegerQuantity;
    private final DifficultyLevel lvl;

    private ExerciseType(String description, String measuredValueName, String unitName, boolean isIntegerQuantity, DifficultyLevel lvl) {
        this.description = description;
        this.measuredValueName = measuredValueName;
        this.unitName = unitName;
        this.isIntegerQuantity = isIntegerQuantity;
        this.lvl = lvl;
    }

    public String getActivityName() { return name(); }
    public String getDescription() { return description; }
    public String getMeasuredValueName() { return measuredValueName; }
    public String getUnitName() { return unitName; }
    public boolean isIntegerQuantity() { return isIntegerQuantity; }
    public DifficultyLevel getDifficulty() { return lvl; }
    public int getCaloriesPerHour() { return lvl.calsPerHour; }
    public int getAverageHeartRate() { return lvl.heart; }
}
