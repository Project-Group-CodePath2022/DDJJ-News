package com.group.ddjjnews.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.group.ddjjnews.Utils.TimeFormatter;

import com.group.ddjjnews.databinding.NewsSavedItemBinding;
import com.group.ddjjnews.models.Category;
import com.group.ddjjnews.models.News;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.List;

public class SavedAdapter extends RecyclerView.Adapter<SavedAdapter.NewsHolder> {
    Context context;
    List<ParseObject> requests;
    private SavedListener listener;

    public void setListener(SavedListener listener) {
        this.listener = listener;
    }

    public interface SavedListener {
        void onOptionsItemClicked(News item, int pos);
    }

    public SavedAdapter(Context ctx, List list) {
        this.context = ctx;
        this.requests = list;
    }

    @NonNull
    @Override
    public NewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsHolder(NewsSavedItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewsHolder holder, int position) {
        holder.bind((News) requests.get(position));
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    class NewsHolder extends RecyclerView.ViewHolder {
        NewsSavedItemBinding binding;

        public NewsHolder(@NonNull NewsSavedItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void bind(News item) {
            if (item.getKeyImage() != null)
                Glide.with(context)
                        .load(item.getKeyImage().getUrl())
                        .transform(new RoundedCorners(22))
                        .into(binding.imgImage);
            String detail = "";
            // detail += ((Category)item.getKeyCategory()).getKeyName();
            // detail += " - ";
            detail += TimeFormatter.getTimeDifference(item.getCreatedAt().toString());
            // binding.imgImage.setImageBitmap(BitmapFactory.decodeByteArray(item.getBytes("image")));
            binding.tvTitle.setText(item.getKeyTitle());
            binding.tvDetail.setText(detail);
            binding.options.setOnClickListener(view -> listener.onOptionsItemClicked(item, getAdapterPosition()));
        }
    }
}
