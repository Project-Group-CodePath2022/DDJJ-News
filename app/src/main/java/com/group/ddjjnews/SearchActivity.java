package com.group.ddjjnews;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.group.ddjjnews.adapters.GenericAdapter;
import com.group.ddjjnews.databinding.ActivitySearchBinding;
import com.group.ddjjnews.databinding.EmptyStateBinding;
import com.group.ddjjnews.databinding.NewsItemDetailBinding;

import com.group.ddjjnews.models.News;

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

                Glide.with(SearchActivity.this)
                        .load(item.getKeyImage().getUrl())
                        .transform(new RoundedCorners(16))
                        .into(binding.imgImage);
                binding.tvTitle.setText(item.getKeyTitle());
            }

            @Override
            public void bindEmpty(EmptyStateBinding binding) {
                binding.title.setText("can search by title");
            }
        };

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rcView.setAdapter(adapter);
        binding.rcView.setLayoutManager(layoutManager);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
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
        SearchView search = (SearchView) menu.findItem(R.id.search_view).getActionView();

        search.setIconifiedByDefault(false);
        search.setIconified(false);

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
                return false;
            }
        });
        return true;
    }


    @SuppressLint("NotifyDataSetChanged")
    private void search(String byTitle) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("title", byTitle);
        News.getNews(params, (objects, e) -> {
            if (e == null) {
                items.clear();
                items.addAll((Collection<? extends News>) objects);
                adapter.notifyDataSetChanged();
            }
        });
    }
}