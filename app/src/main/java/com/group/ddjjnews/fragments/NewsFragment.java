package com.group.ddjjnews.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.group.ddjjnews.MainActivity;
import com.group.ddjjnews.R;
import com.group.ddjjnews.adapters.NewsAdapter;
import com.group.ddjjnews.adapters.ViewPagerDynamicAdapter;
import com.group.ddjjnews.databinding.FragmentNewsBinding;
import com.group.ddjjnews.models.News;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class NewsFragment extends Fragment {
    FragmentNewsBinding binding;
    List<ParseObject> newsPosts = new ArrayList<>();
    NewsAdapter adapter;
    GridLayoutManager layoutManager;
    // TODO: refresh, pagination, filter and search

    public NewsFragment() {}

    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        adapter = new NewsAdapter(getContext(), newsPosts);
        layoutManager = new GridLayoutManager(getContext(), 2);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.pager.setOnTouchListener((view1, motionEvent) -> true); // stop scrolling viewPager
        binding.pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tab));
        binding.tab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // setCurrentItem as the tab position
                binding.pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
        setDynamicFragmentToTabLayout();
//        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                int type = adapter.getItemViewType(position);
//                if (type == NewsAdapter.ALAUNE_TYPE)
//                    return 2;
//                return 1;
//            }
//        });
//        ((RecyclerView)binding.rcNews).setLayoutManager(layoutManager);
//        ((RecyclerView)binding.rcNews).setAdapter(adapter);
//
//        adapter.setListener((NewsAdapter.NewsAdapterListener) item -> ((MainActivity)getContext()).gotoDetail("news", item));
//        getNewsPosts();
    }
//
//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        inflater.inflate(R.menu.main_toolbar_menu, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        Toast.makeText(getContext(), "From NEWS :: " + item.getTitle(), Toast.LENGTH_SHORT).show();
//        return super.onOptionsItemSelected(item);
//    }
    private void setDynamicFragmentToTabLayout() {
        for (int i = 0; i < 7; i++) {
            binding.tab.addTab(binding.tab.newTab().setText("My Page : " + i));
        }
        ViewPagerDynamicAdapter adapter = new ViewPagerDynamicAdapter(getActivity().getSupportFragmentManager(), binding.tab.getTabCount());
        binding.pager.setAdapter(adapter);
        binding.pager.setCurrentItem(0);
    }


//    private void getNewsPosts() {
//        // TODO: get 21 latest posts
//        HashMap<String, String> params = new HashMap<>();
//        News.getNews(params, (objects, e) -> {
//            if (e == null) {
//                newsPosts.addAll((Collection<? extends ParseObject>) objects);
//                adapter.notifyDataSetChanged();
//            } else {
//                Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}