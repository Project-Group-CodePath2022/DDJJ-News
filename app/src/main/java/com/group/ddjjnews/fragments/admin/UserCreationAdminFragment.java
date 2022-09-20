package com.group.ddjjnews.fragments.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import com.group.ddjjnews.R;

public class UserCreationAdminFragment extends DialogFragment {

    public UserCreationAdminFragment() {
        // Required empty public constructor
    }

    public static UserCreationAdminFragment newInstance() {
        UserCreationAdminFragment fragment = new UserCreationAdminFragment();
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
        return inflater.inflate(R.layout.fragment_user_creation_admin, container, false);
    }
}