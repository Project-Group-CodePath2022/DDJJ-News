package com.group.ddjjnews;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.group.ddjjnews.adapters.ViewPagerDynamicAdapter;
import com.group.ddjjnews.databinding.ActivitySearchBinding;
import com.group.ddjjnews.fragments.NestedNewsFragment;

import java.util.Objects;

public class SearchActivity extends AppCompatActivity {
    ActivitySearchBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(SearchActivity.this, R.layout.activity_search);
        setSupportActionBar(binding.searchToolbar); // Set toolbar instead actionbar
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        androidx.appcompat.widget.SearchView searchView = binding.searchToolbar.findViewById(R.id.searchView);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.setQueryHint("Search for news");

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchView.setFocusable(true);
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Nullable
    @Override
    public Intent getSupportParentActivityIntent() {
        finish();
        return null;
    }
}