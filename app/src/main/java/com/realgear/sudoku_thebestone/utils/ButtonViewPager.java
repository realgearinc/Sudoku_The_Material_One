package com.realgear.sudoku_thebestone.utils;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.realgear.sudoku_thebestone.adapters.StateFragmentAdapter;
import com.realgear.sudoku_thebestone.fragments.FragmentType;
import com.realgear.sudoku_thebestone.theme.Theme;

import java.util.ArrayList;
import java.util.List;

public class ButtonViewPager {
    private final Activity mActivity;

    private final List<Object> mData;

    private final FloatingActionButton mBtn_Prev;
    private final FloatingActionButton mBtn_Next;

    private StateFragmentAdapter mAdapter;
    private ViewPager2 mViewPager;
    private Action mCallback;

    private FragmentType mFragType;

    private Theme mTheme;

    public ButtonViewPager(Activity activity, int prevBtnId, int nextBtnId, int viewPagerId) {
        this.mActivity = activity;
        this.mData = new ArrayList<>();

        this.mBtn_Prev  = (FloatingActionButton) mActivity.findViewById(prevBtnId);
        this.mBtn_Next  = (FloatingActionButton) mActivity.findViewById(nextBtnId);
        this.mViewPager = (ViewPager2) mActivity.findViewById(viewPagerId);

        this.mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                try {
                    mCallback.call();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        this.mBtn_Prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickPrev();
            }
        });
        this.mBtn_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickNext();
            }
        });
    }

    public void setFragmentType(FragmentType fragType) {
        this.mFragType = fragType;
    }

    public void setData(FragmentManager fragmentManager, Lifecycle lifecycle, List<Object> data, List<Fragment> fragments) {
        if(this.mViewPager == null)
            return;

        this.mData.addAll(data);
        this.mAdapter = new StateFragmentAdapter(fragmentManager, lifecycle);
        this.mAdapter.addFragments(fragments);
        this.mViewPager.setAdapter(mAdapter);
        this.mViewPager.setOffscreenPageLimit(mAdapter.getItemCount());
    }

    public void addData(List<Object> data, List<Fragment> fragments) {
        this.mData.addAll(data);
        this.mAdapter.addFragments(fragments);
        this.mAdapter.notifyDataSetChanged();
    }

    private void onClickPrev() {
        if(mViewPager == null)
            return;

        if(mViewPager.getAdapter().getItemCount() == 0)
            return;

        int position = mViewPager.getCurrentItem() - 1;
        if(position >= 0) {
            mViewPager.setCurrentItem(position, true);
        }
        else {
            mViewPager.setCurrentItem(mViewPager.getAdapter().getItemCount() - 1, false);
        }
    }

    private void onClickNext() {
        if(mViewPager == null)
            return;

        if(mViewPager.getAdapter().getItemCount() == 0)
            return;

        int position = mViewPager.getCurrentItem() + 1;
        if(position < mViewPager.getAdapter().getItemCount()) {
            mViewPager.setCurrentItem(position, true);
        }
        else {
            mViewPager.setCurrentItem(0, false);
        }

    }

    public Object getData(int position) {
        if(position >= mData.size())
            return null;

        return mData.get(position);
    }

    public int getCurrentPosition() {
        if(mAdapter == null || mViewPager == null)
            return -1;

        return mViewPager.getCurrentItem();
    }

    public void setOnItemChangedAction(Action action) {
        this.mCallback = action;
    }

    public void setCurrentItem(int item, boolean smoothScroll) {
        if(mViewPager == null)
            return;

        mViewPager.setCurrentItem(item, smoothScroll);
    }

    public void setCurrentItem(int item) {
        if(mViewPager == null)
            return;

        mViewPager.setCurrentItem(item);
    }

    public void setTheme(Theme theme) {
        this.mTheme = theme;

        switch (mFragType) {
            case GAME_MODE_FRAGMENT:
                updateGameModeTypeFragment();
                break;

            case SUDOKU_TYPE_FRAGMENT:
                updateSudokuTypeFragment();
                break;
        }
    }

    private void updateSudokuTypeFragment() {}
    private void updateGameModeTypeFragment() {}

}
