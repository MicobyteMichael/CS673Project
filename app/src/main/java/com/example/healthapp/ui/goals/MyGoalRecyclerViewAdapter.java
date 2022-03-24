package com.example.healthapp.ui.goals;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.healthapp.backend.goals.Goal;
import com.example.healthapp.backend.goals.RESTTaskSetGoalActive;
import com.example.healthapp.backend.goals.RESTTaskSubmitGoal;
import com.example.healthapp.backend.goals.RESTTaskSubmitGoalAchievement;
import com.example.healthapp.databinding.FragmentGoalBinding;

import java.util.function.Consumer;

public class MyGoalRecyclerViewAdapter extends RecyclerView.Adapter<MyGoalRecyclerViewAdapter.ViewHolder> {

    private final Goal[] mValues;
    private final Runnable refreshPage;
    private final Consumer<String> errReporter;

    public MyGoalRecyclerViewAdapter(Goal[] items, Runnable refreshPage, Consumer<String> errReporter) { mValues = items; this.refreshPage = refreshPage; this.errReporter = errReporter; }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentGoalBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Goal g = mValues[position];

        if(g.isActive()) {
            g.calculateIsMetToday(achieved -> {
                holder.achieved.setText("Achieved: " + (achieved ? "Yes!" : "Not yet"));
                holder.achieved.setVisibility(View.VISIBLE);
                holder.submitSuccess.setVisibility(View.VISIBLE);

                holder.submitSuccess.setOnClickListener(v -> {
                    if(!achieved) {
                        errReporter.accept("You haven't met this goal yet!");
                    } else {
                        RESTTaskSubmitGoalAchievement.enqueue(g.getName(), () -> {
                            errReporter.accept("Your success has been saved!");
                            refreshPage.run();
                        }, errReporter);
                    }
                });
            }, err -> errReporter.accept("Failed to check if goal \"" + g.getName() + "\" was achieved."));
        } else {
            holder.achieved.setText("");
            holder.achieved.setVisibility(View.GONE);
            holder.submitSuccess.setVisibility(View.GONE);
            holder.submitSuccess.setOnClickListener(v -> {});
        }

        holder.mItem = g;
        holder.mIdView.setText("Name: " + g.getName());
        holder.mContentView.setText("Summary: " + g.getDescription());

        holder.active.setText(g.isActive() ? "Deactivate" : "Activate");
        holder.active.setOnClickListener(v -> {
            RESTTaskSetGoalActive.enqueue(g.getName(), !g.isActive(), refreshPage, msg -> errReporter.accept("Failed to change active status of goal \"" + g.getName() + "\""));
        });
    }

    @Override
    public int getItemCount() {
        return mValues.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView, mContentView, achieved;
        public final Button active, submitSuccess;
        public Goal mItem;

        public ViewHolder(FragmentGoalBinding binding) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
            mContentView = binding.content;
            achieved = binding.achieved;
            active = binding.active;
            submitSuccess = binding.submitSuccess;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}