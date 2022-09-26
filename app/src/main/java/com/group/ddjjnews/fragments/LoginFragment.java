package com.group.ddjjnews.fragments;


import android.app.Activity;
import android.content.Intent;
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

import com.group.ddjjnews.MainActivity;
import com.group.ddjjnews.Utils.IndeterminateDialog;
import com.group.ddjjnews.databinding.FragmentLoginBinding;
import com.group.ddjjnews.models.User;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.facebook.ParseFacebookUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;


public class LoginFragment extends DialogFragment {
    public static final String TAG = "LoginFragment";
    FragmentLoginBinding binding;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnLogin.setOnClickListener(v -> {
            if (!fieldsOk()) return;
            //display indeterminate progress
            IndeterminateDialog dl = IndeterminateDialog.newInstance("Logging In", "Please wait!");
            dl.setCancelable(false);
            dl.show(getChildFragmentManager(), "f_login");
            // Login with credentials
            User.customLogInInBackground(binding.edEmail.getText().toString(), binding.edPassword.getText().toString(), (object, e) -> {
                if (e == null) {
                    dl.dismiss();
                    dismiss();
                    ((MainActivity)getActivity()).restartActivity(); // Recreate the MainActivity
                } else {
                    Toast.makeText(getContext(),  ""+ e.getMessage(), Toast.LENGTH_SHORT).show();
                    dl.dismiss();
                }
            });
        });

        binding.btnJoinUs.setOnClickListener(v -> {
            if (!fieldsOk()) return;
            //display indeterminate progress
            IndeterminateDialog dl = IndeterminateDialog.newInstance("Logging In", "Please wait!");
            dl.setCancelable(false);
            dl.show(getChildFragmentManager(), "f_sign_up");
            // Sign with credentials and store the sessionToken on device
            User.customSignUpInBackground(binding.edEmail.getText().toString(), binding.edPassword.getText().toString(), (object, e) -> {
                if (e == null) {
                    dl.dismiss();
                    dismiss();
                    ((MainActivity) requireActivity()).restartActivity(); // Recreate the MainActivity
                } else {
                    Toast.makeText(getContext(),  ""+ e.getMessage(), Toast.LENGTH_SHORT).show();
                    dl.dismiss();
                }
            });
        });

        binding.oauthFacebook.setOnClickListener(view1 -> loginVIAFacebook());

        binding.edEmail.requestFocus();
        Objects.requireNonNull(getDialog()).getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(getContext(), "Fron onActivityResult FragmentLogin", Toast.LENGTH_SHORT).show();
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        // Get existing layout params for the window
        WindowManager.LayoutParams params = Objects.requireNonNull(getDialog()).getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes(params);
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
    public void loginVIAFacebook() {
        Collection<String> permissions = Arrays.asList("public_profile", "email");
        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, permissions, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {

                if (user == null) {
                    Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                } else if (user.isNew()) {
                    Log.d("MyApp", "User signed up and logged in through Facebook!");

                } else {
                    Log.d("MyApp", "User logged in through Facebook!");
                }
            }
        });

    }

}