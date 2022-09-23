package com.realgear.sudoku_thebestone.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.realgear.sudoku_thebestone.R;
import com.realgear.sudoku_thebestone.core.GameType;
import com.skydoves.expandablelayout.ExpandableLayout;

import org.jetbrains.annotations.NotNull;

import java.util.TreeMap;

public class StatisticsRecyclerViewMainAdapter extends RecyclerView.Adapter<StatisticsRecyclerViewMainAdapter.ViewHolder> {

    private TreeMap<GameType, LevelDataSource> mItems;
    private ViewGroup mParent;

    public StatisticsRecyclerViewMainAdapter(TreeMap<GameType, LevelDataSource> items) {
        this.mItems = (items != null) ? items : new TreeMap<>();
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        this.mParent = parent;
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_statistic_mode_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        if(position >= 0) {
            TextView tvTitle = holder.mExpandableLayout.getParentLayout().findViewById(R.id.textView_title);
            GameType[] gameTypes = mItems.keySet().toArray(new GameType[0]);
            tvTitle.setText(gameTypes[position].name());
            StatisticRecyclerViewAdapter adapter = new StatisticRecyclerViewAdapter(mItems.get(gameTypes[position]));
            holder.mRecyclerView.setLayoutManager(new LinearLayoutManager(mParent.getContext(), LinearLayoutManager.HORIZONTAL, false));
            holder.mRecyclerView.setAdapter(adapter);


            int temp = 0;
            try {
                temp = (int) mParent.getContext().getResources().getDimension(R.dimen.font_size_normal);
                //temp += 4;
            }
            catch (Exception e) {}
            int height = adapter.getItemCount() * temp;
            holder.mRecyclerView.getLayoutParams().height = height;
            holder.mRecyclerView.requestLayout();
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setData(TreeMap<GameType, LevelDataSource> items) {
        this.mItems = items;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ExpandableLayout         mExpandableLayout;
        public RecyclerView             mRecyclerView;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            mExpandableLayout = itemView.findViewById(R.id.expandableLayout_mode_item);
            mExpandableLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mExpandableLayout.toggleLayout();
                }
            });
            //mTableLayout = (AdaptiveRecyclerView) mExpandableLayout.getSecondLayout().findViewById(R.id.adaptiveLayout);
            mRecyclerView = (RecyclerView)mExpandableLayout.getSecondLayout().findViewById(R.id.adaptiveLayout);
        }
    }
}
