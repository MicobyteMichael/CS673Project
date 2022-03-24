package com.example.healthapp.ui.meals;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.healthapp.R;
import com.example.healthapp.backend.foodanddrink.RESTTaskGetMeals;
import com.example.healthapp.backend.foodanddrink.RESTTaskGetWaters;
import com.example.healthapp.backend.foodanddrink.RESTTaskSetWaters;
import com.example.healthapp.ui.MainActivity;

import java.util.function.Consumer;

public class MealsFragment extends Fragment {

    private int numWaters = 0;
    private TextView waters;

    private void error(String msg) {
        new AlertDialog.Builder(getActivity()).setMessage(msg).setNeutralButton("OK", null).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meals_list, container, false);
        View view2 = view.findViewById(R.id.listMeals);
        View view3 = view.findViewById(R.id.buttonSubmitMeal);
        View view4 = view.findViewById(R.id.textViewNoMealsYet);

        View btnAddWater = view.findViewById(R.id.buttonIncreaseWaters);
        View btnSubWater = view.findViewById(R.id.buttonDecreaseWaters);
        waters = (TextView)view.findViewById(R.id.textViewNumGlasses);

        if(view2 instanceof RecyclerView) {
            Context context = view2.getContext();
            RecyclerView recyclerView = (RecyclerView)view2;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            RESTTaskGetMeals.enqueue(meals -> {
                recyclerView.setAdapter(new MyMealRecyclerViewAdapter(meals));
                if(meals.length > 0) view4.setVisibility(View.GONE);
            }, this::error);
        }

        view3.setOnClickListener(v -> {
            ((MainActivity)getActivity()).showFrag(SubmitMealFragment.class);
        });

        btnAddWater.setOnClickListener(v -> changeWater( 1));
        btnSubWater.setOnClickListener(v -> changeWater(-1));

        RESTTaskGetWaters.enqueue(w -> {
            numWaters = w;
            waters.setText(String.valueOf(w));
        }, err -> error("Failed to retrieve number of waters!"));

        return view;
    }

    private void changeWater(int delta) {
        if(delta > 0 || numWaters + delta >= 0) {
            RESTTaskSetWaters.enqueue(numWaters + delta, () -> {
                numWaters += delta;
                waters.setText(String.valueOf(numWaters));
            }, msg -> error("Failed to update waters!"));
        }
    }
}