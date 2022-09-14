package com.group.ddjjnews.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.group.ddjjnews.R;
import com.group.ddjjnews.databinding.MymodelItemBinding;
import com.group.ddjjnews.models.MyModel;

import java.util.List;
import java.util.zip.Inflater;

public class SimpleMyModelAdapter extends RecyclerView.Adapter<SimpleMyModelAdapter.MyModelHolder> {
    List<MyModel> models;

    public SimpleMyModelAdapter(List<MyModel> models) {
        this.models = models;
    }
    @NonNull
    @Override
    public MyModelHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MymodelItemBinding itemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.mymodel_item, parent, false);
        return new MyModelHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyModelHolder holder, int position) {
        holder.bind(models.get(position));
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    class MyModelHolder extends RecyclerView.ViewHolder {
        MymodelItemBinding itemBinding;
        public MyModelHolder(@NonNull MymodelItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }

        public void bind(MyModel item) {
            itemBinding.tvTitle.setText(item.title);
            itemBinding.tvDescription.setText(item.description);
        }
    }
}
