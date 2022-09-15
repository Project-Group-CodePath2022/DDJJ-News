package com.group.ddjjnews;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.group.ddjjnews.databinding.ActivityDetailBinding;
import com.group.ddjjnews.fragments.NewsDetailFragment;

public class DetailActivity extends AppCompatActivity {
    ActivityDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(DetailActivity.this, R.layout.activity_detail);
        setSupportActionBar(binding.detailToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();

        if (intent != null && intent.getStringExtra("type") != null) {
            String type = intent.getStringExtra("type");
            getSupportFragmentManager().beginTransaction().replace(R.id.detailFrame, NewsDetailFragment.newInstance()).commit();
        }

    }
    @Nullable
    @Override
    public Intent getSupportParentActivityIntent() {
        finish();
        return null;
    }
}