package com.example.qrscaner.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrscaner.Model.QrEmail;
import com.example.qrscaner.Model.QrMess;
import com.example.qrscaner.Model.QrScan;
import com.example.qrscaner.Model.QrText;
import com.example.qrscaner.Model.QrUrl;
import com.example.qrscaner.Model.QrWifi;
import com.example.qrscaner.Model.QreTelephone;
import com.example.qrscaner.R;
import com.example.qrscaner.view.fonts.TextViewPoppinBold;

import java.util.List;

public class HistoryAdapterItemQr extends RecyclerView.Adapter<HistoryAdapterItemQr.ViewHolderHistoryItemQr> {
    private List<QrScan> qrList;
    private boolean isEdit;
    private CallEditListener callEdit;


    public HistoryAdapterItemQr(List<QrScan> qrList, boolean isEdit) {
        this.qrList = qrList;
        this.isEdit = isEdit;
        notifyDataSetChanged();
    }

    public HistoryAdapterItemQr(List<QrScan> qrList, boolean isEdit, CallEditListener callEdit) {
        this.qrList = qrList;
        this.isEdit = isEdit;
        this.callEdit = callEdit;
    }

    @NonNull
    @Override
    public ViewHolderHistoryItemQr onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_scan, parent, false);
        return new ViewHolderHistoryItemQr(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderHistoryItemQr holder, int position) {

        QrScan qrScan = qrList.get(position);
        holder.imvItemEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callEdit.edit(false);
            }
        });

        holder.imvItemScanMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.cvItemHistoryQr.setVisibility(View.GONE);
                holder.cvItemHistoryEdit.setVisibility(View.VISIBLE);

            }
        });
        holder.imvItemCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.cvItemHistoryQr.setVisibility(View.VISIBLE);
                holder.cvItemHistoryEdit.setVisibility(View.GONE);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (!qrScan.getIsEdit()) {
                    holder.imvItemCheck.setImageResource(R.drawable.ic_check);
                    qrScan.setEdit(true);
                } else {
                    holder.imvItemCheck.setImageResource(R.drawable.ic_uncheck);
                    qrScan.setEdit(false);

                }
                return true;
            }
        });
        String[] content = qrScan.getScanText().split(":");
        if (content[0].equals("SMSTO")) {
            QrMess qrMess = new QrMess();
            qrMess.compileSMS(content);
            holder.tvItemHistoryQrContent.setText(qrMess.getSendBy());
            holder.imvItemScanType.setImageResource(R.drawable.add_sms);
            holder.tvItemHistoryQrDate.setText(qrScan.getDateString());

        } else if (content[0].equals("Error")) {
            holder.tvItemHistoryQrContent.setText("Error");
            holder.imvItemScanType.setImageResource(R.drawable.ic_error);
            holder.tvItemHistoryQrDate.setText(qrScan.getDateString());

        } else if (content[0].equals("http") || content[0].equals("https")) {
            QrUrl qrUrl = new QrUrl();
            qrUrl.compileUrl(content);
            holder.tvItemHistoryQrContent.setText(qrUrl.getUrl());
            holder.imvItemScanType.setImageResource(R.drawable.add_uri);
            holder.tvItemHistoryQrDate.setText(qrScan.getDateString());
        } else if (content[0].equals("WIFI")) {
            StringBuilder stringBuilder = new StringBuilder();
            String[] contentWifi = qrScan.getScanText().split(";");
            for (String value : contentWifi) {
                stringBuilder.append(value);
            }
            String contentWifi2 = stringBuilder.toString();
            String[] contentWifi3 = contentWifi2.split(":");
            QrWifi qrWifi = new QrWifi();
            qrWifi.compileWifi(contentWifi, contentWifi3);
            holder.tvItemHistoryQrContent.setText(qrWifi.getId());
            holder.imvItemScanType.setImageResource(R.drawable.add_wifi);
            holder.tvItemHistoryQrDate.setText(qrScan.getDateString());
        } else if (content[0].equals("MATMSG")) {
            QrEmail qrEmail = new QrEmail();
            StringBuilder stringBuilder = new StringBuilder();
            for (String value : content) {
                stringBuilder.append(value);
            }

            qrEmail.compileEmail(content);
            holder.tvItemHistoryQrContent.setText(qrEmail.getSendBy());
            holder.imvItemScanType.setImageResource(R.drawable.add_email);
            holder.tvItemHistoryQrDate.setText(qrScan.getDateString());
        } else if (content[0].equals("tel")) {
            QreTelephone qreTelephone = new QreTelephone();
            qreTelephone.compile(content);
            holder.tvItemHistoryQrContent.setText(qreTelephone.getTel());
            holder.imvItemScanType.setImageResource(R.drawable.add_contact);
            holder.tvItemHistoryQrDate.setText(qrScan.getDateString());
        } else {
            QrText qrText = new QrText();
            qrText.setText(qrText.getText());
            holder.tvItemHistoryQrContent.setText(qrText.getText());
            holder.imvItemScanType.setImageResource(R.drawable.add_text);

            holder.tvItemHistoryQrDate.setText(qrScan.getDateString());
        }

    }


    @Override
    public int getItemCount() {
        return qrList.size();
    }

    public class ViewHolderHistoryItemQr extends RecyclerView.ViewHolder {
        TextViewPoppinBold tvItemHistoryQrContent;
        TextView tvItemHistoryQrDate;
        CardView cvItemHistoryQr, cvItemHistoryEdit;
        ImageView imvItemScanType, imvItemScanMenu, imvItemCheck, imvItemShare, imvItemEdit, imvItemDelete;
        LinearLayout imvItemCancel;

        public ViewHolderHistoryItemQr(@NonNull View itemView) {
            super(itemView);
            cvItemHistoryQr = itemView.findViewById(R.id.cv_item_history_scan);
            imvItemCheck = itemView.findViewById(R.id.imv_item_history_scan_check);
            tvItemHistoryQrContent = itemView.findViewById(R.id.tv_item_scan_content);
            tvItemHistoryQrDate = itemView.findViewById(R.id.tv_item_scan_date_create);
            imvItemScanMenu = itemView.findViewById(R.id.imv_item_scan_menu);
            imvItemScanType = itemView.findViewById(R.id.imv_item_scan_type);
            cvItemHistoryEdit = itemView.findViewById(R.id.cv_item_history_edit);
            imvItemCancel = itemView.findViewById(R.id.lnl_item_history_cancel);
            imvItemDelete = itemView.findViewById(R.id.imv_item_history_delete);
            imvItemEdit = itemView.findViewById(R.id.imv_item_history_edit);
            imvItemShare = itemView.findViewById(R.id.imv_item_history_share);
            cvItemHistoryEdit.setVisibility(View.GONE);
            if (!isEdit) {
                imvItemCheck.setVisibility(View.GONE);

            }

        }
    }
    public interface CallEditListener {
        void edit(Boolean isEdit);
    }
}
