package com.example.healthapp.backend.sleeptracking;

import java.time.Instant;

public class SleepSession {

    private final String name;
    private final Instant start, end;

    public SleepSession(String name, long start, long end) { this(name, start == -1 ? null : Instant.ofEpochSecond(start), end == -1 ? null : Instant.ofEpochSecond(end)); }

    public SleepSession(String name, Instant start, Instant end) {
        this.name = name;
        this.start = start;
        this.end = end;
    }

    public String getName() { return name; }
    public Instant getStart() { return start; }
    public Instant getEnd() { return end; }

    public long getStartSecond() {
        if(start != null) return start.getEpochSecond();
        else return -1;
    }

    public long getEndSecond() {
        if(end != null) return end.getEpochSecond();
        else return -1;
    }
}
