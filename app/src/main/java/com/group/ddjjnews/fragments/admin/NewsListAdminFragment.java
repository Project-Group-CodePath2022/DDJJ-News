package com.group.ddjjnews.fragments.admin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.group.ddjjnews.R;
import com.group.ddjjnews.adapters.admin.NewsAdapterAdmin;
import com.group.ddjjnews.databinding.FragmentRefreshFloatingBaseBinding;
import com.group.ddjjnews.fragments.RefreshFloatingBaseFragment;
import com.group.ddjjnews.models.Category;
import com.group.ddjjnews.models.News;

import com.group.ddjjnews.models.User;
import com.parse.FunctionCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class NewsListAdminFragment extends RefreshFloatingBaseFragment {
    List<ParseObject> news = new ArrayList<>();
    FragmentRefreshFloatingBaseBinding binding;
    List<String> nameCategories = new ArrayList<>();

    public static NewsListAdminFragment newInstance() {
        NewsListAdminFragment f = new NewsListAdminFragment();
        Bundle bundle = new Bundle();
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new NewsAdapterAdmin(getContext(), news);
        layoutManager = new LinearLayoutManager(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentRefreshFloatingBaseBinding.inflate(inflater, container, false);
        this.rcItems = binding.rcView;
        this.sRefresh = binding.sRefresh;
        this.floatingAction = binding.floatingAction;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((NewsAdapterAdmin)adapter).setListener((item, pos) -> showBSD(item, pos));
        setCategory();
        getAllNews();
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }

    @Override
    protected void loadMore(int page, int totalItemsCount, RecyclerView view) {}

    @Override
    protected void handleFloatingAB(View view) {
        NewsCreationAdminFragment fr = NewsCreationAdminFragment.newInstance(null, 0);
        fr.setNameCategories(nameCategories);
        fr.setCancelable(true);
        fr.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
        fr.show(getChildFragmentManager(), "new_creation_fragment");
    }

    @Override
    protected void handleRefresh(SwipeRefreshLayout swipeCont) {
        getAllNews();
    }

    public void updateItem(News item, int pos) {
        news.set(pos, item);
        adapter.notifyItemChanged(pos);
    }


    public void addItem(News item) {
        news.add(0, item);
        adapter.notifyItemInserted(0);
        rcItems.scrollToPosition(0);
    }

    private void showBSD(News item, int pos) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(R.layout.bsd_news_admin);
        bottomSheetDialog.findViewById(R.id.edit).setOnClickListener(view -> {
            NewsCreationAdminFragment fr = NewsCreationAdminFragment.newInstance(item, pos);
            fr.setNameCategories(nameCategories);
            fr.setCancelable(true);
            fr.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
            fr.show(getChildFragmentManager(), "nca");
        });
        CheckBox checkBox = bottomSheetDialog.findViewById(R.id.cbActive);
        checkBox.setChecked(item.getKeyActive());
        checkBox.setOnCheckedChangeListener((compoundButton, b) -> News.activeNewsAdmin(item.getObjectId(), b, (object, e) -> {
            if (e == null)
                checkBox.setChecked( ((News)object).getKeyActive());
            else
                Toast.makeText(getContext(), ""+e.toString(), Toast.LENGTH_SHORT).show();
        }));

        bottomSheetDialog.findViewById(R.id.delete).setOnClickListener(view -> {
            News.deleteNews(item.getObjectId(), (object, e) -> {
                if (e == null) {
                    news.remove(pos);
                    adapter.notifyItemRemoved(pos);
                    bottomSheetDialog.dismiss();
                } else {
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        });
        bottomSheetDialog.show();
    }

    private void getAllNews() {
        sRefresh.setRefreshing(true);
        News.getNewsAdmin(new HashMap<>(), (object, e) -> {
            if (e == null) {
                news.clear();
                for (Object o: object){
                    news.add((ParseObject) o);
                }
                adapter.notifyDataSetChanged();
            }
            sRefresh.setRefreshing(false);
        });
    }

    private void setCategory() {
        Category.getAll(new HashMap<>(), (object, e) -> {
            if (e == null){
                nameCategories.clear();
                nameCategories.addAll((Collection<? extends String>) object);
            }
        });
    }
}