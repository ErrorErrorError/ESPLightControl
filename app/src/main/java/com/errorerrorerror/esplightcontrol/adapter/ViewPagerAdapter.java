package com.errorerrorerror.esplightcontrol.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> pager;

    public ViewPagerAdapter(@NonNull FragmentManager fm, List<Fragment> pager) {
        super(fm);
        this.pager = pager;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return pager.get(position);
    }

    @Override
    public int getCount() {
        return pager.size();
    }
}
