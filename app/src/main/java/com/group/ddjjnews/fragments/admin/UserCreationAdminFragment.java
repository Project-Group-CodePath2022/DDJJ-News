package com.group.ddjjnews.fragments.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.group.ddjjnews.databinding.FragmentUserCreationAdminBinding;
import com.group.ddjjnews.models.News;
import com.group.ddjjnews.models.User;
import com.parse.ParseRole;

import java.util.ArrayList;
import java.util.List;

public class UserCreationAdminFragment extends DialogFragment {
    public static final String KEY_USER = "user";
    FragmentUserCreationAdminBinding binding;
    User user;
    String role;
    private List<String> nameROles;

    public UserCreationAdminFragment() {}

    public static UserCreationAdminFragment newInstance(User user) {
        UserCreationAdminFragment fragment = new UserCreationAdminFragment();
        Bundle args = new Bundle();
        args.putParcelable(String.valueOf(user), user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         if (getArguments() != null) {
             user = getArguments().getParcelable(KEY_USER);
         }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserCreationAdminBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setSpinnerCategory();
        prefillIfUpdate();

        binding.btnCreateUser.setOnClickListener(view1 -> {
            if (!fieldsOk()) return;
            createNew();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void prefillIfUpdate() {
        if (user != null) {
            binding.edEmail.setText(user.getUsername());
            binding.btnCreateUser.setText("Update");
        }
    }

    private void createNew() {
        // TODO: create or update
        User.createAdmin(binding.edEmail.getText().toString(), binding.edPassword.getText().toString(), role, (object, e) -> {
            if (e == null) {
                dismiss();
                ((UserListAdminFragment)getParentFragment()).addItem(object);
                Toast.makeText(getContext(), "Saved!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean fieldsOk() {
        if (
                binding.edEmail.getText().toString().trim().isEmpty() ||
                        binding.edPassword.getText().toString().trim().isEmpty() || this.role == null
        ) {
            Toast.makeText(getContext(), "Empty field !", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void setSpinnerCategory() {
        ArrayAdapter<String> adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, nameROles);
        binding.roleSpinner.setAdapter(adapter);
        binding.roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                role = (String) adapterView.getItemAtPosition(i);
                Toast.makeText(getContext(), role, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    public void setNameROles(List<String> nameROles) {
        this.nameROles = nameROles;
    }
}