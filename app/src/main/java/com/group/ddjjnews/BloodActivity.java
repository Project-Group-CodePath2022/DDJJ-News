package com.group.ddjjnews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.group.ddjjnews.adapters.BloodAdapter;
import com.group.ddjjnews.adapters.ViewPagerAdapter;
import com.group.ddjjnews.databinding.ActivityBloodBinding;
import com.group.ddjjnews.databinding.FragmentCreateBloodBinding;
import com.group.ddjjnews.databinding.FragmentLoginBinding;
import com.group.ddjjnews.fragments.ListBloodFragment;
import com.group.ddjjnews.fragments.LoginFragment;
import com.group.ddjjnews.fragments.MyListBloodFragment;
import com.group.ddjjnews.fragments.RefreshFloatingBaseFragment;
import com.group.ddjjnews.models.Blood;
import com.group.ddjjnews.models.User;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class BloodActivity extends AppCompatActivity {
    ActivityBloodBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(BloodActivity.this, R.layout.activity_blood);
        setSupportActionBar(binding.bloodToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.add(ListBloodFragment.newInstance(), "All");
        if (User.getCurrentUser() != null)
            viewPagerAdapter.add(MyListBloodFragment.newInstance(), "My request");
        binding.bloodPager.setAdapter(viewPagerAdapter); // Set adapter
        binding.bloodTab.setupWithViewPager(binding.bloodPager);

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

    // Creation
    public static class CreateBloodFragment extends DialogFragment {
        FragmentCreateBloodBinding binding;

        public CreateBloodFragment() {}

        public static CreateBloodFragment newInstance() {
            CreateBloodFragment f = new CreateBloodFragment();
            Bundle bundle = new Bundle();
            f.setArguments(bundle);
            return f;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            binding = FragmentCreateBloodBinding.inflate(inflater, container, false);
            return binding.getRoot();
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            binding.btnSendRequestBlood.setOnClickListener(view1 -> {
                if (!fieldsOk()) return;
                Toast.makeText(getContext(), "Save!", Toast.LENGTH_SHORT).show();
                dismiss();
            });
        }
        private boolean fieldsOk() {
            if (
                    binding.edGroup.getText().toString().trim().isEmpty() ||
                    binding.edForName.getText().toString().trim().isEmpty() ||
                            binding.edDesc.getText().toString().trim().isEmpty()
            ) {
                Toast.makeText(getContext(), "Empty field !", Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        }
    }

}