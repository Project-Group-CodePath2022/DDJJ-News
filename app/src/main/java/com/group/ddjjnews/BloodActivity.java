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
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.group.ddjjnews.adapters.ViewPagerAdapter;
import com.group.ddjjnews.databinding.ActivityBloodBinding;
import com.group.ddjjnews.databinding.FragmentCreateBloodBinding;
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
        BloodActivity.CreateBloodFragment cBloodFragment = BloodActivity.CreateBloodFragment.newInstance();
        cBloodFragment.setListener(blood -> {
            if (myList != null) {
                myList.addOne(blood);
            }
        });
        cBloodFragment.setCancelable(true);
        // cBloodFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
        cBloodFragment.show(fm, "blood_creation_fragment");
    }

    // Creation
    public static class CreateBloodFragment extends DialogFragment {
        FragmentCreateBloodBinding binding;
        private onCreatedBloodListener listener;


        public CreateBloodFragment() {}

        public void setListener(onCreatedBloodListener listener) {
            this.listener = listener;
        }

        public interface onCreatedBloodListener {
            void onAdded(Blood blood);
        }

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
                HashMap<String, Object> params = new HashMap<>();
                params.put(Blood.KEY_GROUP_BLOOD, binding.edGroup.getText().toString());
                params.put(Blood.KEY_FOR_NAME, binding.edForName.getText().toString());
                params.put(Blood.KEY_DESCRIPTION, binding.edDesc.getText().toString());

                Blood.create(params, new Blood.Callback() {
                    @Override
                    public void done(Blood object, Exception e) {
                        if (e == null) {
                            listener.onAdded(object);
                        } else {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void done(Collection objects, Exception e) {

                    }
                });
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