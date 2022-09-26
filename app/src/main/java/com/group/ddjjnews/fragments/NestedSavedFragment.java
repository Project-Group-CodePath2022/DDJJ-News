package com.group.ddjjnews.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.group.ddjjnews.R;
import com.group.ddjjnews.adapters.SavedAdapter;
import com.group.ddjjnews.databinding.FragmentNestedSavedBinding;
import com.group.ddjjnews.models.Blood;
import com.group.ddjjnews.models.News;
import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NestedSavedFragment extends Fragment {
    FragmentNestedSavedBinding binding;
    private static final String KEY_TYPE = "type";
    public static final String TYPE_NEWS = "news";
    public static final String TYPE_BLOG = "blog";
    List<ParseObject> items = new ArrayList<>();
    SavedAdapter adapter;
    LinearLayoutManager layoutManager;


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
        this.adapter = new SavedAdapter(getContext(), items);
        this.layoutManager = new LinearLayoutManager(getContext());
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
        binding.rcNewsSaved.setLayoutManager(layoutManager);
        binding.rcNewsSaved.setAdapter(adapter);
        binding.rcNewsSaved.setHasFixedSize(true);
        adapter.setListener((item, pos) -> {
            showBSD(item, pos);
        });

        if (getArguments() != null) {
            if (TYPE_NEWS.equals(getArguments().getString(KEY_TYPE))) {
                getNewsFromDB();
            } else if (TYPE_BLOG.equals(getArguments().get(KEY_TYPE))) {
                getBlogFromDB();
            }
        }
    }

    private void showBSD(News item, int pos) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(R.layout.bsd_saved);
        bottomSheetDialog.findViewById(R.id.delete).setOnClickListener(view -> {
            item.unpinInBackground(new DeleteCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        items.remove(pos);
                        adapter.notifyItemRemoved(pos);
                        bottomSheetDialog.dismiss();
                    }
                }
            });

        });
        bottomSheetDialog.show();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getNewsFromDB() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("News");
        query.fromLocalDatastore();
        query.ignoreACLs();
        query.findInBackground((objects, e) -> {
            if (e == null) {
                binding.rcNewsSaved.post(() -> {
                    items.addAll(objects);
                    adapter.notifyDataSetChanged();
                });

            } else {
                Log.d("score", "Error: " + e.getMessage());
            }
        });
    }

    private void getBlogFromDB() {  }
}