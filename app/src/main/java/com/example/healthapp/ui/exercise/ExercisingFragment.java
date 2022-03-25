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
import java.util.function.Function;

public class ExercisingFragment extends Fragment {

    private static void getTextRaw(Activity act, String msg, int inputType, Consumer<String> dialogGenerator, Consumer<String> acceptor, String defaultVal) {
        EditText valInputter = new EditText(act);
        valInputter.setInputType(inputType);

        if(defaultVal != null) valInputter.setText(defaultVal);

        new AlertDialog.Builder(act)
            .setTitle(msg)
            .setView(valInputter)
            .setPositiveButton("OK", (d, w) -> {
                String val = valInputter.getText().toString();

                if(val.length() == 0) dialogGenerator.accept("Please input a value!");
                else acceptor.accept(val);
            })
            .setNegativeButton("Cancel", (d, w) -> d.cancel())
            .show();
    }

    private static void getFloatRaw(Activity act, String msg, int inputType, Consumer<String> dialogGenerator, Consumer<Float> acceptor, String defaultVal) {
        Consumer<String> acceptorProxy = str -> {
            try {
                acceptor.accept(Float.parseFloat(str));
            } catch(Exception e) {
                dialogGenerator.accept("Please input a valid number!");
                e.printStackTrace();
            }
        };

        getTextRaw(act, msg, inputType, dialogGenerator, acceptorProxy, defaultVal);
    }

    private static void getInt(Activity act, String msg, Consumer<String> dialogGenerator, Consumer<Integer> acceptor, Integer defaultVal) {
        Consumer<Float> acceptorProxy = f -> {
            int i = f.intValue();
            if(f == i) acceptor.accept(i);
            else dialogGenerator.accept("Please input a whole number!");
        };

        getFloatRaw(act, msg, InputType.TYPE_CLASS_NUMBER, dialogGenerator, acceptorProxy, defaultVal == null ? null : String.valueOf(defaultVal.intValue()));
    }

    public static void getText(Activity act, String msg, Consumer<String> dialogGenerator, Consumer<String> acceptor) { getTextRaw(act, msg, InputType.TYPE_CLASS_TEXT, dialogGenerator, acceptor, null); }
    public static void getFloat(Activity act, String msg, Consumer<String> dialogGenerator, Consumer<Float> acceptor, Float defaultVal) { getFloatRaw(act, msg, InputType.TYPE_NUMBER_FLAG_DECIMAL, dialogGenerator, acceptor, defaultVal == null ? null : String.valueOf(defaultVal.floatValue())); }

    public static void startExerciseSession(ExerciseType type, Activity act, Consumer<String> dialogGenerator) {
        getText(act, "Please input a name for this exercise session", dialogGenerator, name -> {
            RESTTaskStartExerciseSession.enqueue(name, type, () -> {
                ((MainActivity)act).showFrag(new ExercisingFragment(new ExerciseSession(name, type, Instant.now(), null, 0, 0, 0)));
            }, err -> dialogGenerator.accept("A session with that name already exists, please choose another."));
        });
    }

    public static void endExerciseSession(ExerciseSession s, Consumer<String> dialogGenerator, Activity act, Runnable onSuccess) {
        float hours = Duration.between(s.getStart(), Instant.now()).getSeconds() / 60F / 60F;
        int heart = s.getExerciseType().getAverageHeartRate();
        int cals  = Math.round(s.getExerciseType().getCaloriesPerHour() * hours);

        getInt(act, "Please input your average heart rate.", dialogGenerator, heartRate -> {
            getInt(act, "Please input your number of calories burned.", dialogGenerator, calories -> {
                Consumer<Number> submitter = measuredValue -> {
                    RESTTaskEndExerciseSession.enqueue(s.getName(), heartRate, calories, measuredValue.floatValue(), () -> {
                        dialogGenerator.accept("Saved exercise session \"" + s.getName() + "\"");
                        onSuccess.run();
                    }, err -> dialogGenerator.accept("Failed to exercise session"));
                };

                String m = "Please input your " + s.getExerciseType().getMeasuredValueName();
                String u = s.getExerciseType().getUnitName();
                if(u.length() > 0) m += (" in " + u);

                if(s.getExerciseType().isIntegerQuantity()) getInt(act, m, dialogGenerator, i -> submitter.accept(i), null);
                else getFloat(act, m, dialogGenerator, f -> submitter.accept(f), null);
            }, cals);
        }, heart);
    }

    private final ExerciseSession session;
    private Timer timer;

    public ExercisingFragment(ExerciseSession session) {
        super(R.layout.fragment_exercising);
        this.session = session;
        timer = new Timer();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ((TextView)getActivity().findViewById(R.id.textViewCurrentlyExercising)).setText("Currently Exercising: \"" + session.getName() + "\"");
        ((TextView)getActivity().findViewById(R.id.textViewExerciseNameAndDesc)).setText(session.getExerciseType().getActivityName() + ": " + session.getExerciseType().getDescription());
        ((TextView)getActivity().findViewById(R.id.textViewStartTime2)).setText(session.getStartFormatted());

        Consumer<String> dialogGen = msg -> new AlertDialog.Builder(getActivity()).setMessage(msg).setNeutralButton("OK", null).show();
        getActivity().findViewById(R.id.buttonEndExerciseSession).setOnClickListener(v -> endExerciseSession(session, dialogGen, getActivity(), () -> {
            timer.cancel();
            ((MainActivity)getActivity()).showFrag(ExerciseFragment.class);
        }));


        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
            getActivity().runOnUiThread(() -> {
                long minutes = Duration.between(session.getStart(), Instant.now()).getSeconds() / 60;

                TextView tv = getActivity().findViewById(R.id.textViewDuration2);
                if(tv != null) tv.setText((minutes / 60) + " hours, " + (minutes % 60) + " minutes");
            });
            }
        }, 0, 1000);
    }
}