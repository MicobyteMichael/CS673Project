package com.example.healthapp.ui.exercise;

import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.healthapp.backend.exercise.ExerciseSession;
import com.example.healthapp.databinding.FragmentExerciseBinding;

import java.util.function.Consumer;
import java.util.function.Function;

public class MyExerciseSessionRecyclerViewAdapter extends RecyclerView.Adapter<MyExerciseSessionRecyclerViewAdapter.ViewHolder> {

    private final ExerciseSession[] mValues;
    private final Consumer<String> msgGenerator;
    private final Activity act;
    private final Runnable refresh;

    public MyExerciseSessionRecyclerViewAdapter(ExerciseSession[] items, Consumer<String> msgGenerator, Activity act, Runnable refresh) { mValues = items; this.msgGenerator = msgGenerator; this.act = act; this.refresh = refresh; }
    private boolean isEmpty() { return mValues.length == 0; }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentExerciseBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override public int getItemCount() { return mValues.length; }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ExerciseSession s = mValues[position];

        holder.mItem = s;
        holder.mIdView.setText("Session Name: " + s.getName());
        holder.mContentView.setText(s.getDescription());

        if(s.getEnd() == null) {
            holder.end.setVisibility(View.VISIBLE);
            holder.end.setOnClickListener(v -> ExercisingFragment.endExerciseSession(s, msgGenerator, act, refresh));
        } else {
            holder.end.setVisibility(View.GONE);
            holder.end.setOnClickListener(v -> {});
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public final Button end;
        public ExerciseSession mItem;

        public ViewHolder(FragmentExerciseBinding binding) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
            mContentView = binding.content;
            end = binding.buttonEndExercise;
        }

        @Override public String toString() { return super.toString() + " '" + mContentView.getText() + "'"; }
    }
}