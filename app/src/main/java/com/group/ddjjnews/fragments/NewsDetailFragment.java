package com.group.ddjjnews.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.target.Target;
import com.group.ddjjnews.R;
import com.group.ddjjnews.Utils.TimeFormatter;
import com.group.ddjjnews.databinding.FragmentNewsDetailBinding;
import com.group.ddjjnews.models.News;

import com.parse.ParseObject;
import io.noties.markwon.Markwon;
import io.noties.markwon.image.AsyncDrawable;
import io.noties.markwon.image.glide.GlideImagesPlugin;

public class NewsDetailFragment extends Fragment {
    private static final String ARG_PARAM1 = "object";
    FragmentNewsDetailBinding binding;

    private News mParam1;
    CommentsFragment fr;
    public NewsDetailFragment() {}

    public static NewsDetailFragment newInstance(News object) {
        NewsDetailFragment fragment = new NewsDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, object);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getParcelable(ARG_PARAM1);
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
        if (fr == null)
            fr = CommentsFragment.newInstance(mParam1.getObjectId());
        fr.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
        fr.setCancelable(true);

        display();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.detail_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.detail_save) {
            // Save this post in local
            mParam1.pinInBackground(e -> {
                if (e == null) {
                    Toast.makeText(getContext(), "News saved!", Toast.LENGTH_SHORT).show();
                }
            });
            return true;
        } else if (itemId == R.id.detail_comments) {
            // Show fragment containing all comments for this news and write a new one
            fr.show(getChildFragmentManager(), "c_m_f");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
 

    private void display() {
        binding.DetailNewsTitle.setText(mParam1.getKeyTitle());
        binding.DetailNewsCreatedAt.setText("Date : "+ TimeFormatter.getTimeDifference(mParam1.getCreatedAt().toString()));
        Glide.with(requireContext()).load(mParam1.getKeyImage().getUrl()).into(binding.detailNewsImage);
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
        markwon1.setMarkdown(binding.DetailNewsContent, mParam1.getKeyContent());
    }


    private void refresh() {
        News.getDetailNews(mParam1.getObjectId(), (object, e) -> {
            if (e == null) {
                mParam1 = (News) object;
                Toast.makeText(getContext(), ((News) object).getKeyTitle(), Toast.LENGTH_SHORT).show();
                display();
            }
            binding.sRefresh.setRefreshing(false);
        });
    }
}