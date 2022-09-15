package com.group.ddjjnews.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.group.ddjjnews.MainActivity;

import com.group.ddjjnews.adapters.NewsAdapter;
import com.group.ddjjnews.databinding.FragmentNestedNewsBinding;
import com.group.ddjjnews.models.News;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class NestedNewsFragment extends Fragment {
    public static final String TAG = "NestedNewsFragment";
    private static final String ARG_PARAM1 = "category";
    FragmentNestedNewsBinding binding;

    List<ParseObject> newsPosts = new ArrayList<>();
    NewsAdapter adapter;
    GridLayoutManager layoutManager;

    private String mParam1;

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
            Toast.makeText(getContext(), "Create " + mParam1, Toast.LENGTH_SHORT).show();
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

        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int type = adapter.getItemViewType(position);
                if (type == NewsAdapter.ALAUNE_TYPE)
                    return 2;
                return 1;
            }
        });
        ((RecyclerView)binding.rcNews).setLayoutManager(layoutManager);
        ((RecyclerView)binding.rcNews).setAdapter(adapter);

        adapter.setListener((NewsAdapter.NewsAdapterListener) item -> ((MainActivity)getContext()).gotoDetail("news", item));
        getNewsPosts();
    }

    private void getNewsPosts() {
        // TODO: get 21 latest posts
        HashMap<String, String> params = new HashMap<>();
        News.getNews(params, (objects, e) -> {
            if (e == null) {
                newsPosts.addAll((Collection<? extends ParseObject>) objects);
                adapter.notifyDataSetChanged();
            } else {
                return;
            }
        });
    }
}