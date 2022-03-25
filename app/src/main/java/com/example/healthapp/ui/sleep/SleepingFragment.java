package com.example.healthapp.ui.sleep;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.healthapp.R;
import com.example.healthapp.backend.sleeptracking.RESTTaskEndSleepSession;
import com.example.healthapp.backend.sleeptracking.RESTTaskGetSleep;
import com.example.healthapp.backend.sleeptracking.RESTTaskStartSleepSession;
import com.example.healthapp.backend.sleeptracking.SleepSession;
import com.example.healthapp.ui.MainActivity;

import org.w3c.dom.Text;

import java.time.Duration;
import java.time.Instant;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class SleepingFragment extends Fragment {

    public static void startSleepSession(Activity act, Consumer<String> dialogGenerator) {
        EditText nameInputter = new EditText(act);
        nameInputter.setInputType(InputType.TYPE_CLASS_TEXT);

        new AlertDialog.Builder(act)
            .setTitle("Please input a name for this sleep session")
            .setView(nameInputter)
            .setPositiveButton("OK", (d, w) -> {
                String name = nameInputter.getText().toString();

                RESTTaskStartSleepSession.enqueue(name, () -> {
                    ((MainActivity)act).showFrag(new SleepingFragment(name));
                }, err -> dialogGenerator.accept("A session with that name already exists, please choose another."));
            })
            .setNegativeButton("Cancel", (d, w) -> d.cancel())
            .show();
    }

    public static void endSleepSession(String name, Consumer<String> dialogGenerator, Runnable onSuccess) {
        RESTTaskEndSleepSession.enqueue(name, () -> {
            dialogGenerator.accept("Saved sleep session \"" + name + "\"");
            onSuccess.run();
        }, err -> dialogGenerator.accept("Failed to save session"));
    }

    private final String name;
    private Timer timer;

    public SleepingFragment(String name) {
        super(R.layout.fragment_sleeping);
        this.name = name;
        timer = new Timer();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ((TextView)getActivity().findViewById(R.id.textViewCurrentlySleeping)).setText("Currently Sleeping: \"" + name + "\"");

        Consumer<String> dialogGen = msg -> new AlertDialog.Builder(getActivity()).setMessage(msg).setNeutralButton("OK", null).show();
        getActivity().findViewById(R.id.buttonEndSleepSession).setOnClickListener(v -> endSleepSession(name, dialogGen, () -> {
            timer.cancel();
            ((MainActivity)getActivity()).showFrag(SleepSessionsFragment.class);
        }));

        RESTTaskGetSleep.enqueue(sleeps -> {
            for(SleepSession s : sleeps) if(s.getName().equals(name)) {
                ((TextView)getActivity().findViewById(R.id.textViewStartTime)).setText(s.getStartFormatted());

                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(() -> {
                            long minutes = Duration.between(s.getStart(), Instant.now()).getSeconds() / 60;
                            ((TextView) getActivity().findViewById(R.id.textViewDuration)).setText((minutes / 60) + " hours, " + (minutes % 60) + " minutes");
                        });
                    }
                }, 0, 1000);

                break;
            }
        }, err -> dialogGen.accept("Failed to calculate start time"));
    }
}