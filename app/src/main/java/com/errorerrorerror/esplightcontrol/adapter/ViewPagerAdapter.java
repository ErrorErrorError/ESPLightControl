package com.errorerrorerror.esplightcontrol.adapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> pager;

    public ViewPagerAdapter(FragmentManager fm, List<Fragment> pager)
    {
        super(fm);
        this.pager = pager;
    }

    @NotNull
    @Override
    public Fragment getItem(int position) {
        return pager.get(position);
    }

    @Override
    public int getCount() {
        return pager.size();
    }
}
