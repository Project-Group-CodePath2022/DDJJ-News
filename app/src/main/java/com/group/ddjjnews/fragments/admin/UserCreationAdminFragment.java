package com.group.ddjjnews.fragments.admin;

import android.content.Context;
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
import com.group.ddjjnews.models.User;
import com.parse.ParseDecoder;
import com.parse.ParseObject;
import com.parse.ParseRole;

import org.json.JSONObject;
import java.util.HashMap;
import java.util.List;

public class UserCreationAdminFragment extends DialogFragment {
    public static final String KEY_USER = "user";
    public static final String KEY_POS = "pos";
    FragmentUserCreationAdminBinding binding;
    User user;
    int position;
    String role;
    ArrayAdapter<String> adapter;
    private List<String> nameROles;
    Context context;

    public UserCreationAdminFragment() {}

    public static UserCreationAdminFragment newInstance(User user, int pos) {
        UserCreationAdminFragment fragment = new UserCreationAdminFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_POS, pos);
        args.putParcelable(KEY_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         if (getArguments() != null) {
             this.user = getArguments().getParcelable(KEY_USER);
             this.position = getArguments().getInt(KEY_POS);
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
            if (user != null)
                update();
            else
                createNew();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void prefillIfUpdate() {
        if (this.user != null) {
            binding.edEmail.setText(user.getUsername());
            binding.btnCreateUser.setText("Update");
            if (user.get("role") != null) {
                String r = (((ParseRole)ParseObject.fromJSON(new JSONObject((HashMap) user.get("role")), "_Role", ParseDecoder.get())).getName());
                binding.roleSpinner.setSelection(adapter.getPosition(r));
            }
        }
    }

    private void update() {
        User.updateUser(user.getKeyActive(), binding.edEmail.getText().toString(), null, role, user.getObjectId(), (object, e) -> {
            if (e == null) {
                ((UserListAdminFragment)getParentFragment()).updateItem(object, position);
                this.dismiss();
            } else {
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createNew() {
        User.createAdmin(binding.edEmail.getText().toString(), binding.edPassword.getText().toString(), role, null, (object, e) -> {
            if (e == null) {
                ((UserListAdminFragment)getParentFragment()).addItem(object);
                this.dismiss();
            } else {
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean fieldsOk() {
        if ( binding.edEmail.getText().toString().trim().isEmpty() || this.role == null ) {
            Toast.makeText(getContext(), "Required fields!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void setSpinnerCategory() {
        // Setup roles for spinner
        adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, nameROles);
        binding.roleSpinner.setAdapter(adapter);
        binding.roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                role = (String) adapterView.getItemAtPosition(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    public void setNameROles(List<String> nameROles) {
        this.nameROles = nameROles;
    }
}