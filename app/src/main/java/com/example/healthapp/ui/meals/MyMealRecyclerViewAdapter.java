package com.example.healthapp.ui.meals;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.healthapp.backend.foodanddrink.Meal;
import com.example.healthapp.databinding.FragmentMealsBinding;

import java.util.List;

public class MyMealRecyclerViewAdapter extends RecyclerView.Adapter<MyMealRecyclerViewAdapter.ViewHolder> {

    private final Meal[] mValues;

    public MyMealRecyclerViewAdapter(Meal[] items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentMealsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues[position];
        holder.mIdView.setText(mValues[position].getName());
        holder.mContentView.setText(mValues[position].getCalories() + " calories");
    }

    @Override
    public int getItemCount() {
        return mValues.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public Meal mItem;

        public ViewHolder(FragmentMealsBinding binding) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
            mContentView = binding.content;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}