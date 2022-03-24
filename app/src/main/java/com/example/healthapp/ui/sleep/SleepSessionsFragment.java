package com.example.healthapp.ui.sleep;

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
import com.example.healthapp.backend.sleeptracking.RESTTaskGetSleep;
import com.example.healthapp.ui.MainActivity;

import java.util.function.Consumer;

public class SleepSessionsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sleep_sessions_list, container, false);
        View view2 = view.findViewById(R.id.listSleepSessions);
        View view3 = view.findViewById(R.id.buttonStartSleepSession);
        View view4 = view.findViewById(R.id.textViewNoSleepSessionsYet);

        Consumer<String> errGenerator = msg -> new AlertDialog.Builder(getActivity()).setMessage(msg).setNeutralButton("OK", null).show();
        view3.setOnClickListener(v -> SleepingFragment.startSleepSession(getActivity(), errGenerator));

        if(view2 instanceof RecyclerView) {
            Context context = view2.getContext();
            RecyclerView recyclerView = (RecyclerView)view2;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            RESTTaskGetSleep.enqueue(sleeps -> {
                recyclerView.setAdapter(new MySleepSessionRecyclerViewAdapter(sleeps, errGenerator, () -> ((MainActivity)getActivity()).showFrag(SleepSessionsFragment.class)));
                if(sleeps.length > 0) view4.setVisibility(View.GONE);
            }, err -> errGenerator.accept("Failed to get sleep sessions!"));
        }

        return view;
    }
}