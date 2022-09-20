package com.group.ddjjnews.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.group.ddjjnews.Utils.EndlessRecyclerViewScrollListener;
import com.group.ddjjnews.databinding.FragmentLoginBinding;


public abstract class RefreshBaseFragment extends Fragment {
    protected LinearLayoutManager layoutManager;
    protected RecyclerView.Adapter adapter;
    protected RecyclerView rcItems;
    protected SwipeRefreshLayout sRefresh;
    protected EndlessRecyclerViewScrollListener scrollListener;

    public RefreshBaseFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadMore(page, totalItemsCount, view);
            }
        };
        rcItems.addOnScrollListener(scrollListener);
        rcItems.setAdapter(adapter);
        rcItems.setLayoutManager(layoutManager);
        sRefresh.setOnRefreshListener(() -> handleRefresh(sRefresh));
    }

    protected abstract void loadMore(int page, int totalItemsCount, RecyclerView view);
    protected abstract void handleRefresh(SwipeRefreshLayout swipeCont);
}