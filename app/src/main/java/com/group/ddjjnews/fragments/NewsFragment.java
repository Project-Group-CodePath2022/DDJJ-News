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
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.group.ddjjnews.R;
import com.group.ddjjnews.adapters.ViewPagerDynamicAdapter;
import com.group.ddjjnews.databinding.FragmentNewsBinding;

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

        ViewPagerDynamicAdapter adapter = new ViewPagerDynamicAdapter(getChildFragmentManager());

        for (int i = 0; i < 7; i++) {
            binding.newsTab.addTab(binding.newsTab.newTab().setText("MyPage: " + i));
            adapter.add(NestedNewsFragment.newInstance("" + i));
        }

        binding.newsPager.setAdapter(adapter);
        binding.newsPager.setCurrentItem(0);
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
                Toast.makeText(getContext(), "From news :" + query, Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
}