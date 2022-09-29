package com.group.ddjjnews.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.group.ddjjnews.Utils.TimeFormatter;
import com.group.ddjjnews.databinding.AlertItemBinding;
import com.group.ddjjnews.databinding.BloodItemBinding;
import com.group.ddjjnews.models.Alert;
import com.group.ddjjnews.models.Blood;
import com.parse.ParseObject;

import java.util.List;

public class AlertAdapter extends RecyclerView.Adapter<AlertAdapter.AlertHolder> {
    Context context;
    List<ParseObject> requests;

    public AlertAdapter(Context ctx, List list) {
        this.context = ctx;
        this.requests = list;
    }

    @NonNull
    @Override
    public AlertHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AlertHolder(AlertItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AlertHolder holder, int position) {
        holder.bind((Alert)requests.get(position));

    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    class AlertHolder extends RecyclerView.ViewHolder {
        AlertItemBinding binding;

        public AlertHolder(@NonNull AlertItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void bind(Alert item) {
            if (item.getKeyImage() != null)
                Glide.with(context)
                        .load(item.getKeyImage().getUrl())
                        .transform(new RoundedCorners(16))
                        .into(binding.imgImage);
            binding.tvCategory.setText("Robbery");
            binding.tvCreatedAt.setText(TimeFormatter.getTimeDifference(item.getCreatedAt().toString()));
        }
    }
}
