package com.group.ddjjnews.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class GenericAdapter<T extends Object, F extends ViewDataBinding, G extends ViewDataBinding> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int EMPTY = 10;
    public static final int NORMAL = 20;

    Context context;
    List<T> items;
    Class<? extends ViewDataBinding> b;
    Class<? extends ViewDataBinding> emp;


    public GenericAdapter(Context context, List<T> items, Class<? extends ViewDataBinding> b, @Nullable Class<? extends ViewDataBinding> emp) {
        this.context = context;
        this.items = items;
        this.b = b;
        this.emp = emp;
    }

    @Override
    public int getItemViewType(int position) {
        if (items.size() == 0) return EMPTY;
        return NORMAL;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == NORMAL) {
            F binding = null;
            try {
                Method m = b.getDeclaredMethod("inflate", LayoutInflater.class, ViewGroup.class, boolean.class);
                binding = (F) m.invoke(null, LayoutInflater.from(parent.getContext()), parent, false);

            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
            assert binding != null;
            return new ViewHolder(binding);
        } else {
            G binding = null;
            try {
                Method m = emp.getDeclaredMethod("inflate", LayoutInflater.class, ViewGroup.class, boolean.class);
                binding = (G) m.invoke(null, LayoutInflater.from(parent.getContext()), parent, false);

            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
            assert binding != null;
            return new EmptyHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof GenericAdapter.ViewHolder)
            ((ViewHolder)holder).bind(items.get(position));
        else if (holder instanceof GenericAdapter.EmptyHolder)
            ((EmptyHolder)holder).bind();
    }

    public void bindItem(F binding, T item, int position) {}

    public void bindEmpty(G binding) {}

    @Override
    public int getItemCount() {
        if (this.emp != null && this.items.size() == 0)
            return 1;
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

    class EmptyHolder extends RecyclerView.ViewHolder {
        G binding;
        public EmptyHolder(@NonNull G binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void bind() {
            bindEmpty(binding);
        }
    }
}
