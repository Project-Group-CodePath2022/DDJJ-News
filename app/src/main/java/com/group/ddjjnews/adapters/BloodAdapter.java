package com.group.ddjjnews.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group.ddjjnews.Utils.TimeFormatter;
import com.group.ddjjnews.databinding.BloodItemBinding;
import com.group.ddjjnews.models.Blood;
import com.parse.ParseObject;

import java.util.List;

public class BloodAdapter extends RecyclerView.Adapter<BloodAdapter.BloodHolder> {
    Context context;
    List<ParseObject> requests;

    public BloodAdapter(Context ctx, List list) {
        this.context = ctx;
        this.requests = list;
    }

    @NonNull
    @Override
    public BloodHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BloodHolder(BloodItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BloodHolder holder, int position) {
        holder.bind((Blood) requests.get(position));
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    class BloodHolder extends RecyclerView.ViewHolder {
        BloodItemBinding binding;

        public BloodHolder(@NonNull BloodItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Blood item) {
            binding.bloodTvType.setText(item.getKeyGroupBlood());
            binding.bloodTvForName.setText(item.getKeyForName());
            binding.bloodTvDesc.setText(item.getKeyDescription());
            binding.bloodTvCreatedAt.setText(TimeFormatter.getTimeDifference(item.getCreatedAt().toString()));
        }
    }
}
