package com.group.ddjjnews;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

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
        FragmentManager fragmentManager = getSupportFragmentManager();


        if (intent != null && intent.getStringExtra("type") != null) {
            String type = intent.getStringExtra("type");
            Fragment fr = NewsDetailFragment.newInstance(intent.getParcelableExtra("item"));
            fragmentManager.beginTransaction().add(R.id.detailFrame, fr).show(fr).commit();

            // getSupportFragmentManager().beginTransaction().replace(R.id.detailFrame, NewsDetailFragment.newInstance(intent.getParcelableExtra("item"))).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Nullable
    @Override
    public Intent getSupportParentActivityIntent() {
        finish();
        return null;
    }
}