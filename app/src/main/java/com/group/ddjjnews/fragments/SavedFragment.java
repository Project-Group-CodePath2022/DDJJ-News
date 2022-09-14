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

import com.group.ddjjnews.R;
import com.group.ddjjnews.adapters.ViewPagerAdapter;
import com.group.ddjjnews.databinding.FragmentSavedBinding;

public class SavedFragment extends Fragment {
    FragmentSavedBinding binding;

    public SavedFragment() {}

    public static SavedFragment newInstance() {
        SavedFragment fragment = new SavedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSavedBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());

        viewPagerAdapter.add(NestedSavedFragment.newInstance(NestedSavedFragment.TYPE_NEWS), "News");
        viewPagerAdapter.add(NestedSavedFragment.newInstance(NestedSavedFragment.TYPE_BLOG), "Blog");
        binding.pager.setAdapter(viewPagerAdapter); // Set adapter
        binding.tab.setupWithViewPager(binding.pager);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_toolbar_menu, menu);
        menu.findItem(R.id.main_share).setVisible(false); // Hide search icon
        super.onCreateOptionsMenu(menu, inflater);
    }
}