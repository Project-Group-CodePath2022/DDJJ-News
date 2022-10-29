package com.group.ddjjnews.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.group.ddjjnews.DetailActivity;

import com.group.ddjjnews.R;
import com.group.ddjjnews.Utils.SpaceItemDecoration;
import com.group.ddjjnews.Utils.TimeFormatter;
import com.group.ddjjnews.adapters.GenericAdapter;
import com.group.ddjjnews.databinding.EmptyStateBinding;
import com.group.ddjjnews.databinding.FragmentNestedSavedBinding;

import com.group.ddjjnews.databinding.NewsSavedItemBinding;
import com.group.ddjjnews.models.Category;
import com.group.ddjjnews.models.News;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class NestedSavedFragment extends Fragment {
    FragmentNestedSavedBinding binding;
    private static final String KEY_TYPE = "type";
    public static final String TYPE_NEWS = "news";
    public static final String TYPE_BLOG = "blog";
    GenericAdapter<News, NewsSavedItemBinding, EmptyStateBinding> adapter;
    List<News> items = new ArrayList<>();
    LinearLayoutManager layoutManager;

    public NestedSavedFragment(){}

    public static NestedSavedFragment newInstance(String type) {
        NestedSavedFragment f = new NestedSavedFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TYPE, type);
        f.setArguments(bundle);
        return f;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.adapter = new GenericAdapter<News, NewsSavedItemBinding, EmptyStateBinding>(getContext(), items, NewsSavedItemBinding.class, EmptyStateBinding.class) {
            @Override
            public void bindItem(NewsSavedItemBinding binding, News item, int position) {
                if (item.getKeyImage() != null)
                    Glide.with(requireContext())
                            .load(item.getKeyImage().getUrl())
                            .transform(new RoundedCorners(16))
                            .into(binding.imgImage);
                binding.tvCategory.setText(((Category)item.getKeyCategory()).getKeyName());
                binding.tvTitle.setText(item.getKeyTitle());
                binding.tvDetail.setText(" | " + TimeFormatter.getTimeDifference(item.getCreatedAt().toString()));
                binding.tvTitle.setOnClickListener(view -> gotoDetail(item));
                binding.options.setOnClickListener(view ->showBSD(item, position));
            }

            @Override
            public void bindEmpty(EmptyStateBinding binding) {
                binding.title.setText("You got no saved news");
                binding.subTitle.setText("DDJJ");
            }
        };
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
        binding.rcNewsSaved.addItemDecoration(new SpaceItemDecoration(7, false));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getArguments() != null) {
            if (TYPE_NEWS.equals(getArguments().getString(KEY_TYPE))) {
                getNewsFromDB();
            }
        }
    }

    private void gotoDetail(News item) {
        Intent i = new Intent(getContext(), DetailActivity.class);
        i.putExtra("type", "news");
        i.putExtra("item", item);
        startActivity(i);
    }

    private void showBSD(News item, int pos) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(R.layout.bsd_saved);
        bottomSheetDialog.findViewById(R.id.delete).setOnClickListener(view -> item.unpinInBackground(e -> {
            if (e == null) {
                try {
                    items.remove(pos);
                    adapter.notifyItemRemoved(pos);
                    adapter.notifyItemRangeChanged(pos, 1);
                    bottomSheetDialog.dismiss();
                } catch (Exception ee){
                    Log.e("NestedSaved", ee.toString());
                }
            }
        }));
        bottomSheetDialog.show();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getNewsFromDB() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("News");
        query.include(News.KEY_CATEGORY);
        query.fromLocalDatastore();
        query.ignoreACLs();
        query.findInBackground((objects, e) -> {
            if (e == null) {
                binding.rcNewsSaved.post(() -> {
                    items.clear();
                    for (Object obj: objects) {
                        items.add((News) obj);
                    }
                    adapter.notifyDataSetChanged();
                });
            }
        });
    }
}