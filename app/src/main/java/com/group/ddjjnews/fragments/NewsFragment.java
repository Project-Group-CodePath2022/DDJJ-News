package com.group.ddjjnews.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.group.ddjjnews.R;

import com.group.ddjjnews.Utils.SpaceItemDecoration;
import com.group.ddjjnews.adapters.GenericAdapter;
import com.group.ddjjnews.adapters.NewsAdapter;
import com.group.ddjjnews.adapters.ViewPagerAdapter;

import com.group.ddjjnews.databinding.DashItemBinding;
import com.group.ddjjnews.databinding.EmptyStateBinding;
import com.group.ddjjnews.databinding.FragmentNewsBinding;

import com.group.ddjjnews.databinding.FragmentRefreshBaseBinding;
import com.group.ddjjnews.models.Category;
import com.group.ddjjnews.models.News;
import com.group.ddjjnews.models.User;
import com.parse.ParseQuery;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class NewsFragment extends Fragment {
    FragmentNewsBinding binding;
    ParseLiveQueryClient parseLiveQueryClient = null;
    ParseQuery<News> parseQuery;
    String websocketUrl = "wss://ddjjnews.b4a.io";
    NestedNewsFragment all;
    public NewsFragment() {}

    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getParentFragmentManager());
        if (all == null)
            all = new NestedNewsFragment();

        viewPagerAdapter.add(all, "News");
        viewPagerAdapter.add(new CategoryListFragment(), "Categs");
        binding.newsPager.setAdapter(viewPagerAdapter); // Set adapter
        binding.newsTab.setupWithViewPager(binding.newsPager);

        try {
            parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient(new URI(websocketUrl));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        parseQuery = ParseQuery.getQuery(News.class);
        parseQuery.include(News.KEY_CATEGORY);
        parseQuery.whereEqualTo(News.KEY_ACTIVE, true);
        // Connect to Parse server
        SubscriptionHandling<News> subscriptionHandling = parseLiveQueryClient.subscribe(parseQuery);

        subscriptionHandling.handleEvent(SubscriptionHandling.Event.LEAVE, (query, object) -> getActivity().runOnUiThread(() -> all.removeItem(object)));

        subscriptionHandling.handleEvent(SubscriptionHandling.Event.ENTER, (query, object) -> {
            // RecyclerView updates need to be run on the UI thread
            getActivity().runOnUiThread(() -> {
                all.addItem(object);
                Toast.makeText(getContext(), object.getKeyTitle(), Toast.LENGTH_SHORT).show();
            });
        });

    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_toolbar_menu, menu);
        if (User.getCurrentUser() == null) { // no logged in user do not display logout item menu
            menu.findItem(R.id.main_logout).setVisible(false);
        }
    }

    public static class CategoryListFragment extends RefreshBaseFragment {
        protected FragmentRefreshBaseBinding binding;
        List<Category> items = new ArrayList<>();

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

            ((GridLayoutManager)layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = adapter.getItemViewType(position);
                    if (type == GenericAdapter.EMPTY)
                        return 2;
                    return 1;
                }
            });

            this.adapter = new GenericAdapter<Category, DashItemBinding, EmptyStateBinding>(getContext(), items, DashItemBinding.class, EmptyStateBinding.class) {
                @Override
                public void bindItem(DashItemBinding binding, Category item, int position) {
                    binding.title.setText(item.getKeyName());
                }

                @Override
                public void bindEmpty(EmptyStateBinding binding) {
                    binding.title.setText("No category");
                    binding.subTitle.setText("Try refreshing");
                }
            };
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

        public void displayCats() {
            sRefresh.setRefreshing(true);
            Category.getAll(new HashMap<>(), (object, e) -> {
                if (e == null) {
                    items.clear();
                    for (Object o: object) {
                        items.add((Category) o);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d("news", e.toString(), e);
                }
                sRefresh.setRefreshing(false);
            });
        }
    }
}