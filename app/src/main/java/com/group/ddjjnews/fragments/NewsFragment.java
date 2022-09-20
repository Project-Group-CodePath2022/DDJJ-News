package com.group.ddjjnews.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.android.material.tabs.TabLayout;
import com.group.ddjjnews.R;
import com.group.ddjjnews.Utils.Prefs;
import com.group.ddjjnews.adapters.ViewPagerDynamicAdapter;
import com.group.ddjjnews.databinding.FragmentNewsBinding;
import com.group.ddjjnews.models.Category;
import com.group.ddjjnews.models.User;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NewsFragment extends Fragment {
    FragmentNewsBinding binding;
    ViewPagerDynamicAdapter adapter;

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

        // binding.newsPager.setOnTouchListener((view1, motionEvent) -> true); // stop scrolling viewPager
        binding.newsPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.newsTab));
         binding.newsPager.setOffscreenPageLimit(4);

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
        setTabsCategories();

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_toolbar_menu, menu);
        if (User.getCurrentUser() == null) { // no logged in user do not display logout item menu
            menu.findItem(R.id.main_logout).setVisible(false);
        }
        MenuItem searchItem = menu.findItem(R.id.main_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                // Search in the current category news posts
                NestedNewsFragment f = (NestedNewsFragment) ((ViewPagerDynamicAdapter)binding.newsPager.getAdapter()).getCurrent(binding.newsPager.getCurrentItem());
                f.searchByTitle(query, ok -> {
                    if (ok) {
                        searchView.setQuery("", false);
                    }
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void getCategoriesForNewsInPrefs() {
        // Default Tab (All)
        binding.newsTab.addTab(binding.newsTab.newTab().setText("All"));
        adapter.add(NestedNewsFragment.newInstance(null));
        // Retrieve last Categories
        Set<String> sc = Prefs.read(getContext()).getStringSet("stc", new HashSet<>());
        for (String category: sc) {
            binding.newsTab.addTab(binding.newsTab.newTab().setText(category));
            adapter.add(NestedNewsFragment.newInstance(category));
        }
        adapter.notifyDataSetChanged();
        binding.newsPager.setAdapter(adapter);
    }

    // Return categories News
    private void setTabsCategories() {
        HashMap<String, Integer> params = new HashMap<>();
        params.put("limit", 12);
        ParseCloud.callFunctionInBackground("list-category", params, (FunctionCallback<List<ParseObject>>) (objects, e) -> {
            if (e == null) {
                adapter.clear();
                binding.newsTab.removeAllTabs();
                // Default Tab (All)
                binding.newsTab.addTab(binding.newsTab.newTab().setText("All"));
                adapter.add(NestedNewsFragment.newInstance(null));
                HashSet<String> setCategory = new HashSet<>();
                for (int i = 0; i < objects.size(); i++) {
                    Category category = (Category) objects.get(i);
                    binding.newsTab.addTab(binding.newsTab.newTab().setText(category.getString("name")), i+1);
                    adapter.add(NestedNewsFragment.newInstance(category.getString("name")));
                    setCategory.add(category.getString("name"));
                }
                // Save in pref
                Prefs.save(getContext()).putStringSet("stc", setCategory).commit();
            }
            adapter.notifyDataSetChanged();
            binding.newsPager.setCurrentItem(0);
        });
    }
}