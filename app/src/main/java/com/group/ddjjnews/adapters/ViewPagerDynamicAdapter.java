package com.group.ddjjnews.adapters;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.group.ddjjnews.fragments.NestedSavedFragment;

public class ViewPagerDynamicAdapter  extends FragmentStatePagerAdapter {
    private int mNumOfTabs;

    public ViewPagerDynamicAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        // get the current item with position number
        @Override
        public Fragment getItem(int position) {
//            Bundle b = new Bundle();
//            b.putInt("position", position);
            Fragment frag = NestedSavedFragment.newInstance("news " + position);
//            frag.setArguments(b);
            return frag;
        }

        // get total number of tabs
        @Override
        public int getCount() {
            return mNumOfTabs;
        }
}
