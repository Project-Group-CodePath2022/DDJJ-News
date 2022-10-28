package com.group.ddjjnews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.login.LoginManager;
import com.group.ddjjnews.databinding.ActivityMainBinding;
import com.group.ddjjnews.fragments.LoginFragment;
import com.group.ddjjnews.fragments.NewsFragment;
import com.group.ddjjnews.fragments.SavedFragment;
import com.group.ddjjnews.models.User;
import com.parse.ParseObject;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    public ActivityMainBinding binding;
    ActionBarDrawerToggle drawerToggle;
    Fragment currentFragment;
    Fragment newsFragment;
    Fragment savedFragment;
    User user;
    int lastID;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = (User) User.getCurrentUser();
        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);

        setSupportActionBar(binding.mainAppBar.findViewById(R.id.toolbar)); // Set toolbar instead actionbar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        // getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        drawerToggle = new ActionBarDrawerToggle(this, binding.mainDrawer, R.string.nav_open, R.string.nav_close);
        binding.mainDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        binding.mainNavigation.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.drawer_dashboard:
                    gotoDashboard();
                    break;
                case R.id.drawer_login:
                    showLoginDialog();
                    break;
                case R.id.drawer_request:
                    gotoRequestBlood();
                    break;
                default:
                    break;
            }
            binding.mainDrawer.closeDrawer(GravityCompat.START);
            return true;
        });

        hideShowDrawerItem();
        final FragmentManager fragmentManager = getSupportFragmentManager();
        // Feed news
        newsFragment = new NewsFragment();
        //  Feed, list saved blog and news post using tabs
        savedFragment = new SavedFragment();
        currentFragment = newsFragment; // Current fragment

        fragmentManager.beginTransaction().add(R.id.mainFrameLayout, savedFragment).hide(savedFragment).commit();
        fragmentManager.beginTransaction().add(R.id.mainFrameLayout, newsFragment).commit();

        lastID = R.id.bottom_news;

        binding.mainBottomNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_news:
                    fragmentManager.beginTransaction().hide(currentFragment).show(newsFragment).commit();
                    getSupportActionBar().setTitle("Home");
                    currentFragment = newsFragment;
                    lastID = item.getItemId();
                    break;
                case R.id.bottom_saved:
                    fragmentManager.beginTransaction().hide(currentFragment).show(savedFragment).commit();
                    getSupportActionBar().setTitle("Saved posts");
                    currentFragment = savedFragment;
                    lastID = item.getItemId();
                    break;
                case R.id.bottom_settings:
                    startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                    return true;
                case R.id.bottom_search:
                    binding.mainBottomNavigation.setSelected(false);
                    startActivity(new Intent(MainActivity.this, SearchActivity.class));
                    return true;
                default:
                    break;
            }
            return true;
        });
        // By default display news
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.mainBottomNavigation.setSelectedItemId(lastID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) { super.onActivityResult(requestCode, resultCode, data); }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else if (itemId == R.id.main_logout) {
            LoginManager.getInstance().logOut();
            User.logOut();
            restartActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void restartActivity(){
        finish();
        startActivity(getIntent());
    }

    private void hideShowDrawerItem() {
        Menu m = binding.mainNavigation.getMenu();
        if (user != null) {
            m.findItem(R.id.drawer_login).setVisible(false);
            m.findItem(R.id.drawer_dashboard).setVisible(user.isAdmin());
        } else {
            m.findItem(R.id.drawer_dashboard).setVisible(false);
        }
    }

    public void gotoDashboard() {
        startActivity(new Intent(MainActivity.this, DashboardActivity.class));
    }

    public void gotoRequestBlood() {
        startActivity(new Intent(MainActivity.this, BloodActivity.class));
    }

    public void gotoDetail(String type, ParseObject item) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("item", item);
        startActivity(intent);
    }

    private void showLoginDialog() {
        FragmentManager fm = getSupportFragmentManager();
        LoginFragment loginFragment = LoginFragment.newInstance();
        loginFragment.setCancelable(true);
        loginFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
        loginFragment.show(fm, "authentication_fragment");
    }
}