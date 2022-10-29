package com.group.ddjjnews;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;


import com.google.android.material.tabs.TabLayout;
import com.group.ddjjnews.adapters.ViewPagerDynamicAdapter;
import com.group.ddjjnews.databinding.ActivityDashboardBinding;
import com.group.ddjjnews.fragments.admin.NewsListAdminFragment;
import com.group.ddjjnews.fragments.admin.UserListAdminFragment;
import com.group.ddjjnews.models.User;

import java.util.Objects;


public class DashboardActivity extends AppCompatActivity {
    ActivityDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(DashboardActivity.this, R.layout.activity_dashboard);
        setSupportActionBar(binding.dashToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        User user = (User) User.getCurrentUser();
        getSupportActionBar().setSubtitle(user.getUsername());

        ViewPagerDynamicAdapter adapter = new ViewPagerDynamicAdapter(getSupportFragmentManager());
        binding.dashPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.dashTab));
        binding.dashTab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.dashPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        String[] myModels = {"News", "Users"};
        adapter.add(new NewsListAdminFragment());
        adapter.add(new UserListAdminFragment());

        for (String myModel : myModels) {
            binding.dashTab.addTab(binding.dashTab.newTab().setText(myModel));
        }
        binding.dashPager.setAdapter(adapter); // Set adapter
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Nullable
    @Override
    public Intent getSupportParentActivityIntent() {
        finish();
        return null;
    }
}