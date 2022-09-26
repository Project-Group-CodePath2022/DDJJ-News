package com.group.ddjjnews.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.group.ddjjnews.MainActivity;
import com.group.ddjjnews.Utils.IndeterminateDialog;
import com.group.ddjjnews.databinding.FragmentLoginBinding;
import com.group.ddjjnews.models.User;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class LoginFragment extends DialogFragment {
    public static final String TAG = "LoginFragment";
    FragmentLoginBinding binding;

    String name = null, email = null;
    TextView mUsername, mEmailID;
    ParseUser parseUser;


    public static final List<String> mPermissions = new ArrayList<String>() {{
        add("public_profile");
        add("email");
    }};

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

        // Facebook login
        binding.oauthFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseFacebookUtils.logInWithReadPermissionsInBackground(getActivity(), mPermissions, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException err) {

                        if (user == null) {
                            Log.d("App", "Uh oh. The user cancelled the Facebook login.");
                        } else if (user.isNew()) {
                            Log.d("App", "User signed up and logged in through Facebook!");
                            getUserDetailsFromFB();
                        } else {
                            Log.d("App", "User logged in through Facebook!");
                            getUserDetailsFromParse();
                        }
                    }
                });
            }
        });

        binding.edEmail.requestFocus();
        Objects.requireNonNull(getDialog()).getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    private void getUserDetailsFromParse() {
        parseUser = ParseUser.getCurrentUser();

//Fetch profile photo
//        try {
//            ParseFile parseFile = parseUser.getParseFile("profileThumb");
//            byte[] data = parseFile.getData();
//            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
//            mProfileImage.setImageBitmap(bitmap);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        mEmailID.setText(parseUser.getEmail());
        mUsername.setText(parseUser.getUsername());

        Toast.makeText(getContext(), "Welcome back " + mUsername.getText().toString(), Toast.LENGTH_SHORT).show();

    }



    private void getUserDetailsFromFB() {
        Bundle parameters = new Bundle();
        parameters.putString("fields", "email,name");

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me",
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        /* handle the result */
                        try {

                            email = response.getJSONObject().getString("email");
                            mEmailID.setText(email);

                            name = response.getJSONObject().getString("name");
                            mUsername.setText(name);

//                            JSONObject picture = response.getJSONObject().getJSONObject("picture");
//                            JSONObject data = picture.getJSONObject("data");
//
//                            //  Returns a 50x50 profile picture
//                            String pictureUrl = data.getString("url");
//
//                            new ProfilePhotoAsync(pictureUrl).execute();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
}