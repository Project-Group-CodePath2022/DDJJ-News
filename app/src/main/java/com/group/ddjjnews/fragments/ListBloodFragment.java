package com.group.ddjjnews.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.group.ddjjnews.BloodActivity;
import com.group.ddjjnews.adapters.BloodAdapter;
import com.group.ddjjnews.databinding.FragmentRefreshBaseBinding;
import com.group.ddjjnews.databinding.FragmentRefreshFloatingBaseBinding;
import com.group.ddjjnews.models.Blood;
import com.group.ddjjnews.models.User;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class ListBloodFragment extends RefreshBaseFragment {
    List<ParseObject> requests = new ArrayList<>();
    protected FragmentRefreshBaseBinding binding;

    public static ListBloodFragment newInstance() {
        ListBloodFragment f = new ListBloodFragment();
        Bundle bundle = new Bundle();
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new BloodAdapter(getContext(), requests);
        layoutManager = new LinearLayoutManager(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentRefreshBaseBinding.inflate(inflater, container, false);
        this.rcItems = (RecyclerView) binding.rcView;
        this.sRefresh = binding.sRefresh;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getRequestedBlood();
    }

    @Override
    protected void loadMore(int page, int totalItemsCount, RecyclerView view) {

    }

    @Override
    protected void handleRefresh(SwipeRefreshLayout swipeCont) {
        getRequestedBlood();
    }

    protected void getRequestedBlood() {
        HashMap<String, Object> params = new HashMap<>();
        Blood.getAll(params, new Blood.Callback() {
            @Override
            public void done(Blood object, Exception e) {}
            @Override
            public void done(Collection objects, Exception e) {
                if (e == null) {
                    requests.clear();
                    requests.addAll(objects);
                    adapter.notifyDataSetChanged();
                    sRefresh.setRefreshing(false);
                }
            }
        });
    }
}
