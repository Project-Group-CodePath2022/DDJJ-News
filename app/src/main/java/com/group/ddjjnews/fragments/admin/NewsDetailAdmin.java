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
import com.group.ddjjnews.databinding.NewsItemAdminDetailBinding;
import com.group.ddjjnews.databinding.UserItemAdminDetailBinding;
import com.group.ddjjnews.models.News;
import com.group.ddjjnews.models.User;

import java.util.Objects;

import io.noties.markwon.Markwon;
import io.noties.markwon.image.AsyncDrawable;
import io.noties.markwon.image.glide.GlideImagesPlugin;

public class NewsDetailAdmin extends DialogFragment {
    NewsItemAdminDetailBinding binding;
    News item;

    public NewsDetailAdmin(){}

    public static NewsDetailAdmin newInstance(News item) {
        NewsDetailAdmin f = new NewsDetailAdmin();
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
        binding = NewsItemAdminDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // binding.tvBy.setText(item);
        // binding.imgImage
        binding.tvContent.setText(item.getKeyContent());
        // binding.tvDescription.setText(item);
        binding.tvCreated.setText(item.getCreatedAt().toString());
        binding.tvTitle.setText(item.getKeyTitle());
        binding.tvUpdated.setText(item.getUpdatedAt().toString());
        binding.tvId.setText(item.getObjectId());
        if (item.getKeyActive())
            binding.tvStatus.setText("active");
        else
            binding.tvStatus.setText("noactive");

        binding.tvBy.setText(item.getKeyAuthor().getUsername());

        Glide.with(requireContext()).load(item.getKeyImage().getUrl()).into(binding.imgImage);
        final Markwon markwon1 = Markwon.builder(requireContext())
                .usePlugin(GlideImagesPlugin.create(requireContext()))
                .usePlugin(GlideImagesPlugin.create(Glide.with(getContext())))
                .usePlugin(GlideImagesPlugin.create(new GlideImagesPlugin.GlideStore() {
                    @NonNull
                    @Override
                    public RequestBuilder<Drawable> load(@NonNull AsyncDrawable drawable) {
                        return Glide.with(requireContext()).load(drawable.getDestination());
                    }

                    @Override
                    public void cancel(@NonNull Target<?> target) {
                        Glide.with(requireContext()).clear(target);
                    }
                })).build();
        markwon1.setMarkdown(binding.tvContent, item.getKeyContent());

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
