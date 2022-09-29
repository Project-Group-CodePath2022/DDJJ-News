package com.group.ddjjnews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.group.ddjjnews.adapters.ViewPagerAdapter;
import com.group.ddjjnews.databinding.ActivityBloodBinding;
import com.group.ddjjnews.databinding.FragmentCreateBloodBinding;
import com.group.ddjjnews.fragments.CreateBloodFragment;
import com.group.ddjjnews.fragments.ListBloodFragment;
import com.group.ddjjnews.fragments.MyListBloodFragment;

import com.group.ddjjnews.models.Blood;
import com.group.ddjjnews.models.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class BloodActivity extends AppCompatActivity {
    ActivityBloodBinding binding;
    ListBloodFragment list;
    MyListBloodFragment myList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(BloodActivity.this, R.layout.activity_blood);
        setSupportActionBar(binding.bloodToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        list = ListBloodFragment.newInstance();
        viewPagerAdapter.add(list, "All");
        if (User.getCurrentUser() != null) {
            myList = MyListBloodFragment.newInstance();
            viewPagerAdapter.add(myList, "My request");
        }
        binding.bloodPager.setAdapter(viewPagerAdapter); // Set adapter
        binding.bloodTab.setupWithViewPager(binding.bloodPager);

        if (User.getCurrentUser() == null)
            binding.floatingAction.setVisibility(FloatingActionButton.GONE);
        binding.floatingAction.setOnClickListener(view -> showCreationDialog());

    }

    @Nullable
    @Override
    public Intent getSupportParentActivityIntent() {
        finish();
        return null;
    }

    @Override
    protected void onDestroy() {
        binding = null;
        super.onDestroy();
    }

    private void showCreationDialog() {
        FragmentManager fm = getSupportFragmentManager();
        CreateBloodFragment cBloodFragment = CreateBloodFragment.newInstance();
        cBloodFragment.setListener(blood -> {
            if (myList != null) {
                myList.addOne(blood);
            }
            cBloodFragment.dismiss();
        });
        cBloodFragment.setCancelable(true);
        // cBloodFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
        cBloodFragment.show(fm, "blood_creation_fragment");
    }
}