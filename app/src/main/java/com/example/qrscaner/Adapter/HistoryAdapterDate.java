package com.example.qrscaner.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrscaner.Model.Date;
import com.example.qrscaner.R;

import java.util.List;

public class HistoryAdapterDate extends RecyclerView.Adapter<HistoryAdapterDate.ViewHolderHistoryDate> {
    List<Date> dateList;
    Context mContext;

    public HistoryAdapterDate(List<Date> dateList, Context mContext) {
        this.dateList = dateList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolderHistoryDate onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_history_date, parent, false);
        return new ViewHolderHistoryDate(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderHistoryDate holder, int position) {
        Date date = dateList.get(position);
        holder.tvItemHistoryDate.setText(date.getDate());
        RecyclerView.LayoutManager llmItemHAD = new LinearLayoutManager(mContext);
        holder.rcvItemHistoryQr.setLayoutManager(llmItemHAD);
        HistoryAdapterItemQr historyAdapterItemQr = new HistoryAdapterItemQr();
        historyAdapterItemQr.setQrList(date.getQrList());
        holder.rcvItemHistoryQr.setAdapter(historyAdapterItemQr);

    }

    @Override
    public int getItemCount() {
        return dateList.size();
    }

    public class ViewHolderHistoryDate extends RecyclerView.ViewHolder {
        TextView tvItemHistoryDate;
        RecyclerView rcvItemHistoryQr;

        public ViewHolderHistoryDate(@NonNull View itemView) {
            super(itemView);
            tvItemHistoryDate = itemView.findViewById(R.id.tv_item_history_month_create);
            rcvItemHistoryQr = itemView.findViewById(R.id.rcv_item_history_qr);
        }
    }
}
