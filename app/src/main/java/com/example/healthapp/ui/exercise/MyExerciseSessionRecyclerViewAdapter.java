package com.example.healthapp.ui.exercise;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.healthapp.backend.exercise.ExerciseSession;
import com.example.healthapp.databinding.FragmentExerciseBinding;

public class MyExerciseSessionRecyclerViewAdapter extends RecyclerView.Adapter<MyExerciseSessionRecyclerViewAdapter.ViewHolder> {

    private final ExerciseSession[] mValues;

    public MyExerciseSessionRecyclerViewAdapter(ExerciseSession[] items) { mValues = items; }
    private boolean isEmpty() { return mValues.length == 0; }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentExerciseBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public int getItemCount() {
        if(isEmpty()) return 1;
        else return mValues.length;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if(isEmpty() && position == 0) {
            holder.mItem = null;
            holder.mIdView.setText("");
            holder.mContentView.setText("No exercise sessions yet, feel free to start one!");
        } else {
            holder.mItem = mValues[position];
            holder.mIdView.setText(mValues[position].getName());
            holder.mContentView.setText(mValues[position].getDescription());
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public ExerciseSession mItem;

        public ViewHolder(FragmentExerciseBinding binding) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
            mContentView = binding.content;
        }

        @Override public String toString() { return super.toString() + " '" + mContentView.getText() + "'"; }
    }
}