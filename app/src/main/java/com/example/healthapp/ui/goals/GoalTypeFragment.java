package com.example.healthapp.ui.goals;

import androidx.fragment.app.Fragment;

import com.example.healthapp.backend.goals.Goal;

import java.util.function.Consumer;

public abstract class GoalTypeFragment extends Fragment {

    protected GoalTypeFragment(int layout) { super(layout); }
    public abstract void onSubmitClicked(Consumer<Goal> onSuccess, Consumer<String> onFailure);
}
