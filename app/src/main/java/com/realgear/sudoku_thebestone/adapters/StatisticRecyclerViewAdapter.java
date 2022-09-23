package com.realgear.sudoku_thebestone.adapters;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.realgear.sudoku_thebestone.R;
import com.realgear.sudoku_thebestone.core.enums.LevelEnums;
import com.realgear.sudoku_thebestone.theme.Theme;

import org.jetbrains.annotations.NotNull;

public class StatisticRecyclerViewAdapter extends RecyclerView.Adapter<StatisticRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = StatisticRecyclerViewAdapter.class.getSimpleName();

    private LevelEnums[]        mHeaders;
    private LevelDataSource     mData;
    private Theme               mTheme;

    public StatisticRecyclerViewAdapter(LevelDataSource item) {
        this.mData = item;
        this.mHeaders = LevelEnums.values();
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_statistic_level_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.mHeaderTextView.setText(mHeaders[position].name());
        int dataCount = mData.getRowsCount();
        for(int i = 0; i < dataCount; i++) {
            String data = (String) mData.getItemData(i, position);
            TextView textView = new TextView(holder.mParentView.getContext());
            textView.setText(data);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            holder.mParentView.addView(textView);
            textView.requestLayout();
        }
    }

    public void setTheme(Theme theme) {
        this.mTheme = theme;
    }

    @Override
    public int getItemCount() {
        return mHeaders.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout mParentView;
        public TextView mHeaderTextView;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            mParentView = itemView.findViewById(R.id.item_type_data);
            mHeaderTextView = itemView.findViewById(R.id.textView_header);
        }
    }
}
