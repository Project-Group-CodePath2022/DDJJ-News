package com.group.ddjjnews.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.group.ddjjnews.MainActivity;

import com.group.ddjjnews.adapters.NewsAdapter;
import com.group.ddjjnews.databinding.FragmentNestedNewsBinding;
import com.group.ddjjnews.models.News;
import com.parse.FunctionCallback;
import com.parse.ParseException;
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

    public interface CallOk {
        boolean done(boolean ok);
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
        binding.rcNews.setLayoutManager(layoutManager);
        binding.rcNews.setAdapter(adapter);

        adapter.setListener(item -> ((MainActivity)getContext()).gotoDetail("news", item));
        getNewsPosts(null, (objects, e) -> {
            if (e != null) return;
            newsPosts.clear();
            newsPosts.addAll((Collection<? extends ParseObject>) objects);
            adapter.notifyDataSetChanged();
        });
    }


    public boolean searchByTitle(String query ) {
        final boolean[] res = {false};
        getNewsPosts(query, (objects, e) -> {
            if (e == null) {
                newsPosts.clear();
                newsPosts.addAll((Collection<? extends ParseObject>) objects);
                adapter.notifyDataSetChanged();
                res[0] = true;
            } else {
                res[0] = false;
            }
        });
        return res[0];
    }

    private void getNewsPosts(String query, FunctionCallback<Object> callback) {
        HashMap<String, Object> params = new HashMap<>();
        if (mParam1 != null)
            params.put(ARG_PARAM1, mParam1);
        if (query != null)
            params.put("title", query);
        News.getNews(params, callback);
    }
}