package com.example.healthapp.ui.goals;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.healthapp.R;
import com.example.healthapp.backend.exercise.RESTTaskSetSteps;
import com.example.healthapp.backend.goals.RESTTaskGetGoals;
import com.example.healthapp.ui.MainActivity;

public class GoalsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goals_list, container, false);
        View view2 = view.findViewById(R.id.listGoals);
        View view3 = view.findViewById(R.id.buttonCreateGoal);
        View view4 = view.findViewById(R.id.textViewNoGoalsYet);

        if(view2 instanceof RecyclerView) {
            Context context = view2.getContext();
            RecyclerView recyclerView = (RecyclerView)view2;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            RESTTaskGetGoals.enqueue(goals -> {
                recyclerView.setAdapter(new MyGoalRecyclerViewAdapter(goals, this::refresh, this::error));
                if(goals.length > 0) view4.setVisibility(View.GONE);
            }, this::error);
        }

        view3.setOnClickListener(v -> {
            System.out.println("Add goal!!!");
        });

        return view;
    }

    private void refresh() {
        ((MainActivity)getActivity()).showFrag(GoalsFragment.class);
    }

    private void error(String msg) {
        new AlertDialog.Builder(getActivity()).setMessage(msg).setNeutralButton("OK", null).show();
    }
}