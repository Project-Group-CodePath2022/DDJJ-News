package com.group.ddjjnews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.group.ddjjnews.databinding.ActivityDetailBinding;
import com.group.ddjjnews.fragments.NewsDetailFragment;
import com.group.ddjjnews.models.News;

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
            News n = intent.getParcelableExtra("item");
            setActionBarTitle(n.getKeyTitle());

            Fragment fr = NewsDetailFragment.newInstance(n);
            fragmentManager.beginTransaction().add(R.id.detailFrame, fr).show(fr).commit();

            // getSupportFragmentManager().beginTransaction().replace(R.id.detailFrame, NewsDetailFragment.newInstance(intent.getParcelableExtra("item"))).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public Intent getSupportParentActivityIntent() {
        finish();
        return null;
    }

    public void setActionBarTitle(String newTitle) {
        getSupportActionBar().setTitle(newTitle);
    }
}