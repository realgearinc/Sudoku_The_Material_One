package com.realgear.sudoku_thebestone.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.realgear.sudoku_thebestone.R;
import com.realgear.sudoku_thebestone.adapters.LevelDataSource;
import com.realgear.sudoku_thebestone.adapters.StatisticsRecyclerViewMainAdapter;
import com.realgear.sudoku_thebestone.core.GameType;
import com.realgear.sudoku_thebestone.core.SudokuTypes;
import com.realgear.sudoku_thebestone.theme.Theme;

import org.jetbrains.annotations.NotNull;

import java.util.TreeMap;

public class StatisticFragment extends Fragment {

    public SudokuTypes mType;
    public RecyclerView mStatisticRecyclerView;

    private TreeMap<GameType, LevelDataSource> mItems;

    private StatisticsRecyclerViewMainAdapter mAdapter;

    public StatisticFragment() {

        //this.name = "";
        //mTypes = GameType.values();
    }

    public StatisticFragment(SudokuTypes type) {
        this.mItems = new TreeMap<>();
        this.mType = type;
        //mTypes = GameType.values();
    }

    public void addItem(GameType type, LevelDataSource data) {

        if(!mItems.containsKey(type)) {
            mItems.put(type, data);
        }
        else {
            mItems.replace(type, data);
        }

        Log.e("StatisticFragment", "Item Added");
        if(mAdapter != null) {
            mAdapter.setData(mItems);
            mAdapter.notifyDataSetChanged();
        }
    }

    public SudokuTypes getSudokuType() {
        return this.mType;
    }

    private Theme mTheme;

    @Override
    public void onViewStateRestored(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_statistic, container, false);

        mStatisticRecyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView_statistics);

        initRecyclerView(mStatisticRecyclerView);

        //initRecyclerViewTop3(mTop3_RecyclerView);
        //initRecyclerViewHistory(mHistory_RecyclerView);

        if(mTheme != null) {
            updateTheme(mTheme);

        }

        return rootView;
    }

    public void initRecyclerView(RecyclerView recyclerView) {
        /*List<LevelDataSourceImpl> data = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            data.add(new LevelDataSourceImpl(getContext()));
        }*/

        mAdapter = new StatisticsRecyclerViewMainAdapter(mItems);
        mStatisticRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mStatisticRecyclerView.setAdapter(mAdapter);

        //StatisticsRecyclerViewMainAdapter mainAdapter = new StatisticsRecyclerViewMainAdapter(null, data);
        //mStatisticRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        //mStatisticRecyclerView.setAdapter(mainAdapter);
    }

    public void updateTheme(Theme theme) {
        mTheme = theme;

        /*if(mTop3_RecyclerView == null || mHistory_RecyclerView == null)
            return;

        StatisticRecyclerViewAdapter adapter = (StatisticRecyclerViewAdapter)mTop3_RecyclerView.getAdapter();
        adapter.setTheme(theme);
        mTop3_RecyclerView.post(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });

        StatisticRecyclerViewAdapter adapter1 = (StatisticRecyclerViewAdapter)mHistory_RecyclerView.getAdapter();
        adapter1.setTheme(theme);
        mHistory_RecyclerView.post(new Runnable() {
            @Override
            public void run() {
                adapter1.notifyDataSetChanged();
            }
        });*/
    }
}
