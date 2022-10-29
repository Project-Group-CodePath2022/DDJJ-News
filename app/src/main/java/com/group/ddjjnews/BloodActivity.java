package com.group.ddjjnews;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import com.group.ddjjnews.databinding.ActivityBloodBinding;
import com.group.ddjjnews.fragments.CreateBloodFragment;
import com.group.ddjjnews.fragments.ListBloodFragment;
import com.group.ddjjnews.fragments.MyListBloodFragment;

import java.util.Objects;

public class BloodActivity extends AppCompatActivity {
    ActivityBloodBinding binding;
    ListBloodFragment list;
    MyListBloodFragment myList;
    Fragment current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(BloodActivity.this, R.layout.activity_blood);
        setSupportActionBar(binding.bloodAppBar.findViewById(R.id.toolbar));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        final FragmentManager fragmentManager = getSupportFragmentManager();

        if (list == null)
            list = ListBloodFragment.newInstance();

        if (myList == null)
            myList = MyListBloodFragment.newInstance();
        current = list;

        fragmentManager.beginTransaction().add(R.id.bloodFrameLayout, myList).hide(myList).commit();
        fragmentManager.beginTransaction().add(R.id.bloodFrameLayout, list).commit();

        binding.bloodBottomNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_news:
                    fragmentManager.beginTransaction().hide(current).show(list).commit();
                    getSupportActionBar().setTitle("Requests");
                    current = list;
                    break;
                case R.id.bottom_saved:
                    fragmentManager.beginTransaction().hide(current).show(myList).commit();
                    getSupportActionBar().setTitle("History");
                    current = myList;
                    break;
                default:
                    break;
            }
            return true;
        });
        // By default display news
        binding.bloodBottomNavigation.setSelectedItemId(R.id.bottom_news);
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