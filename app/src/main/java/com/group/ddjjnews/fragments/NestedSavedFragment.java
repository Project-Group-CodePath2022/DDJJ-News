package com.group.ddjjnews.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.group.ddjjnews.databinding.FragmentNestedSavedBinding;

public class NestedSavedFragment extends Fragment {
    FragmentNestedSavedBinding binding;
    private static final String KEY_TYPE = "type";
    public static final String TYPE_NEWS = "news";
    public static final String TYPE_BLOG = "blog";


    public NestedSavedFragment() {}

    public static NestedSavedFragment newInstance(String type) {
        NestedSavedFragment fragment = new NestedSavedFragment();
        Bundle args = new Bundle();
        args.putString(KEY_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNestedSavedBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            if (TYPE_NEWS.equals(getArguments().getString(KEY_TYPE))) {
                getNewsFromDB();
            } else if (TYPE_BLOG.equals(getArguments().get(KEY_TYPE))) {
                getBlogFromDB();
            }
        }
    }

    private void getNewsFromDB() {  }

    private void getBlogFromDB() {  }
}