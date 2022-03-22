package com.example.qrscaner.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrscaner.Model.GenerateItem;
import com.example.qrscaner.R;

import java.util.List;

public class OptionGenerateAdapter extends RecyclerView.Adapter<OptionGenerateAdapter.ViewHolderItemOption> {
    List<GenerateItem> generateItems;

    public void setGenerateItems(List<GenerateItem> generateItems) {
        this.generateItems = generateItems;
    }

    @NonNull
    @Override
    public ViewHolderItemOption onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_cr_qr, parent, false);
        return new ViewHolderItemOption(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderItemOption holder, int position) {
        GenerateItem generateItem = generateItems.get(position);
        holder.txtNameOption.setText(generateItem.getName());
        holder.imgOption.setImageResource(generateItem.getImage());
    }

    @Override
    public int getItemCount() {
        return generateItems.size();
    }

    class ViewHolderItemOption extends RecyclerView.ViewHolder {
        TextView txtNameOption;
        ImageView imgOption;

        public ViewHolderItemOption(@NonNull View itemView) {
            super(itemView);
            txtNameOption = itemView.findViewById(R.id.tv_select_nameOption);
            imgOption = itemView.findViewById(R.id.imv_select_icon_create);
        }
    }
}
