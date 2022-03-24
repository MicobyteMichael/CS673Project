package com.example.healthapp.ui.sleep;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.healthapp.R;
import com.example.healthapp.backend.sleeptracking.RESTTaskEndSleepSession;
import com.example.healthapp.backend.sleeptracking.RESTTaskStartSleepSession;
import com.example.healthapp.ui.MainActivity;

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

    public static void endSleepSession(String name, Consumer<String> dialogGenerator) {
        RESTTaskEndSleepSession.enqueue(name, () -> dialogGenerator.accept("Saved sleep session \"" + name + "\""), err -> dialogGenerator.accept("Failed to save session"));
    }

    private final String name;

    public SleepingFragment(String name) {
        super(R.layout.fragment_sleeping);
        this.name = name;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }
}