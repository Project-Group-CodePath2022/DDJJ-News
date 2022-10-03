package com.group.ddjjnews.adapters.admin;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.group.ddjjnews.R;

import com.group.ddjjnews.databinding.NewsItemAdminBinding;
import com.group.ddjjnews.models.News;
import com.parse.ParseDecoder;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseRole;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class NewsAdapterAdmin extends RecyclerView.Adapter<NewsAdapterAdmin.NewsHolder> {
    Context context;
    List<ParseObject> news;
    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void onOptionsClicked(News item, int pos);
    }
    public NewsAdapterAdmin(Context ctx, List<ParseObject> news) {
        this.context = ctx;
        this.news = news;
    }

    @NonNull
    @Override
    public NewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsHolder(NewsItemAdminBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewsHolder holder, int position) {
        holder.bind((News) news.get(position));
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    class NewsHolder extends RecyclerView.ViewHolder {
        NewsItemAdminBinding binding;

        public NewsHolder(@NonNull NewsItemAdminBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(News item) {

             if (item.getKeyImage() != null)
                Glide.with(context)
                        .load(item.getKeyImage().getUrl())
                        .transform(new RoundedCorners(12))
                        .into(binding.imgImage);

            if (item.getKeyActive())
                binding.active.setImageResource(R.drawable.ic_baseline_check_circle_green);
            else
                binding.active.setImageResource(R.drawable.ic_baseline_check_circle);
            binding.createdAt.setText(item.getCreatedAt().toString());

            binding.options.setOnClickListener(view -> listener.onOptionsClicked(item, getAdapterPosition()));

            binding.title.setText(item.getKeyTitle());
        }
    }
}
