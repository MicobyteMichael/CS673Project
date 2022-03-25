package com.example.healthapp.ui.exercise;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.healthapp.R;
import com.example.healthapp.backend.exercise.ExerciseSession;
import com.example.healthapp.backend.exercise.ExerciseType;
import com.example.healthapp.backend.exercise.RESTTaskEndExerciseSession;
import com.example.healthapp.backend.exercise.RESTTaskGetExerciseSessions;
import com.example.healthapp.backend.exercise.RESTTaskStartExerciseSession;
import com.example.healthapp.ui.MainActivity;

import org.w3c.dom.Text;

import java.time.Duration;
import java.time.Instant;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class ExercisingFragment extends Fragment {

    public static void startExerciseSession(ExerciseType type, Activity act, Consumer<String> dialogGenerator) {
        EditText nameInputter = new EditText(act);
        nameInputter.setInputType(InputType.TYPE_CLASS_TEXT);

        new AlertDialog.Builder(act)
            .setTitle("Please input a name for this exercise session")
            .setView(nameInputter)
            .setPositiveButton("OK", (d, w) -> {
                String name = nameInputter.getText().toString();

                RESTTaskStartExerciseSession.enqueue(name, type, () -> {
                    ((MainActivity)act).showFrag(new ExercisingFragment(name, type));
                }, err -> dialogGenerator.accept("A session with that name already exists, please choose another."));
            })
            .setNegativeButton("Cancel", (d, w) -> d.cancel())
            .show();
    }

    public static void endExerciseSession(String name, Consumer<String> dialogGenerator, Runnable onSuccess) {
        RESTTaskEndExerciseSession.enqueue(name, 0, 0, 0, () -> { // TODO implement actual calorie, heart rate, and distance
            dialogGenerator.accept("Saved exercise session \"" + name + "\"");
            onSuccess.run();
        }, err -> dialogGenerator.accept("Failed to exercise session"));
    }

    private final String name;
    private final ExerciseType type;
    private Timer timer;

    public ExercisingFragment(String name, ExerciseType type) {
        super(R.layout.fragment_exercising);
        this.name = name;
        this.type = type;
        timer = new Timer();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ((TextView)getActivity().findViewById(R.id.textViewCurrentlyExercising)).setText("Currently Exercising: \"" + name + "\"");
        ((TextView)getActivity().findViewById(R.id.textViewExerciseNameAndDesc)).setText(type.getActivityName() + ": " + type.getDescription());

        Consumer<String> dialogGen = msg -> new AlertDialog.Builder(getActivity()).setMessage(msg).setNeutralButton("OK", null).show();
        getActivity().findViewById(R.id.buttonEndExerciseSession).setOnClickListener(v -> endExerciseSession(name, dialogGen, () -> {
            timer.cancel();
            ((MainActivity)getActivity()).showFrag(ExerciseFragment.class);
        }));

        RESTTaskGetExerciseSessions.enqueue(exers -> {
            for(ExerciseSession s : exers) if(s.getName().equals(name)) {
                ((TextView)getActivity().findViewById(R.id.textViewStartTime2)).setText(s.getStartFormatted());

                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(() -> {
                            long minutes = Duration.between(s.getStart(), Instant.now()).getSeconds() / 60;

                            TextView tv = getActivity().findViewById(R.id.textViewDuration2);
                            if(tv != null) tv.setText((minutes / 60) + " hours, " + (minutes % 60) + " minutes");
                        });
                    }
                }, 0, 1000);

                break;
            }
        }, err -> dialogGen.accept("Failed to calculate start time"));
    }
}