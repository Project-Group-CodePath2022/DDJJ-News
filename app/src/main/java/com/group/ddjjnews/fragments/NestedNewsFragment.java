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

import com.group.ddjjnews.Utils.SpaceItemDecoration;
import com.group.ddjjnews.Utils.EndlessRecyclerViewScrollListener;
import com.group.ddjjnews.adapters.NewsAdapter;
import com.group.ddjjnews.databinding.FragmentNestedNewsBinding;
import com.group.ddjjnews.models.News;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class NestedNewsFragment extends Fragment {
    FragmentNestedNewsBinding binding;
    List<ParseObject> newsPosts = new ArrayList<>();
    NewsAdapter adapter;
    GridLayoutManager layoutManager;
    EndlessRecyclerViewScrollListener endless;

    public NestedNewsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new NewsAdapter(getContext(), newsPosts);
        layoutManager = new GridLayoutManager(getContext(), 2);
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
        endless = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Toast.makeText(getContext(), "onLoadMore: " + page, Toast.LENGTH_SHORT).show();
            }
        };

        binding.swipeContainer.setOnRefreshListener(() -> getNewsPosts());

        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int type = adapter.getItemViewType(position);
                if (type == NewsAdapter.ALAUNE_TYPE || type == NewsAdapter.EMPTY)
                    return 2;
                return 1;
            }
        });

        binding.rcNews.addItemDecoration(new SpaceItemDecoration(28, true));
        binding.rcNews.setLayoutManager(layoutManager);
        binding.rcNews.setOnScrollListener(endless);
        binding.rcNews.setAdapter(adapter);
        adapter.setListener(item -> ((MainActivity)getContext()).gotoDetail("news", item));
        getNewsPosts();
    }

    public void addItem(News n) {
        newsPosts.add(0, n);
        adapter.notifyItemInserted(0);
    }

    public void removeItem(News n) {
        for (int i = 0; i < newsPosts.size(); i++) {
            if (newsPosts.get(i).getObjectId().equals(n.getObjectId())){
                newsPosts.remove(i);
                adapter.notifyItemRemoved(i);
                return;
            }
        }
    }

    private void getNewsPosts() {
        HashMap<String, Object> params = new HashMap<>();
        binding.swipeContainer.setRefreshing(true);
        News.getNews(params, (objects, e) -> {
            if (e == null) {
                newsPosts.clear();
                newsPosts.addAll((Collection<? extends ParseObject>) objects);
                adapter.notifyDataSetChanged();
                endless.resetState();
            }
            binding.swipeContainer.setRefreshing(false);
        });
    }
}