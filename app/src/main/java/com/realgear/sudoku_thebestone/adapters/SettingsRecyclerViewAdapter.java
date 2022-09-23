package com.realgear.sudoku_thebestone.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.realgear.sudoku_thebestone.R;
import com.realgear.sudoku_thebestone.core.settings.SettingsItem;
import com.realgear.sudoku_thebestone.utils.Action;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SettingsRecyclerViewAdapter extends RecyclerView.Adapter<SettingsRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = SettingsRecyclerViewAdapter.class.getSimpleName();

    List<SettingsItem> items;
    Action mAction;

    public SettingsRecyclerViewAdapter(List<SettingsItem> items) {
        this.items = (items != null) ? items : new ArrayList<>();
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_settings_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        SettingsItem item = this.items.get(position);

        holder.mTitle.setText(item.getTitle());
        holder.mSubtitle.setText(item.getSubtitle());
        holder.mCheckBox.setChecked(item.isEnabled());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void notifyItemChanged(SettingsItem item) {
        notifyItemChanged(this.items.indexOf(item));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitle;
        public TextView mSubtitle;
        //public TextView mInfo;
        public CheckBox mCheckBox;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            mTitle = itemView.findViewById(R.id.textView_title);
            mSubtitle = itemView.findViewById(R.id.textView_subtitle);
            //mInfo = itemView.findViewById(R.id.textView_info);
            mCheckBox = itemView.findViewById(R.id.checkBox_value);

            //mCheckBox.setChecked(items.get(get).isEnabled());

            mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    SettingsItem item = items.get(getAdapterPosition());
                    item.invoke(b);
                }
            });

        }
    }
}
