package com.group.ddjjnews.adapters;

import android.annotation.SuppressLint;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.group.ddjjnews.R;

import com.group.ddjjnews.databinding.NewsAlauneItemBinding;
import com.group.ddjjnews.databinding.NewsItemBinding;
import com.group.ddjjnews.models.News;
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
        if (News.from(news.get(position)).isAlaune()) {
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
            ((NewsHolder)holder).bind(News.from(news.get(position)));
        else
            ((NewsAlauneHolder)holder).bind(News.from(news.get(position)));
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
            itemBinding.title.setText(item.getKeyTitle());
            itemBinding.createdAt.setText(item.getKeyDescription());
            // Set listener
            itemBinding.title.setOnClickListener(view -> listener.itemClicked(item));

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
            itemBinding.tvTitle.setText("Alaune :: " + item.getKeyTitle());
            itemBinding.tvDescription.setText(item.getKeyDescription());
            // Set listener
            itemBinding.tvTitle.setOnClickListener(view -> listener.itemClicked(item));
        }
    }
}