package com.example.qrscaner.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrscaner.models.Color;
import com.example.qrscaner.R;

import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorViewHolder> {
   private List<Color> colorList;
    private SelectColor selectColor;

    public ColorAdapter(List<Color> colorList , SelectColor selectColor) {
        this.colorList = colorList;
        this.selectColor = selectColor;
    }

    @NonNull
    @Override
    public ColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_color_selected,parent,false);
      return new ColorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorViewHolder holder, int position) {
        Color color = colorList.get(position);
        int backGround = color.getColor();
        holder.mLinearLayout.setBackgroundColor(backGround);

    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }

    public class ColorViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout mLinearLayout;

        public ColorViewHolder(@NonNull View itemView) {
            super(itemView);
            mLinearLayout = itemView.findViewById(R.id.lnl_item_color);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectColor.onSelectColor(colorList.get(getLayoutPosition()).getColor());
                }
            });

        }
    }
    public interface SelectColor{
        void onSelectColor(int color);

    }
}
