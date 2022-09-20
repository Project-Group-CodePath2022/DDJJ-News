package com.group.ddjjnews.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.group.ddjjnews.MainActivity;

import com.group.ddjjnews.SpaceItemDecoration;
import com.group.ddjjnews.Utils.EndlessRecyclerViewScrollListener;
import com.group.ddjjnews.adapters.NewsAdapter;
import com.group.ddjjnews.databinding.FragmentNestedNewsBinding;
import com.group.ddjjnews.models.News;
import com.parse.FunctionCallback;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class NestedNewsFragment extends Fragment {
    public static final String TAG = "NestedNewsFragment";
    private static final String ARG_PARAM1 = "category";
    private static final int DISPLAY_LIMIT = 21;
    FragmentNestedNewsBinding binding;

    List<ParseObject> newsPosts = new ArrayList<>();
    NewsAdapter adapter;
    GridLayoutManager layoutManager;
    int page = 0;

    private String mParam1;

    public interface CallOk {
        void done(boolean ok);
    }

    public NestedNewsFragment() {}

    public static NestedNewsFragment newInstance(String param1) {
        NestedNewsFragment fragment = new NestedNewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new NewsAdapter(getContext(), newsPosts);
        layoutManager = new GridLayoutManager(getContext(), 2);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNestedNewsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EndlessRecyclerViewScrollListener endless = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Toast.makeText(getContext(), ""+page, Toast.LENGTH_SHORT).show();
                HashMap<String, Object> params = new HashMap<>();
                if (mParam1 != null)
                    params.put(ARG_PARAM1, mParam1);
                params.put("limit", DISPLAY_LIMIT);
                params.put("skip", (page * DISPLAY_LIMIT));
                News.getNews(params, (objects, e) -> {
                    if(e != null) return;
                    newsPosts.addAll((Collection<? extends ParseObject>) objects);
                    adapter.notifyDataSetChanged();
                });
            }
        };

        binding.swipeContainer.setOnRefreshListener(() -> getNewsPosts(null, (objects, e) -> {
            if (e == null) {
                endless.resetState();
                page = 0;
                newsPosts.clear();
                newsPosts.addAll((Collection<? extends ParseObject>) objects);
                adapter.notifyDataSetChanged();
            }
            binding.swipeContainer.setRefreshing(false);
        }));

        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int type = adapter.getItemViewType(position);
                if (type == NewsAdapter.ALAUNE_TYPE)
                    return 2;
                return 1;
            }
        });
        binding.rcNews.addItemDecoration(new SpaceItemDecoration(21));
        binding.rcNews.setLayoutManager(layoutManager);
        binding.rcNews.setOnScrollListener(endless);
        binding.rcNews.setAdapter(adapter);

        adapter.setListener(item -> ((MainActivity)getContext()).gotoDetail("news", item));
        binding.swipeContainer.setRefreshing(true);
        getNewsPosts(null, (objects, e) -> {
            if (e == null) {
                newsPosts.clear();
                newsPosts.addAll((Collection<? extends ParseObject>) objects);
                adapter.notifyDataSetChanged();
            }
            binding.swipeContainer.setRefreshing(false);
        });
    }

    public void searchByTitle(String query, CallOk callOk ) {
        binding.swipeContainer.setRefreshing(true);
        getNewsPosts(query, (objects, e) -> {
            if (e == null) {
                newsPosts.clear();
                newsPosts.addAll((Collection<? extends ParseObject>) objects);
                adapter.notifyDataSetChanged();
                callOk.done(true);
            } else {
               callOk.done(false);
            }
            binding.swipeContainer.setRefreshing(false);
        });
    }

    private void getNewsPosts(String query, FunctionCallback<Object> callback) {
        HashMap<String, Object> params = new HashMap<>();
        if (mParam1 != null)
            params.put(ARG_PARAM1, mParam1);
        if (query != null)
            params.put("title", query);
        params.put("limit", DISPLAY_LIMIT);
        News.getNews(params, callback);
    }
}