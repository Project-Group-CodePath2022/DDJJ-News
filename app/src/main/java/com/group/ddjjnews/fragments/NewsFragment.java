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
import com.group.ddjjnews.adapters.ViewPagerDynamicAdapter;
import com.group.ddjjnews.databinding.FragmentNewsBinding;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseObject;

import java.util.HashMap;
import java.util.List;

public class NewsFragment extends Fragment {
    FragmentNewsBinding binding;

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
        binding.newsPager.setOnTouchListener((view1, motionEvent) -> true); // stop scrolling viewPager
        binding.newsPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.newsTab));
//        binding.newsPager.setOffscreenPageLimit(0);
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

        ViewPagerDynamicAdapter adapter = new ViewPagerDynamicAdapter(getChildFragmentManager());
        binding.newsTab.addTab(binding.newsTab.newTab().setText("All"));
        adapter.add(NestedNewsFragment.newInstance(null));
        binding.newsPager.setAdapter(adapter);

        getCategories(12, (objects, e) -> {
            if (e == null) {
                for (int i = 0; i < objects.size(); i++) {
                    ParseObject category = objects.get(i);
                    binding.newsTab.addTab(binding.newsTab.newTab().setText(category.getString("name")));
                    adapter.add(NestedNewsFragment.newInstance(category.getString("name")));
                }
                // TODO: save in pref
            } else {
                // TODO: check pref to see
            }
            adapter.notifyDataSetChanged();
            binding.newsPager.setCurrentItem(0);
        });



    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_toolbar_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.main_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                // TODO: get query and search
                NestedNewsFragment f = (NestedNewsFragment) ((ViewPagerDynamicAdapter)binding.newsPager.getAdapter()).getCurrent(binding.newsPager.getCurrentItem());
                return f.searchByTitle(query);

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    // Return categories News
    private void getCategories(int limit, FunctionCallback<List<ParseObject>> callback) {
        HashMap<String, Integer> params = new HashMap<String, Integer>();
        params.put("limit", limit);
        ParseCloud.callFunctionInBackground("list-category", params, callback);
    }

}