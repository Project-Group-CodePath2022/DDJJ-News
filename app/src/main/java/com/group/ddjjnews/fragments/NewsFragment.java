package com.group.ddjjnews.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.android.material.tabs.TabLayout;
import com.group.ddjjnews.R;
import com.group.ddjjnews.SpaceItemDecoration;
import com.group.ddjjnews.Utils.TimeFormatter;
import com.group.ddjjnews.adapters.GenericAdapter;
import com.group.ddjjnews.adapters.ViewPagerAdapter;
import com.group.ddjjnews.adapters.ViewPagerDynamicAdapter;
import com.group.ddjjnews.databinding.DashItemBinding;
import com.group.ddjjnews.databinding.FragmentNewsBinding;
import com.group.ddjjnews.databinding.FragmentRefreshBaseBinding;
import com.group.ddjjnews.databinding.NewsSavedItemBinding;
import com.group.ddjjnews.models.Category;
import com.group.ddjjnews.models.News;
import com.group.ddjjnews.models.User;
import com.parse.FunctionCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NewsFragment extends Fragment {
    FragmentNewsBinding binding;
    ViewPagerDynamicAdapter adapter;
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
        all = NestedNewsFragment.newInstance(null);

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

        subscriptionHandling.handleEvent(SubscriptionHandling.Event.LEAVE, (query, object) -> {
            getActivity().runOnUiThread(() -> all.removeItem(object));
        });

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
        FragmentRefreshBaseBinding binding;

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
            this.adapter = new GenericAdapter<Category, DashItemBinding>(getContext(), items, DashItemBinding.class) {
                @Override
                public void bindItem(DashItemBinding binding, Category item, int position) {
                    binding.title.setText(item.getKeyName());
                }
            };
            this.layoutManager = new GridLayoutManager(getContext(), 2);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            binding.rcView.addItemDecoration(new SpaceItemDecoration(18));
            displayCats();
        }

        @Override
        protected void handleRefresh(SwipeRefreshLayout swipeCont) {
            displayCats();
        }

        private void displayCats() {
            Category.getAll(new HashMap<>(), (object, e) -> {
                sRefresh.setRefreshing(true);
                if (e == null) {
                    items.clear();
                    items.addAll((Collection<? extends Category>) object);
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d("news", e.toString(), e);
                }
                sRefresh.setRefreshing(false);
            });
        }
    }
}