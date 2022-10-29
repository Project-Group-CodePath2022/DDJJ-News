package com.group.ddjjnews.fragments;

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

import com.group.ddjjnews.databinding.FragmentCreateBloodBinding;
import com.group.ddjjnews.models.Blood;

import java.util.Collection;
import java.util.HashMap;

// Creation
public  class CreateBloodFragment extends DialogFragment {
    FragmentCreateBloodBinding binding;
    private onCreatedBloodListener listener;
    String typeBlood;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateBloodBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String[] listTypeBloods = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, listTypeBloods);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.edGroup.setAdapter(adapter);

        binding.edGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                typeBlood = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        binding.btnSendRequestBlood.setOnClickListener(view1 -> {
            if (!fieldsOk()) return;
            HashMap<String, Object> params = new HashMap<>();
            params.put(Blood.KEY_GROUP_BLOOD, typeBlood);
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
                public void done(Collection objects, Exception e) {}
            });
        });
    }

    private boolean fieldsOk() {
        if ( typeBlood == null ||
            binding.edForName.getText().toString().trim().isEmpty() ||
            binding.edDesc.getText().toString().trim().isEmpty()
        ) {
            Toast.makeText(getContext(), "Empty field !", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}