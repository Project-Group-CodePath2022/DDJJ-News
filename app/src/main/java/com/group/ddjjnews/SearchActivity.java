package com.group.ddjjnews;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.group.ddjjnews.adapters.GenericAdapter;
import com.group.ddjjnews.databinding.ActivitySearchBinding;
import com.group.ddjjnews.databinding.EmptyStateBinding;
import com.group.ddjjnews.databinding.NewsItemDetailBinding;

import com.group.ddjjnews.models.News;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class SearchActivity extends AppCompatActivity {
    ActivitySearchBinding binding;
    List<News> items = new ArrayList<>();
    GenericAdapter<News, NewsItemDetailBinding, EmptyStateBinding> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(SearchActivity.this, R.layout.activity_search);
        setSupportActionBar(binding.searchAppBar.findViewById(R.id.toolbar)); // Set toolbar instead actionbar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        adapter = new GenericAdapter<News, NewsItemDetailBinding, EmptyStateBinding>(this, items, NewsItemDetailBinding.class, EmptyStateBinding.class) {
            @Override
            public void bindItem(NewsItemDetailBinding binding, News item, int position) {
                super.bindItem(binding, item, position);
                binding.tvTitle.setText(item.getKeyTitle());
                binding.tvTitle.setOnClickListener(view -> gotoDetail("news", item));
            }

            @Override
            public void bindEmpty(EmptyStateBinding binding) {
                binding.title.setText("Search by title");
                binding.subTitle.setText("DDJJ");
            }
        };

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rcView.setAdapter(adapter);
        binding.rcView.setLayoutManager(layoutManager);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    public void gotoDetail(String type, ParseObject item) {
        Intent intent = new Intent(SearchActivity.this, DetailActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("item", item);
        startActivity(intent);
    }

    @Nullable
    @Override
    public Intent getSupportParentActivityIntent() {
        finish();
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem s = menu.findItem(R.id.search_view);
        s.expandActionView();
        SearchView search = (SearchView) s.getActionView();
        search.setIconifiedByDefault(false);
        search.clearFocus();
        search.setQueryHint("Search");
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() >= 3) {
                    search(newText);
                    return true;
                }
                binding.progress.setVisibility(View.GONE);
                items.clear();
                adapter.notifyDataSetChanged();
                return false;
            }
        });
        return true;
    }


    @SuppressLint("NotifyDataSetChanged")
    private void search(String byTitle) {
        Log.d("Search", "calling...");
        binding.progress.setVisibility(View.VISIBLE);
        HashMap<String, Object> params = new HashMap<>();
        params.put("title", byTitle);
        News.getNews(params, (objects, e) -> {
            if (e == null) {
                items.clear();
                items.addAll((Collection<? extends News>) objects);
                adapter.notifyDataSetChanged();
            }
            binding.progress.setVisibility(View.GONE);
        });
    }
}