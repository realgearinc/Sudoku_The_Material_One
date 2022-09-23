package com.realgear.sudoku_thebestone.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mList = new ArrayList<>();

    public SimpleFragmentPagerAdapter(@NonNull @NotNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    public void addFragment(Fragment fragment)
    {
        mList.add(fragment);
    }

    @Override
    public int getCount() {
        return mList.size();
    }
}
