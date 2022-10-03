package com.group.ddjjnews.adapters.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.group.ddjjnews.R;
import com.group.ddjjnews.databinding.UserItemAdminBinding;
import com.group.ddjjnews.models.User;
import com.parse.ParseDecoder;
import com.parse.ParseObject;
import com.parse.ParseRole;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class UserAdapterAdmin extends RecyclerView.Adapter<UserAdapterAdmin.UserHolder> {
    Context context;
    List<ParseObject> users;
    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void onOptionsClicked(User item, int pos);
    }

    public UserAdapterAdmin(Context ctx, List<ParseObject> users) {
        this.context = ctx;
        this.users = users;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserHolder(UserItemAdminBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        holder.bind((User) users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserHolder extends RecyclerView.ViewHolder {
        UserItemAdminBinding binding;

        public UserHolder(@NonNull UserItemAdminBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(User item) {
            if (item.get("role") != null) {
                binding.role.setText(((ParseRole)ParseObject.fromJSON(new JSONObject((HashMap) item.get("role")), "_Role", ParseDecoder.get())).getName());
            }
            if (item.getKeyActive())
                binding.active.setImageResource(R.drawable.ic_baseline_check_circle_green);
            else
                binding.active.setImageResource(R.drawable.ic_baseline_check_circle);
            if (item.getCreatedAt() != null)
                binding.createdAt.setText(item.getCreatedAt().toString());
            binding.options.setOnClickListener(view -> listener.onOptionsClicked(item, getAdapterPosition()));
            binding.email.setText(item.getUsername());
        }
    }
}
