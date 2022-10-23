package com.group.ddjjnews.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.group.ddjjnews.Utils.SpaceItemDecoration;

import com.group.ddjjnews.databinding.FragmentRefreshBaseBinding;


public class CategoryListFragment extends RefreshBaseFragment{
    protected FragmentRefreshBaseBinding binding;

    public CategoryListFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentRefreshBaseBinding.inflate(inflater, container, false);
        this.rcItems = binding.rcView;
        this.sRefresh = binding.sRefresh;
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.layoutManager = new GridLayoutManager(getContext(), 2);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.rcView.addItemDecoration(new SpaceItemDecoration(16, true));
        displayCats();
    }

    @Override
    protected void handleRefresh(SwipeRefreshLayout swipeCont) {
        displayCats();
    }

    public void displayCats() {}
}
