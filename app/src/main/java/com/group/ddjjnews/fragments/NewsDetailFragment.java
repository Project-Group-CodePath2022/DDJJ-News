package com.group.ddjjnews.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.target.Target;
import com.group.ddjjnews.Utils.TimeFormatter;
import com.group.ddjjnews.databinding.FragmentNewsDetailBinding;
import com.group.ddjjnews.models.News;
import com.parse.FunctionCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import io.noties.markwon.Markwon;
import io.noties.markwon.image.AsyncDrawable;
import io.noties.markwon.image.glide.GlideImagesPlugin;

public class NewsDetailFragment extends Fragment {
    private static final String ARG_PARAM1 = "object";
    FragmentNewsDetailBinding binding;

    private News mParam1;

    public NewsDetailFragment() {}

    public static NewsDetailFragment newInstance(ParseObject object) {
        NewsDetailFragment fragment = new NewsDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, object);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = (News)getArguments().getParcelable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewsDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.sRefresh.setOnRefreshListener(this::refresh);

        display();

    }

    private void display() {
        binding.DetailNewsTitle.setText(mParam1.getKeyTitle());
        binding.DetailNewsCreatedAt.setText("Date : "+ TimeFormatter.getTimeDifference(mParam1.getCreatedAt().toString()));
        Glide.with(getContext()).load(mParam1.getKeyImage().getUrl()).into(binding.detailNewsImage);
        final Markwon markwon1 = Markwon.builder(getContext())
                .usePlugin(GlideImagesPlugin.create(getContext()))
                .usePlugin(GlideImagesPlugin.create(Glide.with(getContext())))
                .usePlugin(GlideImagesPlugin.create(new GlideImagesPlugin.GlideStore() {
                    @NonNull
                    @Override
                    public RequestBuilder<Drawable> load(@NonNull AsyncDrawable drawable) {
                        return Glide.with(getContext()).load(drawable.getDestination());
                    }

                    @Override
                    public void cancel(@NonNull Target<?> target) {
                        Glide.with(getContext()).clear(target);
                    }
                })).build();
        markwon1.setMarkdown(binding.DetailNewsContent, mParam1.getKeyContent());
    }

    private void refresh() {
        News.getDetailNews(mParam1.getObjectId(), (object, e) -> {
            if (e == null) {
                mParam1 = (News) object;
                display();
            }
        });
    }
}