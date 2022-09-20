package com.group.ddjjnews.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public abstract class RefreshFloatingBaseFragment extends RefreshBaseFragment {
    protected FloatingActionButton floatingAction;

    public RefreshFloatingBaseFragment() {}

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        floatingAction.setOnClickListener(this::handleFloatingAB);
    }
    protected abstract void handleFloatingAB(View view);
}