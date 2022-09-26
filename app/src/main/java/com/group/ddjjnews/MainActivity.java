package com.group.ddjjnews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import com.group.ddjjnews.databinding.ActivityMainBinding;
import com.group.ddjjnews.fragments.LoginFragment;
import com.group.ddjjnews.fragments.NewsFragment;
import com.group.ddjjnews.fragments.SavedFragment;
import com.group.ddjjnews.models.User;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.parse.ParseUser;
import com.parse.facebook.ParseFacebookUtils;

public class MainActivity extends AppCompatActivity {
    public ActivityMainBinding binding;
    ActionBarDrawerToggle drawerToggle;
    Fragment currentFragment;
    Fragment newsFragment;
    Fragment savedFragment;
    User user;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = (User) User.getCurrentUser();
        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);

        setSupportActionBar(binding.mainToolbar); // Set toolbar instead actionbar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

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
                case R.id.drawer_alert:
                    gotoAlert();
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
                    getSupportActionBar().setTitle("My news");
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
   protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
       super.onActivityResult(requestCode, resultCode, data);
   }



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
            User.logOut();
            restartActivity();
            return true;
        } else if (itemId == R.id.main_search) {
            gotoSearch();
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
            if (user.isAdmin()) {
                m.findItem(R.id.drawer_dashboard).setVisible(true);
            } else {
                m.findItem(R.id.drawer_dashboard).setVisible(false);
            }
        } else {
            m.findItem(R.id.drawer_dashboard).setVisible(false);
        }
    }

    public void gotoDashboard() {
        Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
        startActivity(intent);

    }

    public void gotoRequestBlood() {
        Intent intent = new Intent(MainActivity.this, BloodActivity.class);
        startActivity(intent);
    }

    public void gotoDetail(String type, ParseObject item) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("item", item);
        startActivity(intent);
    }

    public void gotoSearch() {
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        startActivity(intent);
    }

    public void gotoAlert() {
        Intent intent = new Intent(MainActivity.this, AlertActivity.class);
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