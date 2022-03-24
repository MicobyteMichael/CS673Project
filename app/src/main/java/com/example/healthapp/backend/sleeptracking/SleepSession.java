package com.example.healthapp.backend.sleeptracking;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

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

    public Duration getDuration() {
        Instant start = getStart(), end = getEnd();
        if(start != null && end != null) return Duration.between(start, end);
        else return null;
    }

    public long getStartSecond() {
        if(start != null) return start.getEpochSecond();
        else return -1;
    }

    public long getEndSecond() {
        if(end != null) return end.getEpochSecond();
        else return -1;
    }

    public String getDescription() {
        Instant start = getStart(), end = getEnd();
        Duration dur = getDuration();

        DateTimeFormatter fmt = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.US).withZone(ZoneId.systemDefault());
        String desc = "Started at " + fmt.format(start);

        if(end != null) {
            float hours = dur.getSeconds() / 60F / 60F;
            float hoursRounded = Math.round(hours * 10) / 10F;

            desc += (", Ended at " + fmt.format(end));
            desc += (", Duration: " + hoursRounded + " hours");
        }

        return desc;
    }
}
