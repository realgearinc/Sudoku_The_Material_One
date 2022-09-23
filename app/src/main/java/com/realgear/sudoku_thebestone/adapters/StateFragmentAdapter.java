package com.realgear.sudoku_thebestone.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class StateFragmentAdapter extends FragmentStateAdapter {

    private List<Fragment> mItems;

    public StateFragmentAdapter(@NonNull @NotNull FragmentManager fragmentManager, @NonNull @NotNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
        mItems = new ArrayList<>();
    }


    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        return mItems.get(position);
    }

    public void addFragment(Fragment fragment) {
        this.mItems.add(fragment);
    }

    public void addFragments(List<Fragment> fragments) {
        this.mItems.addAll(fragments);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public Fragment getItem(int i) {
        return mItems.get(i);
    }
}
