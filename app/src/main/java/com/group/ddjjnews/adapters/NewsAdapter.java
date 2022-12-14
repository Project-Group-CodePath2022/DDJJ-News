package com.group.ddjjnews.adapters;

import android.annotation.SuppressLint;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.group.ddjjnews.R;

import com.group.ddjjnews.Utils.TimeFormatter;
import com.group.ddjjnews.databinding.EmptyStateBinding;
import com.group.ddjjnews.databinding.NewsAlauneItemBinding;
import com.group.ddjjnews.databinding.NewsItemBinding;
import com.group.ddjjnews.models.Category;
import com.group.ddjjnews.models.News;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int DEFAULT_TYPE = 10;
    public static final int ALAUNE_TYPE = 20;
    public static final int EMPTY = 30;
    private NewsAdapterListener listener;
    Context context;
    List<ParseObject> news;

    public void setListener(NewsAdapterListener listener) {
        this.listener = listener;
    }
    public interface NewsAdapterListener { void itemClicked(News item); }

    public NewsAdapter(Context context, List<ParseObject> news) {
       this.context = context;
       this.news = news;
    }

    @Override
    public int getItemViewType(int position) {
        if (news.size() == 0) return EMPTY;
        if (position == 0 || (position % 7) == 0) {
            return ALAUNE_TYPE;
        } else {
            return DEFAULT_TYPE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == EMPTY)
            return new EmptyHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.empty_state, parent, false));

        if (viewType == ALAUNE_TYPE) {
            return new NewsAlauneHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.news_alaune_item, parent, false));
        }
        return new NewsHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.news_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NewsHolder)
            ((NewsHolder)holder).bind((News) news.get(position));
        else if (holder instanceof EmptyHolder)
            ((EmptyHolder)holder).bind();
        else
            ((NewsAlauneHolder)holder).bind((News) news.get(position));
    }

    @Override
    public int getItemCount() {
        if (news.size() == 0) return 1;
        return news.size();
    }


    private class EmptyHolder extends RecyclerView.ViewHolder{
        EmptyStateBinding binding;

        public EmptyHolder(@NonNull EmptyStateBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
        public void bind() {
            binding.title.setText("News");
            binding.subTitle.setText("DDJJ News");
        }
    }

    private class NewsHolder extends RecyclerView.ViewHolder {
        NewsItemBinding itemBinding;
        public NewsHolder(@NonNull NewsItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }

        public void bind(News item) {
            if (item.getKeyImage() != null)
                Glide.with(context)
                        .load(item.getKeyImage().getUrl())
                        .transform(new RoundedCorners(20))
                        .into(itemBinding.imgImage);
            itemBinding.title.setText(item.getKeyTitle());
            itemBinding.tvCategory.setText(((Category)item.getKeyCategory()).getKeyName());
            itemBinding.createdAt.setText(" | " + TimeFormatter.getTimeDifference(item.getCreatedAt().toString()));
            // Set listener
            itemBinding.getRoot().setOnClickListener(view -> listener.itemClicked(item));

        }
    }

    private class NewsAlauneHolder extends RecyclerView.ViewHolder {
        NewsAlauneItemBinding itemBinding;
        public NewsAlauneHolder(@NonNull NewsAlauneItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }

        @SuppressLint("SetTextI18n")
        public void bind(News item) {
            if (item.getKeyImage() != null)
                Glide.with(context)
                        .load(item.getKeyImage().getUrl())
                        .transform(new RoundedCorners(16))
                        .into(itemBinding.imgImage);
            itemBinding.tvTitle.setText(item.getKeyTitle());
            itemBinding.tvCreatedAt.setText(TimeFormatter.getTimeDifference(item.getCreatedAt().toString()));
            try {
                itemBinding.tvCategory.setText(item.getKeyCategory().fetchIfNeeded().getString("name"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            // Set listener
            itemBinding.getRoot().setOnClickListener(view -> listener.itemClicked(item));
        }
    }
}