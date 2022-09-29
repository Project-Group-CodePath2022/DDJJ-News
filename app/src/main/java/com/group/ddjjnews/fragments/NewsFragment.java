package com.group.ddjjnews.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.group.ddjjnews.R;
import com.group.ddjjnews.adapters.ViewPagerDynamicAdapter;
import com.group.ddjjnews.databinding.FragmentNewsBinding;
import com.group.ddjjnews.models.News;
import com.group.ddjjnews.models.User;
import com.parse.ParseQuery;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
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
        adapter = new ViewPagerDynamicAdapter(getChildFragmentManager());
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
        binding.newsPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.newsTab));
        binding.newsPager.setOffscreenPageLimit(4);
        all = NestedNewsFragment.newInstance(null);

        binding.newsTab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // setCurrentItem as the tab position
                binding.newsPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        getCategoriesForNewsInPrefs();

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

        // Listen for CREATE events on the Message class
        subscriptionHandling.handleEvent(SubscriptionHandling.Event.LEAVE, (query, object) -> {
            // RecyclerView updates need to be run on the UI thread
            getActivity().runOnUiThread(() -> all.removeItem(object));
        });

        subscriptionHandling.handleEvent(SubscriptionHandling.Event.ENTER, (query, object) -> {
            // RecyclerView updates need to be run on the UI thread
            getActivity().runOnUiThread(() -> all.addItem(object));
        });

    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_toolbar_menu, menu);
        if (User.getCurrentUser() == null) { // no logged in user do not display logout item menu
            menu.findItem(R.id.main_logout).setVisible(false);
        }

    }

    private void getCategoriesForNewsInPrefs() {
        // Default Tab (All)
        binding.newsTab.addTab(binding.newsTab.newTab().setText("All"));
        adapter.add(all);
        Set<String> sc = new HashSet<>();
        sc.add("culture");
        sc.add("sports");
        sc.add("inter");
        sc.add("politics");
        sc.add("education");

        for (String category: sc) {
            binding.newsTab.addTab(binding.newsTab.newTab().setText(category));
            adapter.add(NestedNewsFragment.newInstance(category));
        }
        adapter.notifyDataSetChanged();
        binding.newsPager.setAdapter(adapter);
    }
}