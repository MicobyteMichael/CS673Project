package com.example.healthapp.ui.bodycomp;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.healthapp.R;
import com.example.healthapp.backend.bodycomp.BodyComposition;
import com.example.healthapp.backend.bodycomp.RESTTaskGetAllBodyCompositions;
import com.example.healthapp.backend.bodycomp.RESTTaskSubmitBodyComposition;
import com.example.healthapp.ui.MainActivity;

import java.time.LocalDate;
import java.util.function.Consumer;

public class BodyCompositionFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_body_comp_list, container, false);
        View view2 = view.findViewById(R.id.list_body_compositions);
        View view3 = view.findViewById(R.id.buttonSubmitTodaysBodyComp);
        View view4 = view.findViewById(R.id.textViewNoBodyCompsYet);

        Consumer<String> errGenerator = msg -> new AlertDialog.Builder(getActivity()).setMessage(msg).setNeutralButton("OK", null).show();

        if(view2 instanceof RecyclerView) {
            Context context = view2.getContext();
            RecyclerView recyclerView = (RecyclerView)view2;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            RESTTaskGetAllBodyCompositions.enqueue(comps -> {
                if(comps.size() > 0) view4.setVisibility(View.GONE);
                recyclerView.setAdapter(new MyBodyCompositionRecyclerViewAdapter(comps));

                LocalDate today = LocalDate.now();
                boolean[] hasToday = { false };

                for(BodyComposition bc : comps) {
                    LocalDate date = bc.getMeasuredOn();

                    if(today.equals(date)) {
                        hasToday[0] = true;
                        break;
                    }
                }

                view3.setOnClickListener(v -> {
                    if(hasToday[0]) errGenerator.accept("Your body composition for today has already been recorded.");
                    else ((MainActivity)getActivity()).showFrag(InputBodyCompositionFragment.class);
                });
            }, errGenerator);
        }
        return view;
    }
}