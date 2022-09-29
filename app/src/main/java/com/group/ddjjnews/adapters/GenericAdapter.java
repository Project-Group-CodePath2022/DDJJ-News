package com.group.ddjjnews.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.ParseObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class GenericAdapter<T extends ParseObject, F extends ViewDataBinding> extends RecyclerView.Adapter<GenericAdapter.ViewHolder> {
    Context context;
    List<? extends ParseObject> items;
    Class<? extends ViewDataBinding> b;

    public GenericAdapter(Context context, List<? extends ParseObject> items, Class<? extends ViewDataBinding> b) {
        this.context = context;
        this.items = items;
        this.b = b;
    }

    @NonNull
    @Override
    public GenericAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        F binding = null;
        try {
            Method m = b.getDeclaredMethod("inflate", LayoutInflater.class, ViewGroup.class, boolean.class);
            binding = (F) m.invoke(null, LayoutInflater.from(parent.getContext()), parent, false);

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        assert binding != null;
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GenericAdapter.ViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    public void bindItem(F binding, T item, int position) {}

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        F binding;

        public ViewHolder(@NonNull F binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(T item) {
            bindItem(binding, item, getAdapterPosition());
        }
    }
}
