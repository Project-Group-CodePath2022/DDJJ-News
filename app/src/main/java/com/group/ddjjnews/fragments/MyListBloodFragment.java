package com.group.ddjjnews.fragments;


import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.group.ddjjnews.adapters.BloodAdapter;
import com.group.ddjjnews.adapters.MyBloodAdapter;
import com.group.ddjjnews.models.Blood;

import java.util.Collection;
import java.util.HashMap;

public class MyListBloodFragment extends ListBloodFragment {
    public static MyListBloodFragment newInstance() {
        MyListBloodFragment f = new MyListBloodFragment();
        Bundle bundle = new Bundle();
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new MyBloodAdapter(getContext(), requests);
        layoutManager = new LinearLayoutManager(getContext());
    }

    @Override
    protected void getRequestedBlood() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("me", true);
        Blood.getAll(params, new Blood.Callback() {
            @Override
            public void done(Blood object, Exception e) {}
            @Override
            public void done(Collection objects, Exception e) {
                if (e == null) {
                    requests.clear();
                    requests.addAll(objects);
                    adapter.notifyDataSetChanged();
                    sRefresh.setRefreshing(false);
                }
            }
        });
    }

    public void addOne(Blood b) {
        requests.add(0, b);
        adapter.notifyItemInserted(0);
        rcItems.scrollToPosition(0);
    }
}
