package com.group.ddjjnews.adapters;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.group.ddjjnews.fragments.NestedNewsFragment;
import com.group.ddjjnews.fragments.NestedSavedFragment;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerDynamicAdapter  extends FragmentStatePagerAdapter {
    private final List<Fragment> fragments = new ArrayList();

    public ViewPagerDynamicAdapter(FragmentManager fm) {
            super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    public void add(Fragment fragment)
    {
        fragments.add(fragment);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
    public Fragment getCurrent(int position) {return fragments.get(position);}

//    @Override
//    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//
//    }
}
