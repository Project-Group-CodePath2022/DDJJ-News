package com.group.ddjjnews.adapters;
import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


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

    public void clear() {
        fragments.clear();
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
    public Fragment getCurrent(int position) {return fragments.get(position);}

//    @Override
//    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {}
}
