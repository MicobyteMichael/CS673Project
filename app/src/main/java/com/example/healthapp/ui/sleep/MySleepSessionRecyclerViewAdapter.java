package com.example.healthapp.ui.sleep;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.healthapp.backend.sleeptracking.SleepSession;
import com.example.healthapp.databinding.FragmentSleepSessionBinding;

import java.util.function.Consumer;

public class MySleepSessionRecyclerViewAdapter extends RecyclerView.Adapter<MySleepSessionRecyclerViewAdapter.ViewHolder> {

    private final SleepSession[] mValues;
    private final Consumer<String> msgGenerator;
    private final Runnable refresh;

    public MySleepSessionRecyclerViewAdapter(SleepSession[] items, Consumer<String> msgGenerator, Runnable refresh) {
        mValues = items;
        this.msgGenerator = msgGenerator;
        this.refresh = refresh;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentSleepSessionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        SleepSession s = mValues[position];
        holder.mItem = s;
        holder.mIdView.setText(s.getName());
        holder.mContentView.setText(s.getDescription());

        if(s.getEnd() == null) {
            holder.end.setVisibility(View.VISIBLE);
            holder.end.setOnClickListener(v -> SleepingFragment.endSleepSession(s.getName(), msgGenerator));
        } else {
            holder.end.setVisibility(View.GONE);
            holder.end.setOnClickListener(v -> {});
        }
    }

    @Override
    public int getItemCount() {
        return mValues.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public final Button end;
        public SleepSession mItem;

        public ViewHolder(FragmentSleepSessionBinding binding) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
            mContentView = binding.content;
            end = binding.endSleepSession;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}