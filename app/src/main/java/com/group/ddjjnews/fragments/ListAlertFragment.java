package com.group.ddjjnews.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.group.ddjjnews.adapters.AlertAdapter;
import com.group.ddjjnews.databinding.FragmentRefreshBaseBinding;
import com.group.ddjjnews.models.Alert;
import com.parse.FunctionCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class ListAlertFragment extends RefreshBaseFragment {
    List<ParseObject> items = new ArrayList<>();
    protected FragmentRefreshBaseBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.adapter = new AlertAdapter(getContext(), items);
        layoutManager = new LinearLayoutManager(getContext());
    }
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getLatestAlerts();
    }

    private void getLatestAlerts() {
        Alert.FindAll(new HashMap<>(), (objects, e) -> {
            if (e == null){
                items.addAll((Collection<? extends ParseObject>) objects);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void loadMore(int page, int totalItemsCount, RecyclerView view) {

    }

    @Override
    protected void handleRefresh(SwipeRefreshLayout swipeCont) {

    }


}
