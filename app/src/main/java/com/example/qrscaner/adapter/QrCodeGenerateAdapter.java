package com.example.qrscaner.adapter;

import android.content.Context;
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

public class QrCodeGenerateAdapter extends RecyclerView.Adapter<QrCodeGenerateAdapter.GenerateViewHolderItem> {
    private List<GenerateItem> generateItemList;
    private Context mContext;
    private BARCODEGenerateAdapter.iCreateQr createQr;

    public QrCodeGenerateAdapter(List<GenerateItem> generateItemList, Context mContext, BARCODEGenerateAdapter.iCreateQr createQr) {
        this.generateItemList = generateItemList;
        this.mContext = mContext;
        this.createQr = createQr;
    }

    @NonNull
    @Override
    public GenerateViewHolderItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_select_cr_qr, parent, false);
        return new GenerateViewHolderItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenerateViewHolderItem holder, int position) {
        GenerateItem generateItem = generateItemList.get(position);
        holder.tvNameItem.setText(generateItem.getName());
        holder.imvIconGen.setImageResource(generateItem.getImage());

    }

    @Override
    public int getItemCount() {
        if (generateItemList!=null){
            return generateItemList.size();
        }
        return 0;
    }

    class GenerateViewHolderItem extends RecyclerView.ViewHolder {
        private ImageView imvIconGen;
        private TextView tvNameItem;

        public GenerateViewHolderItem(@NonNull View itemView) {
            super(itemView);
            imvIconGen = itemView.findViewById(R.id.imv_select_icon_create);
            tvNameItem = itemView.findViewById(R.id.tv_select_nameOption);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GenerateItem generateItem = generateItemList.get(getLayoutPosition());
                    createQr.createListener(generateItem.getId());
                }
            });
        }

    }
}
