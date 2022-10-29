package com.group.ddjjnews.fragments.admin;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.target.Target;
import com.group.ddjjnews.databinding.FragmentCommentsBinding;
import com.group.ddjjnews.databinding.UserItemAdminDetailBinding;
import com.group.ddjjnews.models.User;
import com.parse.ParseDecoder;
import com.parse.ParseObject;
import com.parse.ParseRole;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

import io.noties.markwon.Markwon;
import io.noties.markwon.image.AsyncDrawable;
import io.noties.markwon.image.glide.GlideImagesPlugin;

public class UserDetailAdmin extends DialogFragment {
    UserItemAdminDetailBinding binding;
    User item;

    public UserDetailAdmin(){}

    public static UserDetailAdmin newInstance(User item) {
        UserDetailAdmin f = new UserDetailAdmin();
        Bundle args = new Bundle();
        args.putParcelable("item", item);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            item = getArguments().getParcelable("item");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = UserItemAdminDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.tvCreated.setText(item.getCreatedAt().toString());
        binding.tvEmail.setText(item.getUsername());
        binding.tvUpdated.setText(item.getUpdatedAt().toString());
        binding.tvId.setText(item.getObjectId());
        if (item.getKeyActive())
            binding.tvStatus.setText("active");
        else
            binding.tvStatus.setText("noactive");
        if (item.get("role") != null) {
            binding.tvRole.setText(((ParseRole) ParseObject.fromJSON(new JSONObject((HashMap) item.get("role")), "_Role", ParseDecoder.get())).getName());
        }
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
}
