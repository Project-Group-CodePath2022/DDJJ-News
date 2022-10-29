package com.group.ddjjnews;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import com.group.ddjjnews.databinding.ActivityCategoryDetailBinding;
import com.group.ddjjnews.fragments.NestedNewsFragment;

import com.group.ddjjnews.models.News;
import com.parse.ParseObject;

import java.util.Collection;
import java.util.HashMap;

public class CategoryDetailActivity extends AppCompatActivity {
    ActivityCategoryDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);

        binding = DataBindingUtil.setContentView(CategoryDetailActivity.this, R.layout.activity_category_detail);
        setSupportActionBar(binding.searchAppBar.findViewById(R.id.toolbar)); // Set toolbar instead actionbar

        Intent intent = getIntent();
        if (intent != null && intent.getStringExtra("category") != null) {
            String category = intent.getStringExtra("category");
            getSupportActionBar().setTitle(capitalize(category));
            getSupportFragmentManager().beginTransaction().replace(R.id.catDetail, NewsByCategory.newInstance(category)).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nada_menu, menu);
        return true;
    }

    @Nullable
    @Override
    public Intent getSupportParentActivityIntent() {
        finish();
        return null;
    }

    public static String capitalize(String a) {
        if (a == null || a.length() == 0) return a;
        return a.substring(0, 1).toUpperCase() + a.substring(1);
    }


    static public class NewsByCategory extends NestedNewsFragment {
        String category;


        public static NewsByCategory newInstance(String category) {
            NewsByCategory f = new NewsByCategory();
            Bundle bundle = new Bundle();
            bundle.putString("category", category);
            f.setArguments(bundle);
            return f;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                category = getArguments().getString("category");
            }
        }

        @Override
        protected void getNewsPosts() {
            HashMap<String, Object> params = new HashMap<>();
            binding.swipeContainer.setRefreshing(true);
            News.getNews(params, (objects, e) -> {
                if (e == null) {
                    newsPosts.clear();
                    newsPosts.addAll((Collection<? extends ParseObject>) objects);
                    adapter.notifyDataSetChanged();
                    endless.resetState();
                }
                binding.swipeContainer.setRefreshing(false);
            });
        }

        @Override
        protected void paginate(int page) {
            HashMap<String, Object> params = new HashMap<>();
            params.put("page", page);
            params.put("category", category);
            News.getNews(params, (objects, e) -> {
                if (e == null) {
                    newsPosts.addAll((Collection<? extends ParseObject>) objects);
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

}


