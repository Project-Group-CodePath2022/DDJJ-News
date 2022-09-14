package com.group.ddjjnews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.group.ddjjnews.databinding.ActivityMainBinding;
import com.group.ddjjnews.fragments.NewsFragment;
import com.group.ddjjnews.fragments.SavedFragment;
import com.parse.ParseObject;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    ActionBarDrawerToggle drawerToggle;

    Fragment currentFragment;
    Fragment newsFragment;
    Fragment savedFragment;


    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);

        setSupportActionBar(binding.mainToolbar); // Set toolbar instead actionbar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        drawerToggle = new ActionBarDrawerToggle(this, binding.mainDrawer, R.string.nav_open, R.string.nav_close);
        binding.mainDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        binding.mainNavigation.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.drawer_dashboard) {
                gotoDashboard();
            }
            binding.mainDrawer.closeDrawer(GravityCompat.START);
            return true;
        });

        final FragmentManager fragmentManager = getSupportFragmentManager();

        // Feed news
        newsFragment = NewsFragment.newInstance();
        //  Feed, list saved blog and news post using tabs
        savedFragment = SavedFragment.newInstance();

        currentFragment = newsFragment;
        fragmentManager.beginTransaction().add(R.id.mainFrameLayout, savedFragment).hide(savedFragment).commit();
        fragmentManager.beginTransaction().add(R.id.mainFrameLayout, newsFragment).commit();

        binding.mainBottomNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_news:
                    fragmentManager.beginTransaction().hide(currentFragment).show(newsFragment).commit();
                    getSupportActionBar().setTitle("News");
                    currentFragment = newsFragment;
                    break;
                case R.id.bottom_saved:
                    fragmentManager.beginTransaction().hide(currentFragment).show(savedFragment).commit();
                    getSupportActionBar().setTitle("Saved posts");
                    currentFragment = savedFragment;
                    break;
                default:
                    break;
            }
            return true;
        });

        // By default display news
        binding.mainBottomNavigation.setSelectedItemId(R.id.bottom_news);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else if (itemId == R.id.main_logout) {
            Toast.makeText(this, "From Main", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void gotoDashboard() {
        Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }

    public void gotoDetail(String type, ParseObject item) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("item", item);
        startActivity(intent);
    }
}