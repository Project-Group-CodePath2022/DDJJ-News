package com.group.ddjjnews.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group.ddjjnews.Utils.TimeFormatter;
import com.group.ddjjnews.databinding.BloodItemBinding;
import com.group.ddjjnews.databinding.MyBloodItemBinding;
import com.group.ddjjnews.models.Blood;
import com.parse.ParseObject;

import java.util.List;

public class MyBloodAdapter extends RecyclerView.Adapter<MyBloodAdapter.BloodHolder> {
    Context context;
    List<ParseObject> requests;

    public MyBloodAdapter(Context ctx, List list) {
        this.context = ctx;
        this.requests = list;
    }

    @NonNull
    @Override
    public BloodHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BloodHolder(MyBloodItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
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
        MyBloodItemBinding binding;

        public BloodHolder(@NonNull MyBloodItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void bind(Blood item) {
            binding.bloodTvForName.setText(item.getText());
            binding.bloodTvDesc.setText(item.getKeyDescription());
            binding.bloodTvCreatedAt.setText(item.getCreatedAt().toString());
            binding.options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Options menu", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
