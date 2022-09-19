package com.group.ddjjnews.fragments.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.group.ddjjnews.R;
import com.group.ddjjnews.adapters.admin.NewsAdapterAdmin;
import com.group.ddjjnews.adapters.admin.UserAdapterAdmin;
import com.group.ddjjnews.databinding.FragmentRefreshFloatingBaseBinding;
import com.group.ddjjnews.fragments.RefreshFloatingBaseFragment;
import com.group.ddjjnews.models.News;
import com.group.ddjjnews.models.User;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class UserListAdminFragment extends RefreshFloatingBaseFragment {
    List<ParseObject> users = new ArrayList<>();
    FragmentRefreshFloatingBaseBinding binding;

    public static UserListAdminFragment newInstance() {
        UserListAdminFragment f = new UserListAdminFragment();
        Bundle bundle = new Bundle();
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new UserAdapterAdmin(getContext(), users);
        layoutManager = new LinearLayoutManager(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentRefreshFloatingBaseBinding.inflate(inflater, container, false);
        this.rcItems = (RecyclerView) binding.rcView;
        this.sRefresh = binding.sRefresh;
        this.floatingAction = binding.floatingAction;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getAllUsers();
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }

    @Override
    protected void loadMore(int page, int totalItemsCount, RecyclerView view) {
    }

    @Override
    protected void handleFloatingAB(View view) {
        UserCreationAdminFragment fr = UserCreationAdminFragment.newInstance();
        FragmentManager fm = getChildFragmentManager();
        fr.setCancelable(true);
        fr.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
        fr.show(fm, "user_creation_fragment");
    }

    @Override
    protected void handleRefresh(SwipeRefreshLayout swipeCont) {
        getAllUsers();
    }

    private void getAllUsers() {
        sRefresh.setRefreshing(true);
        User.getAllUser(new HashMap(), new User.Callback() {
            @Override
            public void done(User object, Exception e) {}

            @Override
            public void done(Collection objects, Exception e) {
                if (e == null) {
                    users.clear();
                    users.addAll(objects);
                    adapter.notifyDataSetChanged();
                }
                sRefresh.setRefreshing(false);
            }
        });
    }
}
