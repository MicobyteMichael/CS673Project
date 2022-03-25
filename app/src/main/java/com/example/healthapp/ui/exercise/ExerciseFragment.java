package com.example.healthapp.ui.exercise;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.healthapp.R;
import com.example.healthapp.backend.exercise.ExerciseType;
import com.example.healthapp.backend.exercise.RESTTaskGetExerciseSessions;
import com.example.healthapp.backend.exercise.RESTTaskStartExerciseSession;
import com.example.healthapp.ui.MainActivity;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;

public class ExerciseFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise_list, container, false);
        View view2 = view.findViewById(R.id.listExerciseSessions);
        View view3 = view.findViewById(R.id.buttonStartExerciseSession);
        View view4 = view.findViewById(R.id.textViewNoExerciseYet);

        Consumer<String> errGenerator = msg -> new AlertDialog.Builder(getActivity()).setMessage(msg).setNeutralButton("OK", null).show();

        if(view2 instanceof RecyclerView) {
            Context context = view2.getContext();
            RecyclerView recyclerView = (RecyclerView)view2;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            RESTTaskGetExerciseSessions.enqueue(sessions -> {
                recyclerView.setAdapter(new MyExerciseSessionRecyclerViewAdapter(sessions, errGenerator, getActivity(), () -> ((MainActivity)getActivity()).showFrag(ExerciseFragment.class)));
                if(sessions.length > 0) view4.setVisibility(View.GONE);
            }, errGenerator);
        }

        view3.setOnClickListener(v -> {
            ArrayList<String> types = new ArrayList<>();
            for(ExerciseType et : ExerciseType.values()) types.add(et.name());

            ArrayAdapter<String> adap = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, types);
            adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Spinner typeChooser = new Spinner(getActivity());
            typeChooser.setAdapter(adap);

            new AlertDialog.Builder(getActivity())
                .setTitle("Choose an exercise type")
                .setView(typeChooser)
                .setPositiveButton("OK", (d, w) -> {
                    String name = (String)typeChooser.getSelectedItem();

                    if(name == null) {
                        errGenerator.accept("Please choose an exercise type");
                    } else {
                        ExercisingFragment.startExerciseSession(ExerciseType.valueOf(name), getActivity(), errGenerator);
                    }
                })
                .setNegativeButton("Cancel", (d, w) -> d.cancel())
                .show();
        });

        return view;
    }
}