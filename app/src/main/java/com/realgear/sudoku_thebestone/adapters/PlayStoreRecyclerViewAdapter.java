package com.realgear.sudoku_thebestone.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.realgear.sudoku_thebestone.R;
import com.realgear.sudoku_thebestone.core.SkuItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PlayStoreRecyclerViewAdapter extends RecyclerView.Adapter<PlayStoreRecyclerViewAdapter.ViewHolder> {

    private List<SkuItem> mItems;

    public PlayStoreRecyclerViewAdapter(List<SkuItem> items) {
        this.mItems = (items != null) ? items : new ArrayList<>();
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_btn, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        SkuItem item = mItems.get(position);

        holder.mTitle.setText(item.getTitle());
        holder.mSubtitle.setText(item.getDescription());
        holder.mButton.setText((item.isPurchased()) ? "Item Already Purchased" : item.getPrice());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setList(List<SkuItem> items) {
        mItems = items;
    }

    public int setSkuPurchased(String skuId, boolean value) {
        int index = -1;
        for(SkuItem item : mItems) {
            if(item.getSkuDetails().getSku().equals(skuId)) {
                item.setPurchased(value);
                index = mItems.indexOf(item);
                break;
            }
        }
        return index;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitle;
        public TextView mSubtitle;
        public Button   mButton;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            this.mTitle = itemView.findViewById(R.id.textView_product_title);
            this.mSubtitle = itemView.findViewById(R.id.textView_product_subtitle);
            this.mButton = itemView.findViewById(R.id.btn_product);

            if(mButton != null) {
                mButton.setOnClickListener(mItems.get(getLayoutPosition() + 1).getOnClickListener());
            }
        }
    }
}
