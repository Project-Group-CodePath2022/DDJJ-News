package com.group.ddjjnews.fragments.admin;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.group.ddjjnews.R;


public class NewsCreationAdminFragment extends DialogFragment {

    public NewsCreationAdminFragment() {
        // Required empty public constructor
    }

    public static NewsCreationAdminFragment newInstance() {
        NewsCreationAdminFragment fragment = new NewsCreationAdminFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news_creation_admin, container, false);
    }
}