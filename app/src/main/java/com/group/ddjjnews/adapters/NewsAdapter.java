package com.group.ddjjnews.adapters;

import android.annotation.SuppressLint;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.group.ddjjnews.R;

import com.group.ddjjnews.Utils.TimeFormatter;
import com.group.ddjjnews.databinding.NewsAlauneItemBinding;
import com.group.ddjjnews.databinding.NewsItemBinding;
import com.group.ddjjnews.models.News;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int DEFAULT_TYPE = 10;
    public static final int ALAUNE_TYPE = 20;

    private NewsAdapterListener listener;

    public void setListener(NewsAdapterListener listener) {
        this.listener = listener;
    }

    public interface NewsAdapterListener {
        void itemClicked(News item);
    }

    Context context;
    List<ParseObject> news;

    public NewsAdapter(Context context, List<ParseObject> news) {
       this.context = context;
       this.news = news;
    }

    @Override
    public int getItemViewType(int position) {
        if (((News)news.get(position)).isAlaune() && (position == 0 || ((position + 1) % 3) == 0)) {
            return ALAUNE_TYPE;
        }
        return DEFAULT_TYPE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ALAUNE_TYPE) {
            return new NewsAlauneHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.news_alaune_item, parent, false));
        }
        return new NewsHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.news_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NewsHolder)
            ((NewsHolder)holder).bind((News) news.get(position));
        else
            ((NewsAlauneHolder)holder).bind((News) news.get(position));
    }

    @Override
    public int getItemCount() {
        return news.size();
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
            itemBinding.createdAt.setText("Date: " + TimeFormatter.getTimeDifference(item.getCreatedAt().toString()));
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
            itemBinding.tvCreatedAt.setText("Date: " + TimeFormatter.getTimeDifference(item.getCreatedAt().toString()));
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