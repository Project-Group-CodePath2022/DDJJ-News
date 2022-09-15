package com.group.ddjjnews.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.group.ddjjnews.databinding.FragmentLoginBinding;


public class LoginFragment extends DialogFragment {
    public static final String TAG = "LoginFragment";
    FragmentLoginBinding binding;
    private Listener listener;

    public interface Listener {
//        void onLoginClick(View v, String username, String password, IndeterminateDialog dialog);
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnLogin.setOnClickListener(v -> {
            if (!fieldsOk()) return;
            // TODO: Login with credentials
//            IndeterminateDialog dl = IndeterminateDialog.newInstance("Logging In", "Whip pip pip!!");
//            dl.setCancelable(false);
//            dl.show(getActivity().getSupportFragmentManager(), "login");
//            listener.onLoginClick(btnLogin, edUsername.getText().toString(), edPassword.getText().toString(), dl);
        });

        binding.btnJoinUs.setOnClickListener(v -> {
            if (!fieldsOk()) return;
            // TODO: Sign with credentials and store the sessionToken on device
//            IndeterminateDialog dl = IndeterminateDialog.newInstance("Logging In", "Whip pip pip!!");
//            dl.setCancelable(false);
//            dl.show(getActivity().getSupportFragmentManager(), "login");
//            listener.onLoginClick(btnLogin, edUsername.getText().toString(), edPassword.getText().toString(), dl);
        });
        binding.edEmail.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public void onResume() {
        // Get existing layout params for the window
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        // Call super onResume after sizing
        super.onResume();
    }

    private boolean fieldsOk() {
        if (binding.edEmail.getText().toString().trim().isEmpty() || binding.edPassword.getText().toString().trim().isEmpty()) {
            Toast.makeText(getContext(), "Empty field !", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}