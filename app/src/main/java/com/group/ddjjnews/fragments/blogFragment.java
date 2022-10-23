package com.group.ddjjnews.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.group.ddjjnews.R;
import com.group.ddjjnews.adapters.ViewPagerAdapter;
import com.group.ddjjnews.databinding.FragmentBlogBinding;
import com.group.ddjjnews.databinding.FragmentNewsBinding;

public class blogFragment extends Fragment {
    FragmentBlogBinding binding;
    public blogFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBlogBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getParentFragmentManager());
//        if (all == null)
//            all = new NestedNewsFragment();
//
//        viewPagerAdapter.add(all, "News");
//        viewPagerAdapter.add(new NewsFragment.CategoryListFragment(), "Categs");
//        binding.newsPager.setAdapter(viewPagerAdapter); // Set adapter
//        binding.newsTab.setupWithViewPager(binding.newsPager);
    }
}