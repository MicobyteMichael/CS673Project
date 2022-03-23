package com.example.healthapp.ui.bodycomp;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.healthapp.backend.bodycomp.BodyComposition;
import com.example.healthapp.databinding.FragmentBodyCompBinding;

import java.util.List;

public class MyBodyCompositionRecyclerViewAdapter extends RecyclerView.Adapter<MyBodyCompositionRecyclerViewAdapter.ViewHolder> {

    private final List<BodyComposition> mValues;

    public MyBodyCompositionRecyclerViewAdapter(List<BodyComposition> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentBodyCompBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        BodyComposition bc = mValues.get(position);
        holder.mItem = bc;
        holder.mIdView.setText(bc.getDateMeasuredOn());
        holder.mContentView.setText(mValues.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public BodyComposition mItem;

        public ViewHolder(FragmentBodyCompBinding binding) {
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